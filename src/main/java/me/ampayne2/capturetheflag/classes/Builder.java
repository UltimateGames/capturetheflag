package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.teams.TeamManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Builder extends CTFClass {
    private final UltimateGames ultimateGames;
    private static final ItemStack TIER_1_PICKAXE = new ItemStack(Material.STONE_PICKAXE);
    private static final ItemStack TIER_2_PICKAXE = new ItemStack(Material.GOLD_PICKAXE);
    private static final ItemStack TIER_3_PICKAXE = new ItemStack(Material.IRON_PICKAXE);
    private static final ItemStack TIER_4_PICKAXE = new ItemStack(Material.DIAMOND_PICKAXE);
    private static final ItemStack TIER_5_PICKAXE;
    private static final ItemStack TIER_1_SHOVEL = new ItemStack(Material.STONE_SPADE);
    private static final ItemStack TIER_2_SHOVEL = new ItemStack(Material.GOLD_SPADE);
    private static final ItemStack TIER_3_SHOVEL = new ItemStack(Material.IRON_SPADE);
    private static final ItemStack TIER_4_SHOVEL = new ItemStack(Material.DIAMOND_SPADE);
    private static final ItemStack TIER_5_SHOVEL;

    public Builder(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Builder");
        setClassIcon(new ItemStack(Material.STAINED_CLAY));
        this.ultimateGames = ultimateGames;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        String playerName = player.getName();
        player.getInventory().clear();
        super.resetInventory(player, tier);
        player.getInventory().setArmorContents(null);
        DyeColor dyeColor;
        Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
        TeamManager teamManager = ultimateGames.getTeamManager();
        if (teamManager.getTeam(arena, "Blue").hasPlayer(playerName)) {
            dyeColor = DyeColor.BLUE;
        } else if (teamManager.getTeam(arena, "Red").hasPlayer(playerName)) {
            dyeColor = DyeColor.RED;
        } else {
            dyeColor = DyeColor.WHITE;
        }
        ItemStack blocks = new ItemStack(Material.STAINED_CLAY, 64, dyeColor.getWoolData());

        switch (tier) {
            case 1:
                player.getInventory().addItem(blocks, TIER_1_PICKAXE, TIER_1_SHOVEL);
                break;
            case 2:
                player.getInventory().addItem(blocks, TIER_2_PICKAXE, TIER_2_SHOVEL);
                break;
            case 3:
                player.getInventory().addItem(blocks, TIER_3_PICKAXE, TIER_3_SHOVEL);
                break;
            case 4:
                player.getInventory().addItem(blocks, TIER_4_PICKAXE, TIER_4_SHOVEL);
                break;
            case 5:
                player.getInventory().addItem(blocks, TIER_5_PICKAXE, TIER_5_SHOVEL);
                break;
        }

        player.updateInventory();
    }

    static {
        TIER_5_PICKAXE = new ItemStack(Material.DIAMOND_PICKAXE);
        TIER_5_PICKAXE.addEnchantment(Enchantment.DIG_SPEED, 1);

        TIER_5_SHOVEL = new ItemStack(Material.DIAMOND_SPADE);
        TIER_5_SHOVEL.addEnchantment(Enchantment.DIG_SPEED, 1);
    }
}
