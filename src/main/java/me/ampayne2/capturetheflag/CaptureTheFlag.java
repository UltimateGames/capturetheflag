package me.ampayne2.capturetheflag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ampayne2.capturetheflag.classes.Archer;
import me.ampayne2.capturetheflag.classes.Builder;
import me.ampayne2.capturetheflag.classes.Warrior;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.api.GamePlugin;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.ArenaStatus;
import me.ampayne2.ultimategames.arenas.scoreboards.ArenaScoreboard;
import me.ampayne2.ultimategames.arenas.spawnpoints.PlayerSpawnPoint;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.classes.GameClass;
import me.ampayne2.ultimategames.players.classes.GameClassManager;
import me.ampayne2.ultimategames.players.teams.Team;
import me.ampayne2.ultimategames.players.teams.TeamManager;
import me.ampayne2.ultimategames.signs.ClickInputSign;
import me.ampayne2.ultimategames.signs.SignType;
import me.ampayne2.ultimategames.signs.UGSign;
import me.ampayne2.ultimategames.utils.UGUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CaptureTheFlag extends GamePlugin {

    private UltimateGames ultimateGames;
    private Game game;
    private Archer archer;
    private Builder builder;
    private Warrior warrior;
    private Map<Arena, String> teamBlueFlagHolder = new HashMap<Arena, String>();
    private Map<Arena, String> teamRedFlagHolder = new HashMap<Arena, String>();

    @Override
    public Boolean loadGame(UltimateGames ultimateGames, Game game) {
        this.ultimateGames = ultimateGames;
        this.game = game;
        GameClassManager classManager = ultimateGames.getGameClassManager();
        archer = new Archer(ultimateGames, game, "Archer", false);
        builder = new Builder(ultimateGames, game, "Builder", false);
        warrior = new Warrior(ultimateGames, game, "Warrior", false);
        classManager.registerGameClass(archer);
        classManager.registerGameClass(builder);
        classManager.registerGameClass(warrior);
        return true;
    }

    @Override
    public void unloadGame() {

    }

    @Override
    public Boolean reloadGame() {
        return true;
    }

    @Override
    public Boolean stopGame() {
        return true;
    }

    @Override
    public Boolean loadArena(Arena arena) {
        TeamManager teamManager = ultimateGames.getTeamManager();
        teamManager.addTeam(new Team(ultimateGames, "Blue", arena, ChatColor.BLUE, false));
        teamManager.addTeam(new Team(ultimateGames, "Red", arena, ChatColor.RED, false));
        ultimateGames.addAPIHandler("/" + game.getName() + "/" + arena.getName(), new CaptureTheFlagWebHandler(ultimateGames, arena));
        return true;
    }

    @Override
    public Boolean unloadArena(Arena arena) {
        ultimateGames.getTeamManager().removeTeamsOfArena(arena);
        return true;
    }

    @Override
    public Boolean isStartPossible(Arena arena) {
        return arena.getStatus() == ArenaStatus.OPEN;
    }

    @Override
    public Boolean startArena(Arena arena) {
        return true;
    }

    @Override
    public Boolean beginArena(Arena arena) {
        ultimateGames.getCountdownManager().createEndingCountdown(arena, ultimateGames.getConfigManager().getGameConfig(game).getConfig().getInt("CustomValues.GameTime"), true);

        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().createArenaScoreboard(arena, "Captures");
        scoreBoard.setScore(ChatColor.BLUE + "Team Blue", 0);
        scoreBoard.setScore(ChatColor.RED + "Team Red", 0);
        scoreBoard.setVisible(true);

        GameClassManager classManager = ultimateGames.getGameClassManager();
        TeamManager teamManager = ultimateGames.getTeamManager();
        teamManager.sortPlayersIntoTeams(arena);
        Team blue = teamManager.getTeam(arena, "Blue");
        Team red = teamManager.getTeam(arena, "Red");
        for (String playerName : blue.getPlayers()) {
            Player player = Bukkit.getPlayerExact(playerName);
            scoreBoard.addPlayer(player);
            PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
            spawnPoint.lock(false);
            spawnPoint.teleportPlayer(player);
            blue.setPlayerColorToTeamColor(player);
            classManager.getPlayerClass(game, playerName).resetInventory(player);
        }
        for (String playerName : red.getPlayers()) {
            Player player = Bukkit.getPlayerExact(playerName);
            scoreBoard.addPlayer(player);
            PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 1);
            spawnPoint.lock(false);
            spawnPoint.teleportPlayer(player);
            red.setPlayerColorToTeamColor(player);
            classManager.getPlayerClass(game, playerName).resetInventory(player);
        }
        return true;
    }

    @Override
    public void endArena(Arena arena) {
        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
        Integer teamOneScore = scoreBoard.getScore(ChatColor.BLUE + "Team Blue");
        Integer teamTwoScore = scoreBoard.getScore(ChatColor.RED + "Team Red");
        if (teamOneScore > teamTwoScore) {
            ultimateGames.getMessageManager().sendGameMessage(ultimateGames.getServer(), game, "GameEnd", "Team Blue", game.getName(), arena.getName());
            for (String players : ultimateGames.getTeamManager().getTeam(arena, "Blue").getPlayers()) {
                ultimateGames.getPointManager().addPoint(game, players, "win", 1);
                ultimateGames.getPointManager().addPoint(game, players, "store", 50);
            }
        } else if (teamOneScore < teamTwoScore) {
            ultimateGames.getMessageManager().sendGameMessage(ultimateGames.getServer(), game, "GameEnd", "Team Red", game.getName(), arena.getName());
            for (String players : ultimateGames.getTeamManager().getTeam(arena, "Red").getPlayers()) {
                ultimateGames.getPointManager().addPoint(game, players, "win", 1);
                ultimateGames.getPointManager().addPoint(game, players, "store", 50);
            }
        } else {
            ultimateGames.getMessageManager().sendGameMessage(ultimateGames.getServer(), game, "GameTie", "Team Blue", "Team Red", game.getName(), arena.getName());
        }
        if (teamBlueFlagHolder.containsKey(arena)) {
            teamBlueFlagHolder.remove(arena);
        }
        if (teamRedFlagHolder.containsKey(arena)) {
            teamRedFlagHolder.remove(arena);
        }
    }

    @Override
    public Boolean resetArena(Arena arena) {
        return true;
    }

    @Override
    public Boolean openArena(Arena arena) {
        return true;
    }

    @Override
    public Boolean stopArena(Arena arena) {
        return true;
    }

    @Override
    public Boolean addPlayer(Player player, Arena arena) {
        if (arena.getStatus() == ArenaStatus.OPEN && arena.getPlayers().size() >= arena.getMinPlayers() && !ultimateGames.getCountdownManager().hasStartingCountdown(arena)) {
            ultimateGames.getCountdownManager().createStartingCountdown(arena, ultimateGames.getConfigManager().getGameConfig(game).getConfig().getInt("CustomValues.StartWaitTime"));
        }
        PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getRandomSpawnPoint(arena);
        spawnPoint.lock(false);
        spawnPoint.teleportPlayer(player);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.setHealth(20.0);
        player.setFoodLevel(20);

        warrior.addPlayer(player, true);
        return true;
    }

    @Override
    public void removePlayer(Player player, Arena arena) {
        String playerName = player.getName();
        List<String> queuePlayer = ultimateGames.getQueueManager().getNextPlayers(1, arena);
        TeamManager teamManager = ultimateGames.getTeamManager();
        if (!queuePlayer.isEmpty()) {
            String newPlayerName = queuePlayer.get(0);
            Player newPlayer = Bukkit.getPlayerExact(newPlayerName);
            ultimateGames.getPlayerManager().addPlayerToArena(newPlayer, arena, true);
            Team team = teamManager.getPlayerTeam(playerName);
            if (team != null) {
                team.addPlayer(newPlayer);
            }
        }
        if (teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
            teamBlueFlagHolder.remove(arena);
        }
        if (teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
            teamRedFlagHolder.remove(arena);
        }
        if (arena.getStatus() == ArenaStatus.RUNNING && (teamManager.getTeam(arena, "Red").getPlayers().size() <= 0 || teamManager.getTeam(arena, "Blue").getPlayers().size() <= 0)) {
            ultimateGames.getArenaManager().endArena(arena);
        }
        return;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Boolean addSpectator(Player player, Arena arena) {
        ultimateGames.getSpawnpointManager().getSpectatorSpawnPoint(arena).teleportPlayer(player);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().addItem(UGUtils.createInstructionBook(game));
        player.getInventory().setArmorContents(null);
        player.updateInventory();
        return true;
    }

    @Override
    public void removeSpectator(Player player, Arena arena) {

    }

    @Override
    public void onPlayerDeath(Arena arena, PlayerDeathEvent event) {
        if (arena.getStatus() == ArenaStatus.RUNNING) {
            String playerName = event.getEntity().getName();
            Player killer = event.getEntity().getKiller();
            String killerName = null;
            if (killer != null) {
                killerName = killer.getName();
                ultimateGames.getMessageManager().sendGameMessage(arena, game,"Kill", killerName, event.getEntity().getName());
            } else {
                ultimateGames.getMessageManager().sendGameMessage(arena, game, "Death", event.getEntity().getName());
            }
            if (teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                teamBlueFlagHolder.remove(arena);
                ultimateGames.getMessageManager().sendGameMessage(arena, game, "Drop", playerName, "Team Red");
                for (String player : ultimateGames.getTeamManager().getTeam(arena, "Blue").getPlayers()) {
                    ultimateGames.getPointManager().addPoint(game, player, "carrierKill", 1);
                    ultimateGames.getPointManager().addPoint(game, player, "store", 15);
                }
            } else if (teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                teamRedFlagHolder.remove(arena);
                ultimateGames.getMessageManager().sendGameMessage(arena, game, "Drop", playerName, "Team Blue");
                for (String player : ultimateGames.getTeamManager().getTeam(arena, "Red").getPlayers()) {
                    ultimateGames.getPointManager().addPoint(game, player, "carrierKill", 1);
                    ultimateGames.getPointManager().addPoint(game, player, "store", 15);
                }
            }
        }
        event.getDrops().clear();
        UGUtils.autoRespawn(event.getEntity());
    }

    @Override
    public void onPlayerRespawn(Arena arena, PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        event.setRespawnLocation(ultimateGames.getSpawnpointManager().getSpawnPoint(arena, ultimateGames.getTeamManager().getPlayerTeam(playerName).getName().equals("Blue") ? 0 : 1).getLocation());
        GameClass gameClass = ultimateGames.getGameClassManager().getPlayerClass(game, playerName);
        if (gameClass != null) {
            gameClass.resetInventory(player);
        }
    }

    @Override
    public void onEntityDamage(Arena arena, EntityDamageEvent event) {
        if (arena.getStatus() != ArenaStatus.RUNNING) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityExplode(Arena arena, EntityExplodeEvent event) {
        List<Block> blocks = new ArrayList<Block>(event.blockList());
        for (Block block : blocks) {
            if (block.getType() == Material.FENCE) {
                Block blockUnder = block.getRelative(BlockFace.DOWN);
                while (blockUnder.getType() == Material.FENCE) {
                    blockUnder = blockUnder.getRelative(BlockFace.DOWN);
                }
                if (blockUnder.getType() == Material.GOLD_BLOCK) {
                    event.blockList().remove(block);
                }
            }
        }
    }

    @Override
    public void onBlockBreak(Arena arena, BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.FENCE) {
            Block blockUnder = block.getRelative(BlockFace.DOWN);
            while (blockUnder.getType() == Material.FENCE) {
                blockUnder = blockUnder.getRelative(BlockFace.DOWN);
            }
            if (blockUnder.getType() == Material.GOLD_BLOCK) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onItemDrop(Arena arena, PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void handleUGSignCreate(UGSign ugSign, SignType signType) {
        if (signType == SignType.CLICK_INPUT) {
            ClickInputSign sign = (ClickInputSign) ugSign;
            List<String> lines = new ArrayList<String>();
            lines.add("");
            lines.add("Click to pickup");
            lines.add("or capture flag");
            lines.add("");
            sign.setLines(lines);
        }
    }

    @Override
    public void handleInputSignTrigger(UGSign ugSign, SignType signType, Event event) {
        if (ugSign.getArena().getStatus() == ArenaStatus.RUNNING) {
            if (signType == SignType.CLICK_INPUT) {
                ClickInputSign inputSign = (ClickInputSign) ugSign;
                PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
                Player player = interactEvent.getPlayer();
                String playerName = player.getName();
                Arena arena = inputSign.getArena();
                TeamManager teamManager = ultimateGames.getTeamManager();
                if (inputSign.getLabel().equals("TeamRedFlag")) {
                    if (teamManager.getTeam(arena, "Blue").hasPlayer(playerName) && !teamBlueFlagHolder.containsKey(arena)) {
                        teamBlueFlagHolder.put(arena, playerName);
                        ultimateGames.getMessageManager().sendGameMessage(arena, game, "Pickup", playerName, "Team Red");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6000, 1));
                    } else if (teamManager.getTeam(arena, "Red").hasPlayer(playerName) && teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                        teamRedFlagHolder.remove(arena);
                        ultimateGames.getMessageManager().sendGameMessage(arena, game, "Capture", playerName, "Team Blue");
                        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
                        scoreBoard.setScore(ChatColor.RED + "Team Red", scoreBoard.getScore(ChatColor.RED + "Team Red") + 1);
                        for (String players : ultimateGames.getTeamManager().getTeam(arena, "Red").getPlayers()) {
                            ultimateGames.getPointManager().addPoint(game, players, "capture", 1);
                            ultimateGames.getPointManager().addPoint(game, players, "store", 20);
                        }
                        ultimateGames.getPointManager().addPoint(game, player.getName(), "store", 25);
                        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                            player.removePotionEffect(PotionEffectType.SLOW);
                        }
                    }
                } else if (inputSign.getLabel().equals("TeamBlueFlag")) {
                    if (teamManager.getTeam(arena, "Red").hasPlayer(playerName) && !teamRedFlagHolder.containsKey(arena)) {
                        teamRedFlagHolder.put(arena, playerName);
                        ultimateGames.getMessageManager().sendGameMessage(arena, game, "Pickup", playerName, "Team Blue");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6000, 1));
                    } else if (teamManager.getTeam(arena, "Blue").hasPlayer(playerName) && teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                        teamBlueFlagHolder.remove(arena);
                        ultimateGames.getMessageManager().sendGameMessage(arena, game, "Capture", playerName, "Team Red");
                        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
                        scoreBoard.setScore(ChatColor.BLUE + "Team Blue", scoreBoard.getScore(ChatColor.BLUE + "Team Blue") + 1);
                        for (String players : ultimateGames.getTeamManager().getTeam(arena, "Blue").getPlayers()) {
                            ultimateGames.getPointManager().addPoint(game, players, "store", 20);
                            ultimateGames.getPointManager().addPoint(game, players, "capture", 1);
                        }
                        ultimateGames.getPointManager().addPoint(game, player.getName(), "store", 25);
                        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                            player.removePotionEffect(PotionEffectType.SLOW);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onArenaCommand(Arena arena, String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (command.equals("class") && args.length == 1) {
            String className = args[0].toLowerCase();
            if (className.equals("archer")) {
                archer.addPlayer(player);
            } else if (className.equals("builder")) {
                builder.addPlayer(player);
            } else if (className.equals("warrior")) {
                warrior.addPlayer(player);
            } else {
                ultimateGames.getMessageManager().sendGameMessage(sender, game, "Notaclasstype", className);
            }
        } else if ((arena.getStatus() == ArenaStatus.STARTING || arena.getStatus() == ArenaStatus.OPEN) && command.equals("team") && args.length == 1) {
            String teamName = args[0].toLowerCase();
            TeamManager teamManager = ultimateGames.getTeamManager();
            if (teamName.equals("blue")) {
                Team team = teamManager.getTeam(arena, "Blue");
                if (team.hasSpace()) {
                    teamManager.setPlayerTeam(player, team);
                    PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
                    spawnPoint.lock(false);
                    spawnPoint.teleportPlayer(player);
                } else {
                    ultimateGames.getMessageManager().sendGameMessage(sender, game, "Teamfull", teamName);
                }
            } else if (teamName.equals("red")) {
                Team team = teamManager.getTeam(arena, "Red");
                if (team.hasSpace()) {
                    teamManager.setPlayerTeam(player, team);
                    PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 1);
                    spawnPoint.lock(false);
                    spawnPoint.teleportPlayer(player);
                } else {
                    ultimateGames.getMessageManager().sendGameMessage(sender, game, "Teamfull", teamName);
                }
            } else {
                ultimateGames.getMessageManager().sendGameMessage(sender, game, "Notateam", teamName);
            }
        }
    }

}
