package org.mesdag.map_wormhole.xaero;

import net.minecraft.client.player.RemotePlayer;
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
            WormholeToPlayerPacketC2S.sendToServer(uuid);
            return true;
        }
        return false;
    }
}
