package me.ampayne2.capturetheflag;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.scoreboards.Scoreboard;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.api.webapi.WebHandler;
import me.ampayne2.ultimategames.gson.Gson;

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

        Map<String, Integer> map = new HashMap<>();

        Scoreboard scoreBoard = ug.getScoreboardManager().getScoreboard(arena);
        if (scoreBoard != null) {
            for (Team team : ug.getTeamManager().getTeamsOfArena(arena)) {
                map.put("Team " + team.getName(), scoreBoard.getScore(team));
            }
        }

        return gson.toJson(map);
    }
}
