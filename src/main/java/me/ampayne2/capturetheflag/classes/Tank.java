package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tank extends CTFClass {
    private static final ItemStack[] TIER_1_ARMOR;
    private static final ItemStack[] TIER_2_ARMOR;
    private static final ItemStack[] TIER_3_ARMOR;
    private static final ItemStack[] TIER_4_ARMOR;
    private static final ItemStack[] TIER_5_ARMOR;
    private static final ItemStack TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
    private static final ItemStack TIER_2_SWORD;

    public Tank(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Tank");
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
                player.getInventory().setArmorContents(TIER_1_ARMOR);
                player.getInventory().addItem(TIER_1_SWORD);
                break;
            case 2:
                player.getInventory().setArmorContents(TIER_2_ARMOR);
                player.getInventory().addItem(TIER_1_SWORD);
                break;
            case 3:
                player.getInventory().setArmorContents(TIER_3_ARMOR);
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 4:
                player.getInventory().setArmorContents(TIER_4_ARMOR);
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 5:
                player.getInventory().setArmorContents(TIER_5_ARMOR);
                player.getInventory().addItem(TIER_2_SWORD);
                break;
        }

        player.updateInventory();
    }

    static {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        TIER_1_ARMOR = new ItemStack[]{null, null, null, helmet};
        TIER_2_ARMOR = new ItemStack[]{null, leggings, null, helmet};
        TIER_3_ARMOR = new ItemStack[]{boots, leggings, null, helmet};
        TIER_4_ARMOR = new ItemStack[]{boots, leggings, chestplate, helmet};
        ItemStack enchantedHelmet = helmet.clone();
        enchantedHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        TIER_5_ARMOR = new ItemStack[]{boots, leggings, chestplate, enchantedHelmet};

        TIER_2_SWORD = TIER_1_SWORD.clone();
        TIER_2_SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    }
}
