package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Archer extends CTFClass {
    private static final ItemStack[] TIER_1_ARMOR;
    private static final ItemStack[] TIER_2_ARMOR;
    private static final ItemStack[] TIER_3_ARMOR;
    private static final ItemStack[] TIER_4_ARMOR;
    private static final ItemStack[] TIER_5_ARMOR;
    private static final ItemStack TIER_1_BOW = new ItemStack(Material.BOW);
    private static final ItemStack TIER_2_BOW;
    private static final ItemStack TIER_3_BOW;
    private static final ItemStack TIER_4_BOW;
    private static final ItemStack TIER_5_BOW;
    private static final ItemStack ARROW = new ItemStack(Material.ARROW, 32);

    public Archer(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Archer");
        setClassIcon(TIER_1_BOW);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);
        switch (tier) {
            case 1:
                player.getInventory().setArmorContents(TIER_1_ARMOR);
                player.getInventory().addItem(TIER_1_BOW, ARROW);
                break;
            case 2:
                player.getInventory().setArmorContents(TIER_2_ARMOR);
                player.getInventory().addItem(TIER_2_BOW, ARROW);
                break;
            case 3:
                player.getInventory().setArmorContents(TIER_3_ARMOR);
                player.getInventory().addItem(TIER_3_BOW, ARROW);
                break;
            case 4:
                player.getInventory().setArmorContents(TIER_4_ARMOR);
                player.getInventory().addItem(TIER_4_BOW, ARROW);
                break;
            case 5:
                player.getInventory().setArmorContents(TIER_5_ARMOR);
                player.getInventory().addItem(TIER_5_BOW, ARROW);
        }

        player.updateInventory();
    }

    static {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        TIER_1_ARMOR = new ItemStack[]{null, null, null, helmet};
        TIER_2_ARMOR = new ItemStack[]{null, leggings, null, helmet};
        TIER_3_ARMOR = new ItemStack[]{boots, leggings, null, helmet};
        TIER_4_ARMOR = new ItemStack[]{boots, leggings, chestplate, helmet};
        ItemStack enchantedHelmet = helmet.clone();
        enchantedHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        TIER_5_ARMOR = new ItemStack[]{boots, leggings, chestplate, enchantedHelmet};

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
