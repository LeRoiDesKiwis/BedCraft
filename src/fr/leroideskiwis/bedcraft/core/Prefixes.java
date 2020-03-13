package fr.leroideskiwis.bedcraft.core;

public enum Prefixes {

    BEDCRAFT("§6[§7BedCraft§6]");

    private String string;

    Prefixes(String s) {
        this.string = s;
    }

    public String toString(){
        return string+"§7";
    }
}
