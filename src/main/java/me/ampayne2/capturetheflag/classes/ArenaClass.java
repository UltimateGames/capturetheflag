package me.ampayne2.capturetheflag.classes;

import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.entity.Player;

public abstract class ArenaClass {
    
    /**
     * The base class for any arena class.
     */
    public ArenaClass() {
        
    }
    
    /**
     * Equips a player with a class's equipment.
     * @param player The player.
     */
    public abstract void equipPlayer(Player player, Arena arena);

}
