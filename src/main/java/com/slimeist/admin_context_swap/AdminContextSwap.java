package com.slimeist.admin_context_swap;

import com.slimeist.admin_context_swap.commands.AdminCommand;
import com.slimeist.admin_context_swap.context.PlayerAdminContext;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.event.extension.ExtensionLoadEvent;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.SERVER)
public class AdminContextSwap implements DedicatedServerModInitializer {

    public static final String MOD_ID = "admin_context_swap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            LuckPermsProvider.get().getContextManager().registerCalculator(new PlayerAdminContext());
        });

        CommandRegistrationCallback.EVENT.register((commandDispatcher, registryAccess, registrationEnvironment) -> {
            AdminCommand.register(commandDispatcher);
        });
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
