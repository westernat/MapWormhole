package org.mesdag.map_wormhole.ftbchunks;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mesdag.map_wormhole.WormholeToPlayerPacketC2S;

import java.util.UUID;

public class FTBChunksHelper {
    @OnlyIn(Dist.CLIENT)
    public static void onMouseClicked(Object screen, Object button, UUID playerId) {
        if (Minecraft.getInstance().player == null) return;
        if (!playerId.equals(Minecraft.getInstance().player.getGameProfile().getId())) {
            if (((MouseButton) button).isLeft()) {
                PacketDistributor.sendToServer(new WormholeToPlayerPacketC2S(playerId));
                ((BaseScreen) screen).closeGui(false);
            }
        }
    }
}
