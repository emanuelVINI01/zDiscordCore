package com.emanuelvini.zdiscordcore.spigot.bot.listeners;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.bot.DiscordBot;
import com.emanuelvini.zdiscordcore.spigot.bot.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Stream<? extends Command> stream = MainDiscord.bot.commands.stream().filter(command -> event.getName().equals(command.getName()))
                .filter(command -> event.getMember().hasPermission(command.getPermission()));

        if (MainDiscord.bot.commands.stream().filter(command -> event.getName().equals(command.getName()))
                .anyMatch(command -> event.getMember().hasPermission(command.getPermission()))) {
            stream.findFirst().get().execute(event.getMember(), event);

        }
        else {
            event.reply("Ops, não consegui executar esse comando.").queue();
        }
    }
}
