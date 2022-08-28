package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.User;
import org.postgresql.Driver;

import java.sql.*;
import java.util.UUID;

public class UserDatabaseRepository implements UserRepository {

    private Connection connection;
    private static UserDatabaseRepository instance = null;

    public static UserDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new UserDatabaseRepository();
        }

        return instance;
    }

    public UserDatabaseRepository() {
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

    @Override
    public void saveUsers(User... users) {
        try {
            for (User user : users) {
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO users (id, username, password, role) VALUES(?, ?, ?, ?)");
                insertStatement.setString(1, user.getUuid().toString());
                insertStatement.setString(2, user.getUsername());
                insertStatement.setString(3, user.getPassword());
                insertStatement.setString(4, user.getRole().toString());
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User get(UUID uuid) {
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            queryStatement.setString(1, uuid.toString());
            ResultSet resultSet = queryStatement.executeQuery();
            UUID id = UUID.fromString(resultSet.getString("id"));
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            User.Role role = User.Role.valueOf(resultSet.getString("role"));
            return new User(id, username, password, role);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(User user) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET id = ?, username = ?, password = ?, role = ? WHERE id = ?");
            updateStatement.setString(1, user.getUuid().toString());
            updateStatement.setString(2, user.getUsername());
            updateStatement.setString(3, user.getPassword());
            updateStatement.setString(4, user.getRole().toString());
            updateStatement.setString(5, user.getUuid().toString());
            updateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            deleteStatement.setString(1, uuid.toString());
            deleteStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User authenticate(String username, String password) {
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            queryStatement.setString(1, username);
            queryStatement.setString(2, password);
            ResultSet resultSet = queryStatement.executeQuery();
            if (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                User.Role role = User.Role.valueOf(resultSet.getString("role"));
                return new User(id, username, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
