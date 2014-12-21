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
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@SuppressWarnings("deprecation")
public class Medic extends CTFClass {
    private static final ItemStack TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
    private static final ItemStack TIER_2_SWORD;
    private static final ItemStack TIER_3_SWORD = new ItemStack(Material.GOLD_SWORD);
    private static final ItemStack TIER_4_SWORD;
    private static final ItemStack TIER_5_SWORD = new ItemStack(Material.STONE_SWORD);
    private static final ItemStack HEALTH;

    public Medic(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Medic", new ItemStack(Material.GOLD_BOOTS), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_HELMET));
        setClassIcon(HEALTH);
        setIsUnlockable(true);
        setUnlockableString("medic");
    }

    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);

        switch (tier) {
            case 1:
                player.getInventory().addItem(TIER_1_SWORD, HEALTH);
                break;
            case 2:
                player.getInventory().addItem(TIER_2_SWORD, HEALTH);
                break;
            case 3:
                player.getInventory().addItem(TIER_3_SWORD, HEALTH);
                break;
            case 4:
                player.getInventory().addItem(TIER_4_SWORD, HEALTH);
                break;
            case 5:
                player.getInventory().addItem(TIER_5_SWORD, HEALTH);
        }

        player.updateInventory();
    }

    static {
        TIER_2_SWORD = TIER_1_SWORD.clone();
        TIER_2_SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        TIER_4_SWORD = TIER_3_SWORD.clone();
        TIER_4_SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        HEALTH = new ItemStack(Material.POTION, 8);
        new Potion(PotionType.INSTANT_HEAL, 1, true).apply(HEALTH);
    }
}
