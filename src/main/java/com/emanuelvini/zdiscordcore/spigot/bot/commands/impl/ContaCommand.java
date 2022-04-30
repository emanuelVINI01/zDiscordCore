package com.emanuelvini.zdiscordcore.spigot.bot.commands.impl;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.api.DiscordPlayerAPI;
import com.emanuelvini.zdiscordcore.spigot.bot.commands.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.bukkit.Bukkit;

public class ContaCommand extends Command {
    public ContaCommand() {
        super("conta", null);
    }

    @Override
    public void execute(Member member, SlashCommandInteractionEvent event) {
        if (DiscordPlayerAPI.isDiscordLinked(event.getUser().getId())) {
            event.reply(MainDiscord.config.getString("messages.discord.linked_show").replace("%nick%", Bukkit.getOfflinePlayer(DiscordPlayerAPI.getPlayerUserByDiscordID(event.getUser().getId()).playerLink()).getName())).queue();
        } else {
            event.reply(MainDiscord.config.getString("messages.discord.not_linked")).queue();
        }
    }

    @Override
    public CommandData commandData() {
        return new CommandDataImpl("conta", "Mostra se você está vinculado a algum nick");
    }
}
