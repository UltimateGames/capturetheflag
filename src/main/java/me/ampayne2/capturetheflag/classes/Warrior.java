package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Warrior extends ArenaClass {

    private UltimateGames ultimateGames;
    private Game game;
    
    public Warrior(UltimateGames ultimateGames, Game game) {
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void equipPlayer(Player player, Arena arena) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setArmorContents(
                        new ItemStack[] { new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_CHESTPLATE),
                                new ItemStack(Material.IRON_HELMET) });
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().addItem(sword, new ItemStack(Material.COOKED_BEEF, 8), ultimateGames.getUtils().createInstructionBook(game));
        player.updateInventory();
    }

}
