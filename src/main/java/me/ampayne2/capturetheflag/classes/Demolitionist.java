package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Demolitionist extends CTFClass {
    private Game game;
    private static final ItemStack[] ARMOR = new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_HELMET)};
    private static final ItemStack TNT = new ItemStack(Material.TNT, 4);

    public Demolitionist(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Demolitionist");
        setClassIcon(new ItemStack(Material.TNT));
        setIsUnlockable(true);
        setUnlockableString("demolitionist");
        this.game = game;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player) {
        player.getInventory().clear();
        super.resetInventory(player);
        player.getInventory().setArmorContents(ARMOR);
        player.getInventory().addItem(TNT, UGUtils.createInstructionBook(game));
        player.updateInventory();
    }
}
