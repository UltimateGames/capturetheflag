package me.ampayne2.capturetheflag.classes;

import me.ampayne2.capturetheflag.CaptureTheFlag;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.classes.GameClass;
import me.ampayne2.ultimategames.utils.MenuIcon;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class CTFClass extends GameClass {
    private UltimateGames ultimateGames;
    private final ItemStack instructions;
    private static final ItemStack CLASS_SELECTOR;
    private static final ItemStack FOOD = new ItemStack(Material.COOKED_BEEF, 8);
    private static final Potion POTION = new Potion(PotionType.SPEED);

    public CTFClass(UltimateGames ultimateGames, Game game, String name) {
        super(ultimateGames, game, name, false);
        this.ultimateGames = ultimateGames;
        instructions = UGUtils.createInstructionBook(game);
    }

    @Override
    public void resetInventory(Player player) {
        player.getInventory().addItem(instructions, CLASS_SELECTOR, FOOD);
        new MenuIcon(ultimateGames, CLASS_SELECTOR, getGame());
        if (((CaptureTheFlag) getGame().getGamePlugin()).getPlayerSpeedPerk().containsKey(player.getName())) {
            ItemStack stack = new ItemStack(Material.POTION, ((CaptureTheFlag) getGame().getGamePlugin()).getPlayerSpeedPerk().get(player.getName()));
            POTION.apply(stack);
            player.getInventory().addItem(stack);
        }
    }

    static {
        CLASS_SELECTOR = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = CLASS_SELECTOR.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Class selector");
        CLASS_SELECTOR.setItemMeta(meta);
    }
}
