package fr.leroideskiwis.bedcraft.sql;

import fr.leroideskiwis.bedcraft.core.BedCraft;
import org.bukkit.entity.Player;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SQLManager implements Closeable {
    private Connection connection;
    private Statement statement;

    public void connect(ConnectionData connectionData) throws SQLException {

        if(connection == null){
            connection = DriverManager.getConnection("jdbc:mysql://"+connectionData.host+"/"+connectionData.database, connectionData.user, connectionData.password);
            statement = connection.createStatement();
        }

    }

    public boolean exists(UUID uuid) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?");
        preparedStatement.setString(1, uuid.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isNew = resultSet.next();
        resultSet.close();
        preparedStatement.close();
        return isNew;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public boolean hasNext(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean hasNext = resultSet.next();
        resultSet.close();
        return hasNext;
    }

    public void close(){
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
