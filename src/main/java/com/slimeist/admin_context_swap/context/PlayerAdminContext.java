package com.slimeist.admin_context_swap.context;

import com.slimeist.admin_context_swap.interfaces.PlayerEntityInf;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerAdminContext implements ContextCalculator<ServerPlayerEntity> {
    public static final String ADMIN_MODE_KEY = "admin_mode";

    @Override
    public void calculate(@NotNull ServerPlayerEntity target, @NotNull ContextConsumer consumer) {
        consumer.accept(ADMIN_MODE_KEY, ((PlayerEntityInf) target).isAdminMode() ? "true" : "false");
    }

    @Override
    public @NotNull ContextSet estimatePotentialContexts() {
        return ImmutableContextSet.builder()
                .add(ADMIN_MODE_KEY, "true")
                .add(ADMIN_MODE_KEY, "false")
                .build();
    }
}
