package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Archer extends CTFClass {
    private Game game;
    private static final ItemStack[] ARMOR = new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)};
    private static final ItemStack BOW;
    private static final ItemStack ARROW = new ItemStack(Material.ARROW);

    public Archer(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Archer");
        setClassIcon(BOW);
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(ARMOR);
        player.getInventory().addItem(BOW, ARROW, UGUtils.createInstructionBook(game));
        player.updateInventory();
    }

    static {
        BOW = new ItemStack(Material.BOW);
        BOW.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        BOW.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    }
}
