package me.ampayne2.capturetheflag.classes;

import me.ampayne2.capturetheflag.CaptureTheFlag;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.classes.GameClass;
import me.ampayne2.ultimategames.utils.MenuIcon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class CTFClass extends GameClass{

    private Potion potion = new Potion(PotionType.SPEED);
    public CTFClass(UltimateGames ultimateGames, Game game, String name, boolean canSwitchToWithoutDeath) {
        super(ultimateGames, game, name, canSwitchToWithoutDeath);
    }

    @Override
    public void resetInventory(Player player) {
        if (((CaptureTheFlag)getGame().getGamePlugin()).getPlayerSpeedPerk().containsKey(player.getName())) {
            ItemStack stack = new ItemStack(Material.POTION, ((CaptureTheFlag) getGame().getGamePlugin()).getPlayerSpeedPerk().get(player.getName()));
            potion.apply(stack);
            player.getInventory().addItem(stack);
        }
        ItemStack stack = new ItemStack(Material.NETHER_STAR);
        stack.getItemMeta().setDisplayName("Class change");
        player.getInventory().addItem(stack);
        new MenuIcon(UltimateGames.getInstance(), stack, getGame());
    }
}
