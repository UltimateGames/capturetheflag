/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013-2014, UltimateGames Staff <https://github.com/UltimateGames//>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.players.teams.TeamManager;
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
        super(ultimateGames, game, "Builder", null, null, null, null);
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
