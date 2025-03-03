package dev.siea.catbase.db;

import dev.siea.catbase.db.adapters.PostgreSQLAdapter;
import dev.siea.catbase.db.models.User;
import org.simpleyaml.configuration.ConfigurationSection;

import java.sql.SQLException;

public class DatabaseWrapper {
    private static DatabaseAdapter adapter;

    public static void connect(ConfigurationSection databaseSection) {
        try {
            // Select the correct adapter based on the configuration
            String dbType = databaseSection.getString("type");
            switch (dbType.toLowerCase()) {
                case "postgresql":
                    adapter = new PostgreSQLAdapter(databaseSection);
                    break;
                // others
                default:
                    throw new IllegalArgumentException("Unsupported database type: " + dbType);
            }

            adapter.connect();
            adapter.createTables();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection or create tables", e);
        }
    }

    public static User getUser(int id) {
        return adapter.getUser(id);
    }

    public static User getUser(String authToken) {
        return adapter.getUser(authToken);
    }

    public static User createUser(String email, String username, String password) {
        return adapter.createUser(email, username, password);
    }

    public static boolean deleteUser(int id) {
        return adapter.deleteUser(id);
    }

    public static User updateUser(User user) {
        return adapter.updateUser(user);
    }

    public static User login(User user) {
        return adapter.login(user);
    }
}
