package fr.leroideskiwis.bedcraft.utils;

import org.bukkit.Location;

public class Interval {

    private final int min;
    private final int max;

    public Interval(int min, int max){
        this.min = min;
        this.max = max;
    }

    public boolean isIn(int number){
        return number > min && number < max;
    }

    public static boolean isInBorder(int borderX, int borderX2, int borderZ, int borderZ2, Location location){
        int realBorderX = Math.min(borderX, borderX2);
        int realBorderX2 = Math.max(borderX, borderX2);

        int realBorderZ = Math.min(borderZ, borderZ2);
        int realBorderZ2 = Math.max(borderZ, borderZ2);

        return new Interval(realBorderX, realBorderX2).isIn(location.getBlockX()) && new Interval(realBorderZ, realBorderZ2).isIn(location.getBlockZ());
    }

}
