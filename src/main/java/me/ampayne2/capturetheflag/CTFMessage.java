package me.ampayne2.capturetheflag;

import me.ampayne2.ultimategames.api.message.Message;

public enum CTFMessage implements Message {
    GAME_END("GameEnd", "&b%s won %s on arena %s!"),
    GAME_TIE("GameTie", "&b%s and %s tied %s on arena %s!"),
    KILL("Kill", "%s killed %s!"),
    KILL_FLAGHOLDER("KillFlagHolder", "%s killed %s's flagholder!"),
    DEATH("Death", "%s died!"),
    TEAM("Team", "You are on team %s!"),
    PICKUP_FLAG("PickupFlag", "&4%s picked up %s's flag!"),
    DROP_FLAG("DropFlag", "&4%s dropped %s's flag!"),
    CAPTURE_FLAG("CaptureFlag", "&4%s captured %s's flag!"),
    MOSTCAPTURES("MostCaptures", "%s had the most captures!"),
    MOSTDEFENDS("MostDefends", "%s killed the most flagholders!"),
    NOTATEAM("NotATeam", "&4%s is not an existing team! Choose between blue and red."),
    CLASSNOTUNLOCKED("ClassNotUnlocked", "&4You have not unlocked the %s class!");

    private String message;
    private final String path;
    private final String defaultMessage;

    private CTFMessage(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getDefault() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return message;
    }
}
