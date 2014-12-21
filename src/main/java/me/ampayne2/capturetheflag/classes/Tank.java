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
import me.ampayne2.ultimategames.api.utils.UGUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Tank extends CTFClass {
    private static final ItemStack TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
    private static final ItemStack TIER_2_SWORD;
    private static final ItemStack TIER_3_SWORD;

    public Tank(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Tank", new ItemStack(Material.DIAMOND_BOOTS), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_HELMET));
        setClassIcon(new ItemStack(Material.DIAMOND_CHESTPLATE));
        setIsUnlockable(true);
        setUnlockableString("tank");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);

        switch (tier) {
            case 1:
                player.getInventory().addItem(TIER_1_SWORD);
                break;
            case 2:
                player.getInventory().addItem(TIER_1_SWORD);
                break;
            case 3:
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 4:
                player.getInventory().addItem(TIER_2_SWORD);
                break;
            case 5:
                player.getInventory().addItem(TIER_3_SWORD);
                break;
        }

        player.updateInventory();

        UGUtils.increasePotionEffect(player, PotionEffectType.SLOW);
    }

    static {
        TIER_2_SWORD = TIER_1_SWORD.clone();
        TIER_2_SWORD.addEnchantment(Enchantment.KNOCKBACK, 1);

        TIER_3_SWORD = TIER_1_SWORD.clone();
        TIER_3_SWORD.addEnchantment(Enchantment.KNOCKBACK, 2);
    }
}
