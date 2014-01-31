package me.ampayne2.capturetheflag;

import me.ampayne2.capturetheflag.classes.*;
import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.arenas.scoreboards.Scoreboard;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.PlayerSpawnPoint;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.GamePlugin;
import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.api.players.PlayerManager;
import me.ampayne2.ultimategames.api.players.classes.GameClass;
import me.ampayne2.ultimategames.api.players.classes.GameClassManager;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.api.players.teams.TeamManager;
import me.ampayne2.ultimategames.api.signs.Sign;
import me.ampayne2.ultimategames.api.signs.SignType;
import me.ampayne2.ultimategames.api.utils.UGUtils;
import me.ampayne2.ultimategames.core.signs.ClickInputSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class CaptureTheFlag extends GamePlugin implements Listener {
    private UltimateGames ultimateGames;
    private Game game;
    private Set<String> shouters = new HashSet<>();
    private Map<Arena, String> teamBlueFlagHolder = new HashMap<>();
    private Map<Arena, String> teamRedFlagHolder = new HashMap<>();
    private Map<String, Integer> playerSpeedPerk = new HashMap<>();

    @Override
    public boolean loadGame(UltimateGames ultimateGames, Game game) {
        this.ultimateGames = ultimateGames;
        this.game = game;

        ultimateGames.getGameClassManager().registerGameClass(new Archer(ultimateGames, game)).registerGameClass(new Builder(ultimateGames, game)).registerGameClass(new Warrior(ultimateGames, game)).registerGameClass(new Tank(ultimateGames, game)).registerGameClass(new Demolitionist(ultimateGames, game)).registerGameClass(new Medic(ultimateGames, game)).registerGameClass(new Toxicologist(ultimateGames, game));

        return true;
    }

    @Override
    public void unloadGame() {
    }

    @Override
    public boolean reloadGame() {
        return true;
    }

    @Override
    public boolean stopGame() {
        return true;
    }

    @Override
    public boolean loadArena(Arena arena) {
        TeamManager teamManager = ultimateGames.getTeamManager();
        teamManager.createTeam(ultimateGames, "Blue", arena, ChatColor.BLUE, false, false);
        teamManager.createTeam(ultimateGames, "Red", arena, ChatColor.RED, false, false);
        ultimateGames.addAPIHandler("/" + game.getName() + "/" + arena.getName(), new CaptureTheFlagWebHandler(ultimateGames, arena));
        return true;
    }

    @Override
    public boolean unloadArena(Arena arena) {
        ultimateGames.getTeamManager().removeTeamsOfArena(arena);
        return true;
    }

    @Override
    public boolean isStartPossible(Arena arena) {
        return arena.getStatus() == ArenaStatus.OPEN;
    }

    @Override
    public boolean startArena(Arena arena) {
        return true;
    }

    @Override
    public boolean beginArena(Arena arena) {
        ultimateGames.getCountdownManager().createEndingCountdown(arena, ultimateGames.getConfigManager().getGameConfig(game).getInt("CustomValues.GameTime"), true);

        GameClassManager classManager = ultimateGames.getGameClassManager();
        TeamManager teamManager = ultimateGames.getTeamManager();
        teamManager.sortPlayersIntoTeams(arena);
        Team blue = teamManager.getTeam(arena, "Blue");
        Team red = teamManager.getTeam(arena, "Red");
        Scoreboard scoreBoard = ultimateGames.getScoreboardManager().createScoreboard(arena, "Captures");
        scoreBoard.setScore(blue, 0);
        scoreBoard.setScore(red, 0);
        scoreBoard.setVisible(true);
        for (String playerName : blue.getPlayers()) {
            Player player = Bukkit.getPlayerExact(playerName);
            PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
            spawnPoint.lock(false);
            spawnPoint.teleportPlayer(player);
            scoreBoard.addPlayer(player, blue);
            classManager.getPlayerClass(game, playerName).resetInventory(player);
        }
        for (String playerName : red.getPlayers()) {
            Player player = Bukkit.getPlayerExact(playerName);
            PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 1);
            spawnPoint.lock(false);
            spawnPoint.teleportPlayer(player);
            scoreBoard.addPlayer(player, red);
            classManager.getPlayerClass(game, playerName).resetInventory(player);
        }
        return true;
    }

    @Override
    public void endArena(Arena arena) {
        TeamManager teamManager = ultimateGames.getTeamManager();
        Team blue = teamManager.getTeam(arena, "Blue");
        Team red = teamManager.getTeam(arena, "Red");
        Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
        int teamOneScore = scoreBoard.getScore(blue);
        int teamTwoScore = scoreBoard.getScore(red);
        if (teamOneScore > teamTwoScore) {
            ultimateGames.getMessenger().sendGameMessage(Bukkit.getServer(), game, "GameEnd", "Team Blue", game.getName(), arena.getName());
            for (String player : blue.getPlayers()) {
                ultimateGames.getPointManager().addPoint(game, player, "win", 1);
                ultimateGames.getPointManager().addPoint(game, player, "store", 50);
            }
        } else if (teamOneScore < teamTwoScore) {
            ultimateGames.getMessenger().sendGameMessage(Bukkit.getServer(), game, "GameEnd", "Team Red", game.getName(), arena.getName());
            for (String player : red.getPlayers()) {
                ultimateGames.getPointManager().addPoint(game, player, "win", 1);
                ultimateGames.getPointManager().addPoint(game, player, "store", 50);
            }
        } else {
            ultimateGames.getMessenger().sendGameMessage(Bukkit.getServer(), game, "GameTie", "Team Blue", "Team Red", game.getName(), arena.getName());
            for (String player : arena.getPlayers()) {
                ultimateGames.getPointManager().addPoint(game, player, "store", 25);
            }
        }
        if (teamBlueFlagHolder.containsKey(arena)) {
            teamBlueFlagHolder.remove(arena);
        }
        if (teamRedFlagHolder.containsKey(arena)) {
            teamRedFlagHolder.remove(arena);
        }
    }

    @Override
    public boolean openArena(Arena arena) {
        return true;
    }

    @Override
    public boolean stopArena(Arena arena) {
        return true;
    }

    @Override
    public boolean addPlayer(Player player, Arena arena) {
        if (arena.getStatus() == ArenaStatus.OPEN && arena.getPlayers().size() >= arena.getMinPlayers() && !ultimateGames.getCountdownManager().hasStartingCountdown(arena)) {
            ultimateGames.getCountdownManager().createStartingCountdown(arena, ultimateGames.getConfigManager().getGameConfig(game).getInt("CustomValues.StartWaitTime"));
        }
        PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getRandomSpawnPoint(arena);
        spawnPoint.lock(false);
        spawnPoint.teleportPlayer(player);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.setHealth(20.0);
        player.setFoodLevel(20);

        ultimateGames.getGameClassManager().getGameClass(game, "Warrior").addPlayer(player, true, false);
        if (ultimateGames.getPointManager().hasPerk(game, player.getName(), "speedpotion4")) {
            playerSpeedPerk.put(player.getName(), 4);
        } else if (ultimateGames.getPointManager().hasPerk(game, player.getName(), "speedpotion3")) {
            playerSpeedPerk.put(player.getName(), 3);
        } else if (ultimateGames.getPointManager().hasPerk(game, player.getName(), "speedpotion2")) {
            playerSpeedPerk.put(player.getName(), 2);
        } else if (ultimateGames.getPointManager().hasPerk(game, player.getName(), "speedpotion1")) {
            playerSpeedPerk.put(player.getName(), 1);
        }
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
                teamManager.setPlayerTeam(newPlayer, team);
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
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean addSpectator(Player player, Arena arena) {
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
        Player player = event.getEntity();
        if (arena.getStatus() == ArenaStatus.RUNNING) {
            String playerName = player.getName();
            Player killer = player.getKiller();
            if (killer != null) {
                ultimateGames.getMessenger().sendGameMessage(arena, game, "Kill", killer.getName(), event.getEntity().getName());
            } else {
                ultimateGames.getMessenger().sendGameMessage(arena, game, "Death", event.getEntity().getName());
            }
            if (teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                teamBlueFlagHolder.remove(arena);
                ultimateGames.getMessenger().sendGameMessage(arena, game, "Drop", playerName, "Team Red");
                for (String redPlayer : ultimateGames.getTeamManager().getTeam(arena, "Red").getPlayers()) {
                    ultimateGames.getPointManager().addPoint(game, redPlayer, "carrierKill", 1);
                    ultimateGames.getPointManager().addPoint(game, redPlayer, "store", 15);
                }
            } else if (teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                teamRedFlagHolder.remove(arena);
                ultimateGames.getMessenger().sendGameMessage(arena, game, "Drop", playerName, "Team Blue");
                for (String bluePlayer : ultimateGames.getTeamManager().getTeam(arena, "Blue").getPlayers()) {
                    ultimateGames.getPointManager().addPoint(game, bluePlayer, "carrierKill", 1);
                    ultimateGames.getPointManager().addPoint(game, bluePlayer, "store", 15);
                }
            }
        }
        event.getDrops().clear();
        UGUtils.autoRespawn(ultimateGames.getPlugin(), event.getEntity());
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
        for (Block block : new ArrayList<>(event.blockList())) {
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
    public void onBlockPlace(Arena arena, BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.TNT) {
            block.setType(Material.AIR);
            Location location = block.getLocation();
            TNTPrimed tnt = (TNTPrimed) location.getWorld().spawnEntity(location.add(0.5, 0.5, 0.5), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(40);
            tnt.setVelocity(new Vector(0.0, 0.2, 0.0));
        }
    }

    @Override
    public void onItemDrop(Arena arena, PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void handleUGSignCreate(Sign ugSign, SignType signType) {
        if (signType == SignType.CLICK_INPUT) {
            ClickInputSign sign = (ClickInputSign) ugSign;
            List<String> lines = new ArrayList<>();
            lines.add("");
            lines.add("Click to pickup");
            lines.add("or capture flag");
            lines.add("");
            sign.setLines(lines);
        }
    }

    @Override
    public void handleInputSignTrigger(Sign ugSign, SignType signType, Event event) {
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
                        ultimateGames.getMessenger().sendGameMessage(arena, game, "Pickup", playerName, "Team Red");
                        UGUtils.increasePotionEffect(player, PotionEffectType.SLOW);
                    } else if (teamManager.getTeam(arena, "Red").hasPlayer(playerName) && teamRedFlagHolder.containsKey(arena) && !teamBlueFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                        teamRedFlagHolder.remove(arena);
                        ultimateGames.getMessenger().sendGameMessage(arena, game, "Capture", playerName, "Team Blue");
                        Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
                        scoreBoard.setScore(ChatColor.RED + "Team Red", scoreBoard.getScore(ChatColor.RED + "Team Red") + 1);
                        for (String players : ultimateGames.getTeamManager().getTeam(arena, "Red").getPlayers()) {
                            if (!players.equals(player.getName())) {
                                ultimateGames.getPointManager().addPoint(game, players, "store", 20);
                            }
                        }
                        ultimateGames.getPointManager().addPoint(game, player.getName(), "capture", 1);
                        ultimateGames.getPointManager().addPoint(game, player.getName(), "store", 25);
                        UGUtils.decreasePotionEffect(player, PotionEffectType.SLOW, 1);

                        if (scoreBoard.getScore(ChatColor.RED + "Team Red") == 3) {
                            ultimateGames.getArenaManager().endArena(arena);
                        }
                    }
                } else if (inputSign.getLabel().equals("TeamBlueFlag")) {
                    if (teamManager.getTeam(arena, "Red").hasPlayer(playerName) && !teamRedFlagHolder.containsKey(arena)) {
                        teamRedFlagHolder.put(arena, playerName);
                        ultimateGames.getMessenger().sendGameMessage(arena, game, "Pickup", playerName, "Team Blue");
                        UGUtils.increasePotionEffect(player, PotionEffectType.SLOW);
                    } else if (teamManager.getTeam(arena, "Blue").hasPlayer(playerName) && teamBlueFlagHolder.containsKey(arena) && !teamRedFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                        teamBlueFlagHolder.remove(arena);
                        ultimateGames.getMessenger().sendGameMessage(arena, game, "Capture", playerName, "Team Red");
                        Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
                        scoreBoard.setScore(ChatColor.BLUE + "Team Blue", scoreBoard.getScore(ChatColor.BLUE + "Team Blue") + 1);
                        for (String players : ultimateGames.getTeamManager().getTeam(arena, "Blue").getPlayers()) {
                            if (!players.equals(player.getName())) {
                                ultimateGames.getPointManager().addPoint(game, players, "store", 20);
                            }
                        }
                        ultimateGames.getPointManager().addPoint(game, player.getName(), "capture", 1);
                        ultimateGames.getPointManager().addPoint(game, player.getName(), "store", 25);
                        UGUtils.decreasePotionEffect(player, PotionEffectType.SLOW, 1);

                        if (scoreBoard.getScore(ChatColor.BLUE + "Team Blue") == 3) {
                            ultimateGames.getArenaManager().endArena(arena);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onArenaCommand(Arena arena, String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if ((arena.getStatus() == ArenaStatus.STARTING || arena.getStatus() == ArenaStatus.OPEN) && command.equals("team") && args.length == 1) {
            String teamName = args[0].toLowerCase();
            TeamManager teamManager = ultimateGames.getTeamManager();
            switch (args[0].toLowerCase()) {
                case "blue":
                    Team team = teamManager.getTeam(arena, "Blue");
                    if (team.hasSpace()) {
                        teamManager.setPlayerTeam(player, team);
                        PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
                        spawnPoint.lock(false);
                        spawnPoint.teleportPlayer(player);
                    } else {
                        ultimateGames.getMessenger().sendGameMessage(sender, game, "Teamfull", teamName);
                    }
                    break;
                case "red":
                    team = teamManager.getTeam(arena, "Red");
                    if (team.hasSpace()) {
                        teamManager.setPlayerTeam(player, team);
                        PlayerSpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 1);
                        spawnPoint.lock(false);
                        spawnPoint.teleportPlayer(player);
                    } else {
                        ultimateGames.getMessenger().sendGameMessage(sender, game, "Teamfull", teamName);
                    }
                    break;
                default:
                    ultimateGames.getMessenger().sendGameMessage(sender, game, "Notateam", teamName);

            }
        } else if (arena.getStatus() == ArenaStatus.RUNNING && command.equalsIgnoreCase("shout")) {
            StringBuilder builder = new StringBuilder();
            for (String s : args) {
                builder.append(s);
                builder.append(" ");
            }
            shouters.add(player.getName());
            player.chat(builder.toString());
            shouters.remove(player.getName());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        PlayerManager playerManager = ultimateGames.getPlayerManager();
        if (playerManager.isPlayerInArena(playerName)) {
            Arena arena = playerManager.getPlayerArena(playerName);
            if (arena.getGame().equals(game) && arena.getStatus() == ArenaStatus.RUNNING && !shouters.contains(playerName)) {
                Team team = ultimateGames.getTeamManager().getPlayerTeam(playerName);
                Messenger messenger = ultimateGames.getMessenger();
                messenger.sendRawMessage(team, String.format(messenger.getGameMessage(game, "teamprefix"), team.getColor() + playerName) + event.getMessage());
                event.setCancelled(true);
                ultimateGames.getMessenger().getLogger().info("<" + playerName + "> " + event.getMessage());
            }
        }
    }

    public Map<String, Integer> getPlayerSpeedPerk() {
        return playerSpeedPerk;
    }

    @Override
    public void onPlayerItemConsume(Arena arena, PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.POTION)) {
            int amount = playerSpeedPerk.get(event.getPlayer().getName()) - 1;
            if (amount <= 0) {
                playerSpeedPerk.remove(event.getPlayer().getName());
            } else {
                playerSpeedPerk.put(event.getPlayer().getName(), amount);
            }
        }
    }
}
