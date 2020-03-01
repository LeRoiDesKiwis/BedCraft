package fr.leroideskiwis.bedcraft.utils;

public class Corners {

    public final int cornerXmin;
    public final int cornerXmax;

    public final int cornerZmin;
    public final int cornerZmax;

    public Corners(int cornerX, int cornerX1, int cornerZ, int cornerZ1) {
        this.cornerXmin = Math.min(cornerX, cornerX1);
        this.cornerXmax = Math.max(cornerX, cornerX1);
        this.cornerZmin = Math.min(cornerZ, cornerZ1);
        this.cornerZmax = Math.max(cornerZ, cornerZ1);
    }
}
