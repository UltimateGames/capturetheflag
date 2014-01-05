package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tank extends CTFClass {
    private static final ItemStack[] ARMOR = new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET)};
    private static final ItemStack SWORD = new ItemStack(Material.STONE_SWORD);

    public Tank(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Tank");
        setClassIcon(new ItemStack(Material.DIAMOND_CHESTPLATE));
        setIsUnlockable(true);
        setUnlockableString("tank");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(ARMOR);
        player.getInventory().addItem(SWORD);
        player.updateInventory();
    }
}
