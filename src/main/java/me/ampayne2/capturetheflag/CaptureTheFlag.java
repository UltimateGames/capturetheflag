package me.ampayne2.capturetheflag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.ampayne2.capturetheflag.classes.Archer;
import me.ampayne2.capturetheflag.classes.ArenaClass;
import me.ampayne2.capturetheflag.classes.Builder;
import me.ampayne2.capturetheflag.classes.ClassType;
//import me.ampayne2.capturetheflag.classes.Demolitionist;
import me.ampayne2.capturetheflag.classes.Warrior;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.api.GamePlugin;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.SpawnPoint;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.enums.SignType;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.scoreboards.ArenaScoreboard;
import me.ampayne2.ultimategames.signs.ClickInputSign;
import me.ampayne2.ultimategames.signs.UGSign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
    private Map<Arena, List<String>> teamBlue = new HashMap<Arena, List<String>>();
    private Map<Arena, List<String>> teamRed = new HashMap<Arena, List<String>>();
    private Map<Arena, String> teamBlueFlagHolder = new HashMap<Arena, String>();
    private Map<Arena, String> teamRedFlagHolder = new HashMap<Arena, String>();
    private Map<String, ArenaClass> playerClasses = new HashMap<String, ArenaClass>();
    private Map<String, ArenaClass> respawnClasses = new HashMap<String, ArenaClass>();
    private ArenaClass archer;
    private ArenaClass builder;
    private ArenaClass warrior;

    @Override
    public Boolean loadGame(UltimateGames ultimateGames, Game game) {
        this.ultimateGames = ultimateGames;
        this.game = game;
        archer = new Archer(ultimateGames, game);
        builder = new Builder(this, ultimateGames, game);
        warrior = new Warrior(ultimateGames, game);
        return true;
    }

    @Override
    public Boolean unloadGame() {
        return true;
    }

    @Override
    public Boolean stopGame() {
        return true;
    }

    @Override
    public Boolean loadArena(Arena arena) {
        teamBlue.put(arena, new ArrayList<String>());
        teamRed.put(arena, new ArrayList<String>());
        ultimateGames.addAPIHandler("/" + game.getName() + "/" + arena.getName(), new CaptureTheFlagWebHandler(ultimateGames, arena));
        return true;
    }

    @Override
    public Boolean unloadArena(Arena arena) {
        teamBlue.remove(arena);
        teamRed.remove(arena);
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
        // Creates a new ending countdown
        ultimateGames.getCountdownManager().createEndingCountdown(arena, ultimateGames.getConfigManager().getGameConfig(game).getConfig().getInt("CustomValues.GameTime"), true);
        
        // Creates a new arena scoreboard and adds team blue and red
        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().createArenaScoreboard(arena, "Captures");
        scoreBoard.setScore(ChatColor.BLUE + "Team Blue", 0);
        scoreBoard.setScore(ChatColor.RED + "Team Red", 0);
        
        List<String> teamOne = new ArrayList<String>(teamBlue.get(arena));
        List<String> teamTwo = new ArrayList<String>(teamRed.get(arena));
        
        Random generator = new Random();
        if (arena.getPlayers().size() % 2 != 0) {
            ultimateGames.getPlayerManager().removePlayerFromArena(Bukkit.getPlayerExact(arena.getPlayers().get(arena.getPlayers().size() - 1)), false);
        }
        while (teamOne.size() + teamTwo.size() != arena.getPlayers().size()) {
            String playerName = arena.getPlayers().get(generator.nextInt(arena.getPlayers().size()));
            Player player = Bukkit.getPlayerExact(playerName);
            if (!teamOne.contains(playerName) && !teamTwo.contains(playerName)) {
                if (teamOne.size() <= teamTwo.size()) {
                    teamOne.add(playerName);
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, player, "Team", ChatColor.BLUE + "Team Blue");
                    scoreBoard.addPlayer(player);
                    scoreBoard.setPlayerColor(player, ChatColor.BLUE);
                    SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
                    spawnPoint.lock(false);
                    spawnPoint.teleportPlayer(player);
                } else if (teamOne.size() > teamTwo.size()) {
                    teamTwo.add(playerName);
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, player, "Team", ChatColor.RED + "Team Red");
                    scoreBoard.addPlayer(player);
                    scoreBoard.setPlayerColor(player, ChatColor.RED);
                    SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 1);
                    spawnPoint.lock(false);
                    spawnPoint.teleportPlayer(player);
                }
            }
        }
        teamBlue.put(arena, teamOne);
        teamRed.put(arena, teamTwo);
        
        for (String playerName : arena.getPlayers()) {
            Player player = Bukkit.getPlayerExact(playerName);
            playerClasses.get(playerName).equipPlayer(player, arena);
        }
        
        scoreBoard.setVisible(true);
        return true;
    }

    @Override
    public void endArena(Arena arena) {
        for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
            if (scoreBoard.getName().equals("Captures")) {
                Integer teamOneScore = scoreBoard.getScore(ChatColor.BLUE + "Team Blue");
                Integer teamTwoScore = scoreBoard.getScore(ChatColor.RED + "Team Red");
                if (teamOneScore > teamTwoScore) {
                    ultimateGames.getMessageManager().broadcastReplacedGameMessage(game, "GameEnd", "Team Blue", game.getName(), arena.getName());
                } else if (teamOneScore < teamTwoScore) {
                    ultimateGames.getMessageManager().broadcastReplacedGameMessage(game, "GameEnd", "Team Red", game.getName(), arena.getName());
                } else {
                    ultimateGames.getMessageManager().broadcastReplacedGameMessage(game, "GameTie", "Team Blue", "Team Red", game.getName(), arena.getName());
                }
            }
        }
        teamBlue.put(arena, new ArrayList<String>());
        teamBlue.put(arena, new ArrayList<String>());
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
        if (arena.getStatus() == ArenaStatus.OPEN && arena.getPlayers().size() >= arena.getMinPlayers() && !ultimateGames.getCountdownManager().isStartingCountdownEnabled(arena)) {
            ultimateGames.getCountdownManager().createStartingCountdown(arena, ultimateGames.getConfigManager().getGameConfig(game).getConfig().getInt("CustomValues.StartWaitTime"));
        }
        SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getRandomSpawnPoint(arena);
        spawnPoint.lock(false);
        spawnPoint.teleportPlayer(player);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        playerClasses.put(player.getName(), warrior);
        warrior.equipPlayer(player, arena);
        ultimateGames.getMessageManager().sendGameMessage(game, player, "Join");
        return true;
    }

    @Override
    public void removePlayer(Player player, Arena arena) {
        String playerName = player.getName();
        String newPlayer = null;
        List<String> queuePlayer = ultimateGames.getQueueManager().getNextPlayers(1, arena);
        if (!queuePlayer.isEmpty()) {
            newPlayer = queuePlayer.get(0);
            ultimateGames.getQueueManager().removePlayerFromQueues(Bukkit.getPlayerExact(newPlayer));
        }
        if (teamBlue.get(arena).contains(playerName)) {
            teamBlue.get(arena).remove(playerName);
            if (teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                teamBlueFlagHolder.remove(arena);
            }
            if (newPlayer != null) {
                teamBlue.get(arena).add(newPlayer);
                for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                    scoreBoard.setPlayerColor(Bukkit.getPlayerExact(newPlayer), ChatColor.BLUE);
                }
            }
        } else if (teamRed.get(arena).contains(playerName)) {
            teamRed.get(arena).remove(playerName);
            if (teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                teamRedFlagHolder.remove(arena);
            }
            if (newPlayer != null) {
                teamRed.get(arena).add(newPlayer);
                for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                    scoreBoard.setPlayerColor(Bukkit.getPlayerExact(newPlayer), ChatColor.RED);
                }
            }
        }
        if (arena.getStatus() == ArenaStatus.RUNNING && !(teamBlue.get(arena).size() == 0 && teamRed.get(arena).size() == 0) && (teamBlue.get(arena).size() == 0 || teamRed.get(arena).size() == 0)) {
            ultimateGames.getArenaManager().endArena(arena);
        } else if (arena.getStatus() == ArenaStatus.OPEN || arena.getStatus() == ArenaStatus.STARTING) {
            if (Math.floor(arena.getPlayers().size() / 2) < teamBlue.get(arena).size()) {
                teamBlue.get(arena).remove(teamBlue.get(arena).size() - 1);
            } else if (Math.floor(arena.getPlayers().size() / 2) < teamRed.get(arena).size()) {
                teamRed.get(arena).remove(teamRed.get(arena).size() - 1);
            }
        }
        playerClasses.remove(playerName);
        return;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public Boolean addSpectator(Player player, Arena arena) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getRandomSpawnPoint(arena);
        spawnPoint.lock(false);
        spawnPoint.teleportPlayer(player);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getInventory().addItem(ultimateGames.getUtils().createInstructionBook(game));
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
                ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Kill", killerName, event.getEntity().getName());
            } else {
                ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Death", event.getEntity().getName());
            }
            if (teamBlue.get(arena).contains(playerName) && teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                teamBlueFlagHolder.remove(arena);
                ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Drop", playerName, "Team Red");
            } else if (teamRed.get(arena).contains(playerName) && teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                teamRedFlagHolder.remove(arena);
                ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Drop", playerName, "Team Blue");
            }
            if (respawnClasses.containsKey(playerName)) {
                playerClasses.put(playerName, respawnClasses.get(playerName));
                respawnClasses.remove(playerName);
            }
        }
        event.getDrops().clear();
        ultimateGames.getUtils().autoRespawn(event.getEntity());
    }

    @Override
    public void onPlayerRespawn(Arena arena, PlayerRespawnEvent event) {
        event.setRespawnLocation(ultimateGames.getSpawnpointManager().getSpawnPoint(arena, teamBlue.get(arena).contains(event.getPlayer().getName()) ? 0 : 1).getLocation());
        playerClasses.get(event.getPlayer().getName()).equipPlayer(event.getPlayer(), arena);
    }

    @Override
    public void onEntityDamage(Arena arena, EntityDamageEvent event) {
        if (arena.getStatus() != ArenaStatus.RUNNING) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onEntityDamageByEntity(Arena arena, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            String playerName = player.getName();
            Player damager;
            String damagerName = "";
            Entity entity = event.getDamager();
            if (entity instanceof Arrow) {
                LivingEntity shooter = ((Arrow) entity).getShooter();
                if (shooter instanceof Player) {
                    damager = (Player) shooter;
                    damagerName = damager.getName();
                    if (!ultimateGames.getPlayerManager().isPlayerInArena(damagerName) || !ultimateGames.getPlayerManager().getPlayerArena(damagerName).equals(arena)) {
                        event.setCancelled(true);
                    }
                }
            } else if (entity instanceof Player) {
                damager = (Player) entity;
                damagerName = damager.getName();
            }
            if ((teamBlue.get(arena).contains(playerName) && teamBlue.get(arena).contains(damagerName)) || (teamRed.get(arena).contains(playerName) && teamRed.get(arena).contains(damagerName))) {
                event.setCancelled(true);
            }
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
                if (inputSign.getLabel().equals("TeamRedFlag")) {
                    if (teamBlue.get(arena).contains(playerName) && !teamBlueFlagHolder.containsKey(arena)) {
                        teamBlueFlagHolder.put(arena, playerName);
                        ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Pickup", playerName, "Team Red");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6000, 1));
                    } else if (teamRed.get(arena).contains(playerName) && teamRedFlagHolder.containsKey(arena) && teamRedFlagHolder.get(arena).equals(playerName)) {
                        teamRedFlagHolder.remove(arena);
                        ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Capture", playerName, "Team Blue");
                        for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                            if (scoreBoard.getName().equals("Captures")) {
                                scoreBoard.setScore(ChatColor.RED + "Team Red", scoreBoard.getScore(ChatColor.RED + "Team Red") + 1);
                            }
                        }
                        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
                            player.removePotionEffect(PotionEffectType.SLOW);
                        }
                    }
                } else if (inputSign.getLabel().equals("TeamBlueFlag")) {
                    if (teamRed.get(arena).contains(playerName) && !teamRedFlagHolder.containsKey(arena)) {
                        teamRedFlagHolder.put(arena, playerName);
                        ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Pickup", playerName, "Team Blue");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6000, 1));
                    } else if (teamBlue.get(arena).contains(playerName) && teamBlueFlagHolder.containsKey(arena) && teamBlueFlagHolder.get(arena).equals(playerName)) {
                        teamBlueFlagHolder.remove(arena);
                        ultimateGames.getMessageManager().broadcastReplacedGameMessageToArena(game, arena, "Capture", playerName, "Team Red");
                        for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                            if (scoreBoard.getName().equals("Captures")) {
                                scoreBoard.setScore(ChatColor.BLUE + "Team Blue", scoreBoard.getScore(ChatColor.BLUE + "Team Blue") + 1);
                            }
                        }
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
        if (command.equals("class") && args.length == 1) {
            ClassType classType = ClassType.fromLabel(args[0].toLowerCase());
            if (classType == null) {
                ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Notaclasstype", args[0]);
                return;
            } else {
                ArenaClass arenaClass;
                Class<? extends ArenaClass> arenaClassType = classType.getClassType();
                if (arenaClassType.equals(Archer.class)) {
                    arenaClass = archer;
                } else if (arenaClassType.equals(Builder.class)) {
                    arenaClass = builder;
                //} else if (arenaClassType.equals(Demolitionist.class)) {
                //    arenaClass = demolitionist;
                } else {
                    arenaClass = warrior;
                }
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    respawnClasses.put(sender.getName(), arenaClass);
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Classchangenextdeath", classType.getLabel());
                } else {
                    playerClasses.put(sender.getName(), arenaClass);
                    arenaClass.equipPlayer((Player) sender, arena);
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Classchange", classType.getLabel());
                }
            }
        } else if ((arena.getStatus() == ArenaStatus.STARTING || arena.getStatus() == ArenaStatus.OPEN) && command.equals("team") && args.length == 1) {
            String teamName = args[0].toLowerCase();
            Player player = (Player) sender;
            ArenaScoreboard scoreBoard = null;
            if (ultimateGames.getScoreboardManager().getArenaScoreboards(arena).size() == 0) {
                scoreBoard = ultimateGames.getScoreboardManager().createArenaScoreboard(arena, "Captures");
            } else {
                for (ArenaScoreboard arenaScoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                    if (arenaScoreBoard.getName().equals("Captures")) {
                        scoreBoard = arenaScoreBoard;
                    }
                }
            }
            if (teamName.equals("blue")) {
                if (Math.floor(arena.getPlayers().size() / 2) > teamBlue.get(arena).size()) {
                    teamBlue.get(arena).add(player.getName());
                    scoreBoard.addPlayer(player);
                    scoreBoard.setPlayerColor(player, ChatColor.BLUE);
                    SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
                    spawnPoint.lock(false);
                    spawnPoint.teleportPlayer(player);
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Teamchange", teamName);
                } else {
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Teamfull", teamName);
                }
            } else if (teamName.equals("red")) {
                if (Math.floor(arena.getPlayers().size() / 2) > teamRed.get(arena).size()) {
                    teamRed.get(arena).add(player.getName());
                    scoreBoard.addPlayer(player);
                    scoreBoard.setPlayerColor(player, ChatColor.RED);
                    SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().getSpawnPoint(arena, 0);
                    spawnPoint.lock(false);
                    spawnPoint.teleportPlayer(player);
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Teamchange", teamName);
                } else {
                    ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Teamfull", teamName);
                }
            } else {
                ultimateGames.getMessageManager().sendReplacedGameMessage(game, (Player) sender, "Notateam", teamName);
            }
        }
    }
    
    public Boolean isPlayerTeamBlue(Arena arena, String playerName) {
        return teamBlue.get(arena).contains(playerName);
    }
    
    public Boolean isPlayerTeamRed(Arena arena, String playerName) {
        return teamRed.get(arena).contains(playerName);
    }

}
