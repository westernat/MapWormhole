package org.mesdag.map_wormhole.xaero;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import xaero.map.server.radar.tracker.ISyncedPlayerTrackerSystem;

public class WormholeSyncedPlayerTrackerSystem implements ISyncedPlayerTrackerSystem {
    @Override
    public int getTrackingLevel(Player tracker, Player tracked) {
        Team trackerTeam = tracker.getTeam();
        Team trackedTeam = tracked.getTeam();
        if (trackerTeam == trackedTeam) return 2;
        if (trackerTeam == null || trackedTeam == null) return 0;
        return 1;
    }

    @Override
    public boolean isPartySystem() {
        return true;
    }
}
