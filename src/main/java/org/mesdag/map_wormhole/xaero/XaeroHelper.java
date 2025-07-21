package org.mesdag.map_wormhole.xaero;

import net.minecraft.client.player.RemotePlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mesdag.map_wormhole.WormholeToPlayerPacketC2S;
import xaero.map.radar.tracker.PlayerTrackerMapElement;

import java.util.UUID;

public class XaeroHelper {
    public static boolean teleport(Object o) {
        UUID uuid = null;
        if (o instanceof RemotePlayer player) {
            uuid = player.getUUID();
        } else if (o instanceof PlayerTrackerMapElement<?> element) {
            uuid = element.getPlayerId();
        }
        if (uuid != null) {
            PacketDistributor.sendToServer(new WormholeToPlayerPacketC2S(uuid));
            return true;
        }
        return false;
    }
}
