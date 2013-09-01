package me.ampayne2.capturetheflag.classes;

import me.ampayne2.capturetheflag.CaptureTheFlag;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Builder extends ArenaClass {

    private CaptureTheFlag captureTheFlag;
    private UltimateGames ultimateGames;
    private Game game;
    
    public Builder(CaptureTheFlag captureTheFlag, UltimateGames ultimateGames, Game game) {
        this.captureTheFlag = captureTheFlag;
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void equipPlayer(Player player, Arena arena) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        DyeColor dyeColor;
        if (captureTheFlag.isPlayerTeamBlue(arena, player.getName())) {
            dyeColor = DyeColor.BLUE;
        } else if (captureTheFlag.isPlayerTeamRed(arena, player.getName())) {
            dyeColor = DyeColor.RED;
        } else {
            dyeColor = DyeColor.WHITE;
        }
        ItemStack blocks = new ItemStack(Material.STAINED_CLAY, 64, dyeColor.getWoolData());
        player.getInventory().addItem(blocks, new ItemStack(Material.DIAMOND_PICKAXE), new ItemStack(Material.DIAMOND_SPADE), new ItemStack(Material.COOKED_BEEF, 8), ultimateGames.getUtils().createInstructionBook(game));
        player.updateInventory();
    }

}
