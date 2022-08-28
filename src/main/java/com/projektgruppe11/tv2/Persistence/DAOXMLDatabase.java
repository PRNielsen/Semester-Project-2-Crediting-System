package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Production;
import org.postgresql.Driver;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DAOXMLDatabase {

    private Connection connection;


    public DAOXMLDatabase() {
        connect();
    }

    public void connect() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gruppe_11", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void saveProductions(Production... productions) {
        try {
            for (Production production : productions) {
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO daoxml (ext_id, uuid, description, name, episode_number, productionyear, producedby, extendedcast) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                insertStatement.setString(1, production.getExt_id());
                insertStatement.setString(2, String.valueOf(production.getUuid()));
                insertStatement.setString(3, production.getDescription());
                insertStatement.setString(4, production.getName());
                insertStatement.setString(5, production.getEpisodeNumber());
                insertStatement.setString(6, production.getProductionYear());
                insertStatement.setString(7, production.getProducedBy());
                insertStatement.setString(8, production.getCast());
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Production get(String ext_id) {
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM daoxml WHERE ext_id = ?");
            queryStatement.setString(1, ext_id);
            ResultSet resultSet = queryStatement.executeQuery();
            return new Production(resultSet.getString("ext_id"), UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("description"), resultSet.getString("name"),
                    resultSet.getString("episodeNumber"), resultSet.getString("productionYear"),
                    resultSet.getString("producedBy"), resultSet.getString("extendedCast"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Production production) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE daoxml SET name = ?, uuid = ?, ext_id = ?, description = ?, episode_number = ?, productionyear = ?, producedby = ?, extendedcast = ? WHERE id = ?");
            updateStatement.setString(1, production.getName());
            updateStatement.setString(2, production.getUuid().toString());
            updateStatement.setString(3, String.valueOf(production.getExt_id()));
            updateStatement.setString(4, production.getDescription());
            updateStatement.setString(5, production.getEpisodeNumber());
            updateStatement.setString(6, production.getProductionYear());
            updateStatement.setString(7, production.getProducedBy());
            updateStatement.setString(8, production.getCast());
            updateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String ext_id) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM daoxml WHERE ext_id = ?");
            deleteStatement.setString(1, ext_id);
            deleteStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Production> getProductions() {
        Map<String, Production> productions = new LinkedHashMap<>();
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM daoxml");
            ResultSet resultSet = queryStatement.executeQuery();
            while (resultSet.next()) {
                productions.put(resultSet.getString("ext_id"), new Production(resultSet.getString("ext_id"),
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getString("description"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productions;
    }

}
