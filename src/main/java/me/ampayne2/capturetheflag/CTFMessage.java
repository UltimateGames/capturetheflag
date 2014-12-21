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
