package com.emanuelvini.zdiscordcore.spigot.bot.commands.impl;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.bot.commands.Command;
import com.emanuelvini.zdiscordcore.spigot.storage.types.DiscordCode;
import com.emanuelvini.zdiscordcore.spigot.storage.types.PlayerUser;
import jdk.jfr.internal.tool.Main;
import lombok.val;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;
import java.util.UUID;

public class VincularCommand extends Command {
    public VincularCommand() {
        super("vincular", null);
    }



    @Override
    public void execute(Member member, SlashCommandInteractionEvent event) {
        PlayerUser u = MainDiscord.sqlProvider.getUserByDiscordID(member.getId());
        if (u != null) {
            event.reply(MainDiscord.config.getString("messages.discord.already_linked")).queue();
            return;
        }
        DiscordCode code = MainDiscord.sqlProvider.getDiscordCodeByCode(event.getOption("code").getAsString());
        if (code == null) {
            event.reply(MainDiscord.config.getString("messages.discord.code_not_found")).queue();
            return;
        }
        if (code.expired()) {
            event.reply(MainDiscord.config.getString("messages.discord.expired")).queue();
            return;
        }
        MainDiscord.sqlProvider.storeUser(new PlayerUser() {
            @Override
            public String discordID() {
                return member.getId();
            }

            @Override
            public UUID playerLink() {
                return code.playerLink();
            }
        });
        MainDiscord.sqlProvider.removeCode(code.playerCode());
        event.reply(MainDiscord.config.getString("messages.discord.link_success").replace("%nick%", Bukkit.getOfflinePlayer(code.playerLink()).getName())).queue();
        val player = Bukkit.getPlayer(code.playerLink());
        if (player != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',MainDiscord.config.getString("messages.server.linked_success").replace("%user%", event.getUser().getName()+"("+event.getUser().getId()+")")));
        }
        if (MainDiscord.config.getBoolean("debug"))
            Bukkit.getConsoleSender().sendMessage("§e[zDiscordCore] §6[DEBUG] "+event.getUser().getName()+"("+event.getUser().getId()+") foi vinculado a "+Bukkit.getOfflinePlayer(code.playerLink()).getName()+".");
    }

    @Override
    public CommandData commandData() {
        return new CommandDataImpl("vincular", "Vincula seu discord ao seu jogador in-game").
                addOptions(new OptionData(OptionType.STRING, "code", "Código gerado in-game", true));
    }
}
