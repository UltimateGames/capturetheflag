package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Miner extends ArenaClass {

    private UltimateGames ultimateGames;
    private Game game;
    
    public Miner(UltimateGames ultimateGames, Game game) {
        this.ultimateGames = ultimateGames;
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void equipPlayer(Player player, Arena arena) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE), new ItemStack(Material.DIAMOND_SPADE), new ItemStack(Material.COOKED_BEEF, 8), ultimateGames.getUtils().createInstructionBook(game));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, ultimateGames.getConfigManager().getGameConfig(game).getConfig().getInt("CustomValues.GameTime"), 2));
        player.updateInventory();
    }

}
