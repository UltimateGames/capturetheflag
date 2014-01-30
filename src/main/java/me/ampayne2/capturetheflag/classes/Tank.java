package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tank extends CTFClass {
    private static final ItemStack TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
    private static final ItemStack TIER_2_SWORD;
    private static final ItemStack TIER_3_SWORD;

    public Tank(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Tank", new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET));
        setClassIcon(new ItemStack(Material.DIAMOND_CHESTPLATE));
        setIsUnlockable(true);
        setUnlockableString("tank");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);

        switch (tier) {
            case 1:
                player.getInventory().addItem(TIER_1_SWORD);
                break;
            case 2:
                player.getInventory().addItem(TIER_1_SWORD);
                break;
            case 3:
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 4:
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 5:
                player.getInventory().addItem(TIER_3_SWORD);
                break;
        }

        player.updateInventory();
    }

    static {
        TIER_2_SWORD = TIER_1_SWORD.clone();
        TIER_2_SWORD.addEnchantment(Enchantment.KNOCKBACK, 1);

        TIER_3_SWORD = TIER_1_SWORD.clone();
        TIER_3_SWORD.addEnchantment(Enchantment.KNOCKBACK, 2);
    }
}
