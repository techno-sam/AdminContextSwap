package com.slimeist.admin_context_swap.mixin;

import com.mojang.authlib.GameProfile;
import com.slimeist.admin_context_swap.interfaces.PlayerEntityInf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerEntityInf {

    @Shadow @Final public ServerPlayerInteractionManager interactionManager;

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    private NbtList backupInventory = new NbtList();
    private boolean adminMode = false;
    private GameMode backupGameMode = GameMode.CREATIVE;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Override
    public boolean isAdminMode() {
        return this.adminMode;
    }

    @Override
    public void setAdminMode(boolean b) {
        this.adminMode = b;
    }

    @Override
    public void performSwap() {
        PlayerInventory inventory = getInventory();
        NbtList current = inventory.writeNbt(new NbtList());
        inventory.readNbt(backupInventory);
        backupInventory = current;

        GameMode currentGameMode = this.interactionManager.getGameMode();
        this.changeGameMode(backupGameMode);
        backupGameMode = currentGameMode;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void read(NbtCompound nbt, CallbackInfo ci) {
        backupInventory = (NbtList) nbt.get("backupInventory");
        adminMode = nbt.getBoolean("adminMode");
        backupGameMode = GameMode.byId(nbt.getInt("backupGameMode"), GameMode.DEFAULT);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void write(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("backupInventory", backupInventory);
        nbt.putBoolean("adminMode", adminMode);
        nbt.putInt("backupGameMode", backupGameMode.getId());
    }
}
