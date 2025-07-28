package org.mesdag.map_wormhole;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;
import java.util.function.Supplier;

public record WormholeToPlayerPacketC2S(UUID playerId) {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MapWormhole.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUUID(playerId);
    }

    public static WormholeToPlayerPacketC2S decode(FriendlyByteBuf friendlyByteBuf) {
        return new WormholeToPlayerPacketC2S(friendlyByteBuf.readUUID());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;
            ServerPlayer target = sender.server.getPlayerList().getPlayer(playerId);
            if (target != null && sender.getTeam() == target.getTeam()) {
                ItemStack potion = getWormholePotion(sender);
                if (!potion.isEmpty()) {
                    potion.shrink(1);
                    teleport(sender, target);
                }
            }
        });
        context.setPacketHandled(true);
    }

    public static boolean isTrackable(ServerPlayer trackingPlayer, ServerPlayer trackedPlayer) {
        return trackingPlayer != trackedPlayer && trackingPlayer.getTeam() == trackedPlayer.getTeam();
    }

    private static ItemStack getWormholePotion(ServerPlayer serverPlayer) {
        Inventory inventory = serverPlayer.getInventory();
        ItemStack stack = inventory.offhand.get(0);
        if (!stack.isEmpty() && stack.is(MapWormhole.WORMHOLE_POTION.get())) {
            return stack;
        } else {
            for (ItemStack itemStack : inventory.items) {
                if (!itemStack.isEmpty() && itemStack.is(MapWormhole.WORMHOLE_POTION.get())) {
                    return itemStack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    private void teleport(ServerPlayer serverPlayer, ServerPlayer target) {
        serverPlayer.teleportTo(serverPlayer.serverLevel(), target.getX(), target.getY(), target.getZ(), serverPlayer.getXRot(), serverPlayer.getYRot());
    }

    public static void sendToServer(UUID playerId) {
        CHANNEL.sendToServer(new WormholeToPlayerPacketC2S(playerId));
    }
}
