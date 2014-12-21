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
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@SuppressWarnings("deprecation")
public class Toxicologist extends CTFClass {
    private static final ItemStack BOOTS;
    private static final ItemStack LEGGINGS;
    private static final ItemStack CHESTPLATE;
    private static final ItemStack HELMET;
    private static final ItemStack TIER_1_DAMAGE;
    private static final ItemStack TIER_2_DAMAGE;
    private static final ItemStack TIER_1_POISON;
    private static final ItemStack TIER_2_POISON;
    private static final ItemStack TIER_1_SWORD;
    private static final ItemStack TIER_2_SWORD;
    private static final ItemStack TIER_3_SWORD;

    public Toxicologist(UltimateGames ultimateGames, Game game) {
        super(ultimateGames, game, "Toxicologist", BOOTS, LEGGINGS, CHESTPLATE, HELMET);
        setClassIcon(TIER_1_DAMAGE);
        setIsUnlockable(true);
        setUnlockableString("toxicologist");
    }

    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().clear();
        super.resetInventory(player, tier);

        switch (tier) {
            case 1:
                player.getInventory().addItem(TIER_1_SWORD, TIER_1_DAMAGE);
                break;
            case 2:
                player.getInventory().addItem(TIER_1_SWORD, TIER_2_DAMAGE);
                break;
            case 3:
                player.getInventory().addItem(TIER_2_SWORD, TIER_1_DAMAGE, TIER_1_POISON);
                break;
            case 4:
                player.getInventory().addItem(TIER_2_SWORD, TIER_2_DAMAGE, TIER_1_POISON);
                break;
            case 5:
                player.getInventory().addItem(TIER_3_SWORD, TIER_2_DAMAGE, TIER_2_POISON);
                break;
        }

        player.updateInventory();
    }

    static {
        BOOTS = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootMeta = (LeatherArmorMeta) BOOTS.getItemMeta();
        bootMeta.setColor(Color.BLACK);
        BOOTS.setItemMeta(bootMeta);

        LEGGINGS = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingMeta = (LeatherArmorMeta) LEGGINGS.getItemMeta();
        leggingMeta.setColor(Color.BLACK);
        LEGGINGS.setItemMeta(leggingMeta);

        CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) CHESTPLATE.getItemMeta();
        chestplateMeta.setColor(Color.BLACK);
        CHESTPLATE.setItemMeta(chestplateMeta);

        HELMET = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) HELMET.getItemMeta();
        helmetMeta.setColor(Color.BLACK);
        HELMET.setItemMeta(helmetMeta);

        TIER_1_SWORD = new ItemStack(Material.WOOD_SWORD);
        TIER_1_SWORD.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        TIER_2_SWORD = TIER_1_SWORD.clone();
        TIER_2_SWORD.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        TIER_3_SWORD = new ItemStack(Material.STONE_SWORD);
        TIER_3_SWORD.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

        TIER_1_DAMAGE = new ItemStack(Material.POTION, 2);
        new Potion(PotionType.INSTANT_DAMAGE, 1, true).apply(TIER_1_DAMAGE);

        TIER_2_DAMAGE = TIER_1_DAMAGE.clone();
        TIER_2_DAMAGE.setAmount(4);

        TIER_1_POISON = new ItemStack(Material.POTION, 2);
        new Potion(PotionType.POISON, 1, true).apply(TIER_1_POISON);

        TIER_2_POISON = TIER_1_POISON.clone();
        TIER_2_POISON.setAmount(4);
    }
}
