package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Medic extends CTFClass {
    private static final ItemStack[] ARMOR = new ItemStack[]{new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_HELMET)};
    private static final ItemStack SWORD = new ItemStack(Material.GOLD_SWORD);
    private static final ItemStack HEALTH;

    public Medic(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Medic");
        setClassIcon(HEALTH);
        setIsUnlockable(true);
        setUnlockableString("medic");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(ARMOR);
        player.getInventory().addItem(SWORD, HEALTH);
        player.updateInventory();
    }

    static {
        HEALTH = new ItemStack(Material.POTION, 4);
        new Potion(PotionType.INSTANT_HEAL, 1, true).apply(HEALTH);
    }
}
