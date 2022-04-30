package com.emanuelvini.zdiscordcore.spigot.storage;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.storage.adapter.DiscordCodeAdapter;
import com.emanuelvini.zdiscordcore.spigot.storage.adapter.PlayerUserAdapter;
import com.emanuelvini.zdiscordcore.spigot.storage.types.DiscordCode;
import com.emanuelvini.zdiscordcore.spigot.storage.types.PlayerUser;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.val;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class SQLProvider {

    private MainDiscord plugin;
    private SQLExecutor executor;

    public SQLProvider(MainDiscord main) {
        plugin = main;

        val connector = getSQLite().connect();
        executor = new SQLExecutor(connector);
        executor.updateQuery("CREATE TABLE IF NOT EXISTS codes (uuid text, code text, createdAt text, expired boolean)");
        executor.updateQuery("CREATE TABLE IF NOT EXISTS users (uuid text, discord_id text)");
        Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §aDatabase iniciada com sucesso.");

    }

    public void storeCode(DiscordCode code) {
        if (getDiscordCodeByCode(code.playerCode()) == null) {
            executor.updateQuery("INSERT INTO codes (uuid,code,createdAt,expired) VALUES (?,?,?,?)", st -> {
                st.set(1, code.playerLink().toString());
                st.set(2, code.playerCode());
                st.set(3, code.createdAt());
                st.set(4, code.expired());
            });
        }
    }

    public DiscordCode getDiscordCodeByCode(String code) {
        DiscordCode c = null;

        for (DiscordCode codeb : getAllCodes()) {
            c = codeb.playerCode().equals(code) ? codeb : c;
        }

        return c;
    }
    public DiscordCode getDiscordCodeByUUID(UUID uuid) {
        DiscordCode c = null;

        for (DiscordCode codeb : getAllCodes()) {
            c = codeb.playerLink().equals(uuid) ? codeb : c;
        }

        return c;
    }

    public void storeUser(PlayerUser user) {
        if (getUserByUUID(user.playerLink()) == null)
            executor.updateQuery("INSERT INTO users (uuid,discord_id) VALUES (?,?)", st -> {
                st.set(1, user.playerLink().toString());
                st.set(2, user.discordID());
            });
    }

    public List<DiscordCode> getAllCodes() {
        val set = executor.resultManyQuery("SELECT * FROM codes", st -> {}, DiscordCodeAdapter.class);
        return new ArrayList<>(set);
    }
    public void removeCode(String code) {
        executor.updateQuery("DELETE FROM codes WHERE code = ?", st -> {
            st.set(1, code);
        });
    }
    public void removeUser(UUID uuid) {
        executor.updateQuery("DELETE FROM users WHERE uuid = ?", st -> {
            st.set(1, uuid.toString());
        });
    }

    public PlayerUser getUserByUUID(UUID uuid) {
        PlayerUser c = null;

        for (PlayerUser user : getAllUsers()) {
            c = user.playerLink().equals(uuid) ? user : c;
        }

        return c;
    }
    public PlayerUser getUserByDiscordID(String discord_id) {
        PlayerUser c = null;

        for (PlayerUser user : getAllUsers()) {
            c = user.discordID().equals(discord_id) ? user : c;
        }

        return c;
    }
    public List<PlayerUser> getAllUsers() {
        val set = executor.resultManyQuery("SELECT * FROM users", st -> {}, PlayerUserAdapter.class);
        return new ArrayList<>(set);
    }

    public SQLiteDatabaseType getSQLite() {
        return SQLiteDatabaseType.builder().file(new File(plugin.getDataFolder(), "database.db")).build();
    }

}
