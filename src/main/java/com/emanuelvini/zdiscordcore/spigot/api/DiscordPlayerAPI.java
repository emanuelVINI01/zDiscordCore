package com.emanuelvini.zdiscordcore.spigot.api;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.storage.types.PlayerUser;

import java.util.UUID;

public class DiscordPlayerAPI {

    public static PlayerUser getPlayerUserByUUID(UUID uuid) {
        return MainDiscord.sqlProvider.getUserByUUID(uuid);
    }
    public static PlayerUser getPlayerUserByDiscordID(String discord_id) {
        return MainDiscord.sqlProvider.getUserByDiscordID(discord_id);
    }


    public static boolean isPlayerLinked(UUID uuid) {
        return getPlayerUserByUUID(uuid) != null;
    }

    public static boolean isDiscordLinked(String discord_id) {
        return getPlayerUserByDiscordID(discord_id) != null;
    }

}
