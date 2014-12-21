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

import me.ampayne2.capturetheflag.CaptureTheFlag;
import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.items.GameItem;
import me.ampayne2.ultimategames.api.players.classes.ClassSelector;
import me.ampayne2.ultimategames.api.players.classes.TieredClass;
import me.ampayne2.ultimategames.api.utils.UGUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CTFClass extends TieredClass {
    private UltimateGames ultimateGames;
    private final ItemStack instructions;
    private final GameItem CLASS_SELECTOR;
    private final ItemStack boots;
    private final ItemStack leggings;
    private final ItemStack chestplate;
    private final ItemStack helmet;
    private static final ItemStack FOOD = new ItemStack(Material.COOKED_BEEF, 8);
    private static final Potion POTION = new Potion(PotionType.SPEED);
    private static final ItemStack GOLDEN_APPLE = new ItemStack(Material.GOLDEN_APPLE);
    private static final ItemStack ENDER_PEARL = new ItemStack(Material.ENDER_PEARL);

    public CTFClass(UltimateGames ultimateGames, Game game, String name, ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet) {
        super(ultimateGames, game, name, false);
        this.ultimateGames = ultimateGames;
        instructions = UGUtils.createInstructionBook(game);

        this.boots = boots;
        this.leggings = leggings;
        this.chestplate = chestplate;
        this.helmet = helmet;

        CLASS_SELECTOR = new ClassSelector(ultimateGames);
        ultimateGames.getGameItemManager()
                .registerGameItem(game, CLASS_SELECTOR);
    }

    @Override
    public void resetInventory(Player player, int tier) {
        player.getInventory().addItem(instructions, CLASS_SELECTOR.getItem());
        player.getInventory().addItem(FOOD);
        ItemStack[] armor = new ItemStack[]{boots, leggings, chestplate, helmet};
        if (tier > 1 && chestplate != null) {
            ItemStack newChestplate = chestplate.clone();
            newChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, tier - 1);
            armor[2] = newChestplate;
        }
        player.getInventory().setArmorContents(armor);
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

        UGUtils.removePotionEffect(player, PotionEffectType.SLOW);
    }
}
