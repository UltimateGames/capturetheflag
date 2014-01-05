package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.teams.TeamManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Builder extends CTFClass {
    private UltimateGames ultimateGames;
    private static final ItemStack PICKAXE = new ItemStack(Material.DIAMOND_PICKAXE);
    private static final ItemStack SHOVEL = new ItemStack(Material.DIAMOND_SPADE);

    public Builder(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Builder");
        setClassIcon(new ItemStack(Material.STAINED_CLAY));
        this.ultimateGames = ultimateGames;
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
        player.getInventory().addItem(blocks, PICKAXE, SHOVEL);
        player.updateInventory();
    }
}
