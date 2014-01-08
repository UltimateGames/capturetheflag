package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Archer extends CTFClass {
    private static final ItemStack TIER_1_BOW = new ItemStack(Material.BOW);
    private static final ItemStack TIER_2_BOW;
    private static final ItemStack TIER_3_BOW;
    private static final ItemStack TIER_4_BOW;
    private static final ItemStack TIER_5_BOW;
    private static final ItemStack ARROW = new ItemStack(Material.ARROW, 32);

    public Archer(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Archer", new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET));
        setClassIcon(TIER_1_BOW);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);
        switch (tier) {
            case 1:
                player.getInventory().addItem(TIER_1_BOW, ARROW);
                break;
            case 2:
                player.getInventory().addItem(TIER_2_BOW, ARROW);
                break;
            case 3:
                player.getInventory().addItem(TIER_3_BOW, ARROW);
                break;
            case 4:
                player.getInventory().addItem(TIER_4_BOW, ARROW);
                break;
            case 5:
                player.getInventory().addItem(TIER_5_BOW, ARROW);
        }

        player.updateInventory();
    }

    static {
        TIER_2_BOW = TIER_1_BOW.clone();
        TIER_2_BOW.addEnchantment(Enchantment.ARROW_DAMAGE, 1);

        TIER_3_BOW = TIER_1_BOW.clone();
        TIER_3_BOW.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

        TIER_4_BOW = TIER_3_BOW.clone();
        TIER_4_BOW.addEnchantment(Enchantment.ARROW_FIRE, 1);

        TIER_5_BOW = TIER_4_BOW.clone();
        TIER_5_BOW.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    }
}
