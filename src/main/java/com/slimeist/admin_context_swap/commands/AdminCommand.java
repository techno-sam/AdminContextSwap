package com.slimeist.admin_context_swap.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.slimeist.admin_context_swap.interfaces.PlayerEntityInf;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AdminCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager
                .literal("adminMode")
                .requires(Permissions.require("admin_context_swap.swap", 4))
                .executes(AdminCommand::swap)
                .then(
                        CommandManager.literal("keep-inv")
                                .executes(AdminCommand::swapKeepInv)
                )
                .then(
                        CommandManager.literal("get")
                                .executes(AdminCommand::get)
                )
        );
    }

    private static int get(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity executor = context.getSource().getPlayer();

        if (((PlayerEntityInf) executor).isAdminMode()) {
            executor.sendMessage(Text.literal("Admin mode is enabled"), false);
            return 1;
        } else {
            executor.sendMessage(Text.literal("Admin mode is disabled"), false);
            return 0;
        }
    }

    private static int swapKeepInv(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity executor = context.getSource().getPlayer();

        if (((PlayerEntityInf) executor).isAdminMode()) {
            ((PlayerEntityInf) executor).setAdminMode(false);
            executor.sendMessage(Text.literal("Admin mode disabled (keep inv)"), false);
        } else {
            ((PlayerEntityInf) executor).setAdminMode(true);
            executor.sendMessage(Text.literal("Admin mode enabled (keep inv)"), false);
        }
        LuckPermsProvider.get().getContextManager().signalContextUpdate(executor);
        return 1;
    }

    private static int swap(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity executor = context.getSource().getPlayer();

        if (((PlayerEntityInf) executor).isAdminMode()) {
            ((PlayerEntityInf) executor).setAdminMode(false);
            ((PlayerEntityInf) executor).performSwap();
            executor.sendMessage(Text.literal("Admin mode disabled"), false);
        } else {
            ((PlayerEntityInf) executor).setAdminMode(true);
            ((PlayerEntityInf) executor).performSwap();
            executor.sendMessage(Text.literal("Admin mode enabled"), false);
        }
        LuckPermsProvider.get().getContextManager().signalContextUpdate(executor);
        return 1;
    }
}
