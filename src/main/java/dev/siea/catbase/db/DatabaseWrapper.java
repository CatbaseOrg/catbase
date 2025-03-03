package dev.siea.catbase.db;

import dev.siea.catbase.db.models.User;
import dev.siea.catbase.dto.UserLoginRequest;
import org.simpleyaml.configuration.ConfigurationSection;

import java.sql.SQLException;

public class DatabaseWrapper {
    private static DatabaseConnector connector;

    public static void connect(ConfigurationSection databaseSection) {
        try {
            connector = new DatabaseConnector(databaseSection);
        } catch (Exception e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }

        try {
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database tables", e);
        }
    }

    private static void createTables() throws SQLException {
        try (var connection = connector.getConnection()) {
            var statement = connection.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL)");
        }
    }

    public static User getUser(int id) {
        //later
        return new User(0, "admin", "password", "ADMIN");
    }

    public static User login(UserLoginRequest request) {
        //later
        return new User(0, "admin", "password", "ADMIN");
    }

    public static User getUser(String authToken) {
        //later
        return new User(0, "admin", "password", "ADMIN");
    }
}
