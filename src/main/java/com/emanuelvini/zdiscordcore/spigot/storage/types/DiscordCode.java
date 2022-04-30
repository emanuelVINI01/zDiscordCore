package com.emanuelvini.zdiscordcore.spigot.storage.types;

import java.util.Date;
import java.util.UUID;

public interface DiscordCode {

    String playerCode();

    Date createdAt();

    boolean expired();

    UUID playerLink();


}
