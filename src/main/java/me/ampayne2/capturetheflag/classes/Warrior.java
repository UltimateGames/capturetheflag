package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Warrior extends CTFClass {
    private static final ItemStack TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
    private static final ItemStack TIER_2_SWORD = new ItemStack(Material.STONE_SWORD);
    private static final ItemStack TIER_3_SWORD = new ItemStack(Material.GOLD_SWORD);
    private static final ItemStack TIER_4_SWORD = new ItemStack(Material.IRON_SWORD);
    private static final ItemStack TIER_5_SWORD;

    public Warrior(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Warrior", new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET));
        setClassIcon(TIER_4_SWORD);
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
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 3:
                player.getInventory().addItem(TIER_3_SWORD);
                break;
            case 4:
                player.getInventory().addItem(TIER_4_SWORD);
                break;
            case 5:
                player.getInventory().addItem(TIER_5_SWORD);
                break;
        }

        player.updateInventory();
    }

    static {
        TIER_5_SWORD = TIER_4_SWORD.clone();
        TIER_5_SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    }
}
