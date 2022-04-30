package com.emanuelvini.zdiscordcore.spigot.storage.adapter;

import com.emanuelvini.zdiscordcore.spigot.storage.types.DiscordCode;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class DiscordCodeAdapter implements SQLResultAdapter<DiscordCode> {

    @Override
    public DiscordCode adaptResult(SimpleResultSet simpleResultSet) {
        boolean expired = (Integer) simpleResultSet.get("expired") == 1;
        Date createdAt = new Date(Long.parseLong(simpleResultSet.get("createdAt")));
        String playerCode = simpleResultSet.get("code");
        UUID playerLink = UUID.fromString(simpleResultSet.get("uuid"));
        return new DiscordCode() {
            @Override
            public String playerCode() {
                return playerCode;
            }

            @Override
            public Date createdAt() {
                return createdAt;
            }

            @Override
            public boolean expired() {
                return expired;
            }

            @Override
            public UUID playerLink() {
                return playerLink;
            }
        };
    }
}
