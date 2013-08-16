package me.ampayne2.capturetheflag.classes;

import me.ampayne2.capturetheflag.CaptureTheFlag;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class Builder extends ArenaClass {

    private CaptureTheFlag captureTheFlag;
    private UltimateGames ultimateGames;
    private Game game;
    
    public Builder(CaptureTheFlag captureTheFlag, UltimateGames ultimateGames, Game game) {
        this.captureTheFlag = captureTheFlag;
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

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
        ItemStack wool = new Wool(dyeColor).toItemStack();
        wool.setAmount(64);
        player.getInventory().addItem(wool, new ItemStack(Material.SHEARS), new ItemStack(Material.COOKED_BEEF, 8), ultimateGames.getUtils().createInstructionBook(game));
    }

}
