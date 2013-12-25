package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import me.ampayne2.ultimategames.players.classes.GameClass;
import me.ampayne2.ultimategames.players.teams.TeamManager;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Builder extends CTFClass {

    private UltimateGames ultimateGames;
    private Game game;

    public Builder(UltimateGames ultimateGames, Game game, String name, boolean canSwitchToWithoutDeath) {
        super(ultimateGames, game, name, canSwitchToWithoutDeath);
        setClassIcon(new ItemStack(Material.STAINED_CLAY));
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        String playerName = player.getName();
        Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(null);
        DyeColor dyeColor;
        TeamManager teamManager = ultimateGames.getTeamManager();
        if (teamManager.getTeam(arena, "Blue").hasPlayer(playerName)) {
            dyeColor = DyeColor.BLUE;
        } else if (teamManager.getTeam(arena, "Red").hasPlayer(playerName)) {
            dyeColor = DyeColor.RED;
        } else {
            dyeColor = DyeColor.WHITE;
        }
        ItemStack blocks = new ItemStack(Material.STAINED_CLAY, 64, dyeColor.getWoolData());
        player.getInventory().addItem(blocks, new ItemStack(Material.DIAMOND_PICKAXE), new ItemStack(Material.DIAMOND_SPADE), new ItemStack(Material.COOKED_BEEF, 8),
                UGUtils.createInstructionBook(game));
        player.updateInventory();
    }

}
