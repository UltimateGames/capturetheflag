package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Warrior extends CTFClass {
    private UltimateGames ultimateGames;
    private Game game;
    private static final ItemStack[] ARMOR = new ItemStack[]{new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_HELMET)};
    private static final ItemStack SWORD;

    public Warrior(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Warrior");
        setClassIcon(new ItemStack(Material.IRON_SWORD));
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(ARMOR);
        player.getInventory().addItem(SWORD, UGUtils.createInstructionBook(game));
        player.updateInventory();
    }

    static {
        SWORD = new ItemStack(Material.IRON_SWORD);
        SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    }
}
