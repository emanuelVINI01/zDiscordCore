package com.emanuelvini.zdiscordcore.spigot.storage.adapter;

import com.emanuelvini.zdiscordcore.spigot.storage.types.PlayerUser;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;

import java.util.UUID;

public class PlayerUserAdapter implements SQLResultAdapter<PlayerUser> {
    @Override
    public PlayerUser adaptResult(SimpleResultSet simpleResultSet) {
        String discordID = simpleResultSet.get("discord_id");
        UUID playerLink = UUID.fromString(simpleResultSet.get("uuid"));
        return new PlayerUser() {
            @Override
            public String discordID() {
                return discordID;
            }

            @Override
            public UUID playerLink() {
                return playerLink;
            }
        };
    }
}
