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
        );
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
