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
    private static final ItemStack[] ARMOR;
    private static final ItemStack SWORD;
    private static final ItemStack POISON;
    private static final ItemStack DAMAGE;

    public Toxicologist(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Toxicologist");
        ItemStack icon = POISON.clone();
        icon.setAmount(1);
        setClassIcon(icon);
        setIsUnlockable(true);
        setUnlockableString("toxicologist");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(ARMOR);
        player.getInventory().addItem(SWORD, POISON, DAMAGE);
        player.updateInventory();
    }

    static {
        SWORD = new ItemStack(Material.STONE_SWORD);
        SWORD.addEnchantment(Enchantment.DURABILITY, 1);

        POISON = new ItemStack(Material.POTION, 8);
        new Potion(PotionType.POISON, 1, true).apply(POISON);
        DAMAGE = new ItemStack(Material.POTION, 8);
        new Potion(PotionType.INSTANT_DAMAGE, 1, true).apply(DAMAGE);

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

        ARMOR = new ItemStack[] {boots, leggings, chestplate, helmet};
    }
}
