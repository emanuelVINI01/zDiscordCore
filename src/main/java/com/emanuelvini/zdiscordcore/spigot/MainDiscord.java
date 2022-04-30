package com.emanuelvini.zdiscordcore.spigot;

import com.emanuelvini.zdiscordcore.spigot.bot.DiscordBot;
import com.emanuelvini.zdiscordcore.spigot.commands.DiscordCommand;
import com.emanuelvini.zdiscordcore.spigot.storage.SQLProvider;
import com.emanuelvini.zdiscordcore.spigot.storage.types.DiscordCode;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainDiscord extends JavaPlugin {
    public static Configuration config;
    public static SQLProvider sqlProvider;
    public static DiscordBot bot;
    @Override
    public void onEnable() {
        super.onEnable();
        saveDefaultConfig();
        config = getConfig();
        sqlProvider = new SQLProvider(this);
        try {
            bot = new DiscordBot(config.getString("bot.token"), config.getString("bot.status"));
            Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §aBot ligado com sucesso, "+bot.getBot().getSelfUser().getName()+" iniciado.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §cFalha ao iniciar bot, token inválido.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            new Thread() {
                @Override
                public void run() {
                    sqlProvider.getAllCodes().forEach(code -> {
                        if (TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - code.createdAt().getTime()) >= 5 && !code.expired()) {
                            sqlProvider.removeCode(code.playerCode());
                            sqlProvider.storeCode(new DiscordCode() {
                                @Override
                                public String playerCode() {
                                    return code.playerCode();
                                }

                                @Override
                                public Date createdAt() {
                                    return code.createdAt();
                                }

                                @Override
                                public boolean expired() {
                                    return true;
                                }

                                @Override
                                public UUID playerLink() {
                                    return code.playerLink();
                                }
                            });
                            if (config.getBoolean("debug"))
                                Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §6[DEBUG] Código "+code.playerCode()+" expirado.");
                        }
                    });
                }
            }.start();
        }, 0, 20);
        getCommand("discord").setExecutor(new DiscordCommand());
        Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §aPlugin ligado com sucesso.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (bot != null) {
            bot.getBot().shutdown();
            Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §6Bot desativado com sucesso.");
        }
        Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §aPlugin desativado com sucesso.");
    }
}
