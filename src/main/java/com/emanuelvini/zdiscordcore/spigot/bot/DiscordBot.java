package com.emanuelvini.zdiscordcore.spigot.bot;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.bot.commands.Command;
import com.emanuelvini.zdiscordcore.spigot.bot.commands.impl.ContaCommand;
import com.emanuelvini.zdiscordcore.spigot.bot.commands.impl.VincularCommand;
import com.emanuelvini.zdiscordcore.spigot.bot.listeners.MessageListener;
import lombok.val;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class DiscordBot {

    private JDA bot;
    public List<Command> commands = new ArrayList<>();

    public DiscordBot(String token, String status) throws LoginException, InterruptedException {
        val builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing(status));
        builder.addEventListeners(new MessageListener());
        commands.add(new VincularCommand());
        commands.add(new ContaCommand());
        bot = builder.build().awaitReady();
        bot.getGuilds().forEach(guild -> {
            commands.forEach(command -> {
                guild.upsertCommand(command.commandData()).queue();
                if (MainDiscord.config.getBoolean("debug"))
                    Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §6[DEBUG] Comando "+command.getName()+" registrado na guild " + guild.getName() + "(" +guild.getId()+").");
            });
            guild.updateCommands().queue();
        });

    }

    public JDA getBot() {
        return bot;
    }
}
