package me.ampayne2.capturetheflag.classes;

import me.ampayne2.capturetheflag.CaptureTheFlag;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.classes.TieredClass;
import me.ampayne2.ultimategames.utils.MenuIcon;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class CTFClass extends TieredClass {
    private UltimateGames ultimateGames;
    private final ItemStack instructions;
    private static final ItemStack CLASS_SELECTOR;
    private static final ItemStack FOOD = new ItemStack(Material.COOKED_BEEF, 8);
    private static final Potion POTION = new Potion(PotionType.SPEED);
    private static final ItemStack GOLDEN_APPLE = new ItemStack(Material.GOLDEN_APPLE);
    private static final ItemStack ENDER_PEARL = new ItemStack(Material.ENDER_PEARL);

    public CTFClass(UltimateGames ultimateGames, Game game, String name) {
        super(ultimateGames, game, name, false);
        this.ultimateGames = ultimateGames;
        instructions = UGUtils.createInstructionBook(game);
    }

    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().addItem(instructions, CLASS_SELECTOR, FOOD);
        new MenuIcon(ultimateGames, CLASS_SELECTOR, getGame());
        if (((CaptureTheFlag) getGame().getGamePlugin()).getPlayerSpeedPerk().containsKey(player.getName())) {
            ItemStack stack = new ItemStack(Material.POTION, ((CaptureTheFlag) getGame().getGamePlugin()).getPlayerSpeedPerk().get(player.getName()));
            POTION.apply(stack);
            player.getInventory().addItem(stack);
        }
        if (tier >= 4 && ultimateGames.getPointManager().hasPerk(getGame(), player.getName(), "goldenapple")) {
            player.getInventory().addItem(GOLDEN_APPLE);
        }
        if (tier == 5 && ultimateGames.getPointManager().hasPerk(getGame(), player.getName(), "enderpearl")) {
            player.getInventory().addItem(ENDER_PEARL);
        }
    }

    static {
        CLASS_SELECTOR = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = CLASS_SELECTOR.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Class selector");
        CLASS_SELECTOR.setItemMeta(meta);
    }
}
