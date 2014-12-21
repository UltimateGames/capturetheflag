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
import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Archer extends CTFClass {
    private static final ItemStack TIER_1_BOW = new ItemStack(Material.BOW);
    private static final ItemStack TIER_2_BOW;
    private static final ItemStack TIER_3_BOW;
    private static final ItemStack TIER_4_BOW;
    private static final ItemStack TIER_5_BOW;
    private static final ItemStack ARROW = new ItemStack(Material.ARROW, 32);

    public Archer(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Archer", new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET));
        setClassIcon(TIER_1_BOW);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);
        switch (tier) {
            case 1:
                player.getInventory().addItem(TIER_1_BOW, ARROW);
                break;
            case 2:
                player.getInventory().addItem(TIER_2_BOW, ARROW);
                break;
            case 3:
                player.getInventory().addItem(TIER_3_BOW, ARROW);
                break;
            case 4:
                player.getInventory().addItem(TIER_4_BOW, ARROW);
                break;
            case 5:
                player.getInventory().addItem(TIER_5_BOW, ARROW);
        }

        player.updateInventory();
    }

    static {
        TIER_2_BOW = TIER_1_BOW.clone();
        TIER_2_BOW.addEnchantment(Enchantment.ARROW_DAMAGE, 1);

        TIER_3_BOW = TIER_1_BOW.clone();
        TIER_3_BOW.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

        TIER_4_BOW = TIER_3_BOW.clone();
        TIER_4_BOW.addEnchantment(Enchantment.ARROW_FIRE, 1);

        TIER_5_BOW = TIER_4_BOW.clone();
        TIER_5_BOW.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    }
}
