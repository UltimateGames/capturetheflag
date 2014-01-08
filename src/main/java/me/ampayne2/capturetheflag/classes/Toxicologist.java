package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Toxicologist extends CTFClass {
    private static final ItemStack[] TIER_1_ARMOR;
    private static final ItemStack[] TIER_2_ARMOR;
    private static final ItemStack[] TIER_3_ARMOR;
    private static final ItemStack[] TIER_4_ARMOR;
    private static final ItemStack[] TIER_5_ARMOR;
    private static final ItemStack TIER_1_DAMAGE;
    private static final ItemStack TIER_2_DAMAGE;
    private static final ItemStack TIER_1_POISON;
    private static final ItemStack TIER_2_POISON;
    private static final ItemStack TIER_1_SWORD;
    private static final ItemStack TIER_2_SWORD;
    private static final ItemStack TIER_3_SWORD;

    public Toxicologist(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Toxicologist");
        setClassIcon(TIER_1_DAMAGE);
        setIsUnlockable(true);
        setUnlockableString("toxicologist");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);

        switch (tier) {
            case 1:
                player.getInventory().setArmorContents(TIER_1_ARMOR);
                player.getInventory().addItem(TIER_1_SWORD, TIER_1_DAMAGE);
                break;
            case 2:
                player.getInventory().setArmorContents(TIER_2_ARMOR);
                player.getInventory().addItem(TIER_1_SWORD, TIER_2_DAMAGE);
                break;
            case 3:
                player.getInventory().setArmorContents(TIER_3_ARMOR);
                player.getInventory().addItem(TIER_2_SWORD, TIER_2_DAMAGE, TIER_1_POISON);
                break;
            case 4:
                player.getInventory().setArmorContents(TIER_4_ARMOR);
                player.getInventory().addItem(TIER_2_SWORD, TIER_2_DAMAGE, TIER_2_POISON);
                break;
            case 5:
                player.getInventory().setArmorContents(TIER_5_ARMOR);
                player.getInventory().addItem(TIER_3_SWORD, TIER_2_DAMAGE, TIER_2_POISON);
                break;
        }

        player.updateInventory();
    }

    static {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootMeta.setColor(Color.BLACK);
        boots.setItemMeta(bootMeta);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingMeta = (LeatherArmorMeta) boots.getItemMeta();
        leggingMeta.setColor(Color.BLACK);
        leggings.setItemMeta(leggingMeta);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) boots.getItemMeta();
        chestplateMeta.setColor(Color.BLACK);
        chestplate.setItemMeta(chestplateMeta);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) boots.getItemMeta();
        helmetMeta.setColor(Color.BLACK);
        helmet.setItemMeta(helmetMeta);

        TIER_1_ARMOR = new ItemStack[]{null, null, null, helmet};
        TIER_2_ARMOR = new ItemStack[]{null, leggings, null, helmet};
        TIER_3_ARMOR = new ItemStack[]{boots, leggings, null, helmet};
        TIER_4_ARMOR = new ItemStack[]{boots, leggings, chestplate, helmet};
        ItemStack enchantedHelmet = helmet.clone();
        enchantedHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        TIER_5_ARMOR = new ItemStack[]{boots, leggings, chestplate, enchantedHelmet};

        TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
        TIER_1_SWORD.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        TIER_2_SWORD = TIER_1_SWORD.clone();
        TIER_2_SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        TIER_3_SWORD = new ItemStack(Material.STONE_SWORD);
        TIER_3_SWORD.addEnchantment(Enchantment.DURABILITY, 10);

        TIER_1_DAMAGE = new ItemStack(Material.POTION, 4);
        new Potion(PotionType.INSTANT_DAMAGE, 1, true).apply(TIER_1_DAMAGE);

        TIER_2_DAMAGE = TIER_1_DAMAGE.clone();
        TIER_2_DAMAGE.setAmount(8);

        TIER_1_POISON = new ItemStack(Material.POTION, 4);
        new Potion(PotionType.POISON, 1, true).apply(TIER_1_POISON);

        TIER_2_POISON = TIER_1_POISON.clone();
        TIER_2_POISON.setAmount(8);
    }
}
