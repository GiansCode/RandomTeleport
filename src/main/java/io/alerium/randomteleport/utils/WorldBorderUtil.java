package io.alerium.randomteleport.utils;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;

public final class WorldBorderUtil {

    private WorldBorderUtil() {
    }

    /**
     * This method gets the world border data
     * @param world The World name
     * @return An int array, the first value is the max X coordinate, and the second value is the max Z coordinate
     */
    public static int[] getWorldRanges(String world) {
        BorderData data = Config.Border(world);
        return new int[] {
                data.getRadiusX(),
                data.getRadiusZ()
        };
    }
    
}
