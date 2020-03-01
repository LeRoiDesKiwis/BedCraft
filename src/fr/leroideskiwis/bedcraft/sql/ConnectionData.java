package fr.leroideskiwis.bedcraft.sql;

public class ConnectionData {

    public final String host;
    public final String user;
    public final String password;
    public final String database;
    public final int port;

    public ConnectionData(String host, int port, String database, String user, String password){
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        this.port = port;
    }

}
