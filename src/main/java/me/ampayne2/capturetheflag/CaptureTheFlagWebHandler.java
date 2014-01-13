package me.ampayne2.capturetheflag;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.scoreboards.ArenaScoreboard;
import me.ampayne2.ultimategames.gson.Gson;
import me.ampayne2.ultimategames.players.teams.Team;
import me.ampayne2.ultimategames.players.teams.TeamManager;
import me.ampayne2.ultimategames.webapi.WebHandler;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class CaptureTheFlagWebHandler implements WebHandler {
    private Arena arena;
    private UltimateGames ug;

    public CaptureTheFlagWebHandler(UltimateGames ug, Arena arena) {
        this.arena = arena;
        this.ug = ug;
    }

    @Override
    public String sendResult() {
        Gson gson = new Gson();

        Map<String, Integer> map = new HashMap<String, Integer>();

        ArenaScoreboard scoreBoard = ug.getScoreboardManager().getScoreboard(arena);
        if (scoreBoard != null) {
            for (Team team : ug.getTeamManager().getTeamsOfArena(arena)) {
                map.put("Team " + team.getName(), scoreBoard.getScore(team));
            }
        }

        return gson.toJson(map);
    }
}
