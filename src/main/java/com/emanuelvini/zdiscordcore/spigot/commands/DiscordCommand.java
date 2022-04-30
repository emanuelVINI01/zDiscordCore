package com.emanuelvini.zdiscordcore.spigot.commands;

import com.emanuelvini.zdiscordcore.spigot.MainDiscord;
import com.emanuelvini.zdiscordcore.spigot.api.DiscordPlayerAPI;
import com.emanuelvini.zdiscordcore.spigot.storage.types.DiscordCode;
import lombok.val;
import me.saiintbrisson.minecraft.command.command.Context;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class DiscordCommand implements CommandExecutor {

    public void handleLinkCommand(Player player) {
        if (DiscordPlayerAPI.isPlayerLinked(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',MainDiscord.config.getString("messages.server.already_linked")));
            return;
        }
        if (MainDiscord.sqlProvider.getDiscordCodeByUUID(player.getUniqueId()) != null && !MainDiscord.sqlProvider.getDiscordCodeByUUID(player.getUniqueId()).expired()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',MainDiscord.config.getString("messages.server.recent_code")));
            return;
        }
        String code_str = RandomStringUtils.randomAlphabetic(6);
        DiscordCode code = new DiscordCode() {
            @Override
            public String playerCode() {
                return code_str;
            }

            @Override
            public Date createdAt() {
                return new Date();
            }

            @Override
            public boolean expired() {
                return false;
            }

            @Override
            public UUID playerLink() {
                return player.getUniqueId();
            }
        };
        MainDiscord.sqlProvider.storeCode(code);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',MainDiscord.config.getString("messages.server.link_message")).
                replace("%codigo%", code_str));
    }

    public void handleUnlinkCommand(Player player) {
        if (!DiscordPlayerAPI.isPlayerLinked(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',MainDiscord.config.getString("messages.server.not_linked")));
            return;
        }
        MainDiscord.sqlProvider.removeUser(player.getUniqueId());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',MainDiscord.config.getString("messages.server.success_unlinked")));
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        if (strings.length != 1) {
            commandSender.sendMessage(MainDiscord.config.getStringList("messages.server.help").stream().map(line -> ChatColor.translateAlternateColorCodes('&',line)).collect(Collectors.joining("\n")));
            return false;
        }
        if ((strings[0].equalsIgnoreCase("vincular") || strings[0].equalsIgnoreCase("link"))) {
            handleLinkCommand((Player) commandSender);
            return false;
        }
        if ((strings[0].equalsIgnoreCase("desvincular") || strings[0].equalsIgnoreCase("unlink"))) {
            handleUnlinkCommand((Player) commandSender);
            return false;
        }
        commandSender.sendMessage(MainDiscord.config.getStringList("messages.server.help").stream().map(line -> ChatColor.translateAlternateColorCodes('&',line)).collect(Collectors.joining("\n")));


        return false;
    }
}
