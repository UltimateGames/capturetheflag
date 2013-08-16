package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Archer extends ArenaClass {
    
    private UltimateGames ultimateGames;
    private Game game;
    
    public Archer(UltimateGames ultimateGames, Game game) {
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

    @Override
    public void equipPlayer(Player player, Arena arena) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setArmorContents(
                        new ItemStack[] { new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE),
                                new ItemStack(Material.LEATHER_HELMET) });
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        player.getInventory().addItem(bow, new ItemStack(Material.ARROW), new ItemStack(Material.COOKED_BEEF, 8), ultimateGames.getUtils().createInstructionBook(game));
    }

}
