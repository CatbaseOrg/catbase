package dev.siea.catbase.db.adapters;

import dev.siea.catbase.db.DBUtils;
import dev.siea.catbase.db.DatabaseAdapter;
import dev.siea.catbase.db.models.User;
import org.simpleyaml.configuration.ConfigurationSection;

import java.sql.*;

public class PostgreSQLAdapter implements DatabaseAdapter {
    private final String dbUrl;
    private final String username;
    private final String password;
    private Connection connection;  // Persistent connection object

    // Constructor to receive necessary DB configurations
    public PostgreSQLAdapter(ConfigurationSection databaseSection) {
        String host = databaseSection.getString("host");
        String dbName = databaseSection.getString("database");
        this.dbUrl = "jdbc:postgresql://" + host + "/" + dbName;  // Construct the DB URL
        this.username = databaseSection.getString("credentials.username");
        this.password = databaseSection.getString("credentials.password");
    }

    // Connect to the PostgreSQL database and persist the connection
    @Override
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Connected to PostgreSQL database");
        }
    }

    // Create the necessary tables in the database
    @Override
    public void createTables() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "email VARCHAR(255) NOT NULL UNIQUE, " +  // Ensure email is unique
                "username VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "api_key VARCHAR(255), " +
                "last_login TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "role VARCHAR(255) NOT NULL DEFAULT 'NORMAL', " +
                "CONSTRAINT unique_email UNIQUE(email)" +  // Enforce unique email constraint
                ")";
        executeQuery(createTableSQL);
    }

    // Create a new user, ensuring that the email is unique
    @Override
    public User createUser(String email, String username, String password) {
        // Check if the email already exists
        if (userExistsByEmail(email)) {
            System.out.println("Email already exists: " + email);
            return null;  // Return null or throw an exception
        }

        String query = "INSERT INTO users (email, username, password, api_key) VALUES (?, ?, ?, ?) RETURNING *";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, DBUtils.generateAPIKey(32));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Check if a user exists by email
    private boolean userExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a user by ID, return true if user existed and was deleted
    @Override
    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;  // Return true if the user was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false if an error occurred
        }
    }

    @Override
    public User updateUser(User user) {
        String query = "UPDATE users SET email = ?, username = ?, password = ?, api_key = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getApiKey());
            statement.setString(5, user.getRole().name());
            statement.setInt(6, user.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return user;
            }
            return null;  // If no rows were updated
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get user by ID (direct query)
    @Override
    public User getUser(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);  // Set the ID directly as an integer
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get user by API key (direct query)
    @Override
    public User getUser(String apiKey) {
        String query = "SELECT * FROM users WHERE api_key = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, apiKey);  // Set the API key directly as a string
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to map ResultSet to User object
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        Timestamp lastLoginTimestamp = resultSet.getTimestamp("last_login");
        long lastLoginMillis = lastLoginTimestamp != null ? lastLoginTimestamp.getTime() : 0;
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("api_key"),
                lastLoginMillis,
                User.Role.valueOf(resultSet.getString("role"))
        );
    }

    // Not yet implemented
    @Override
    public User login(User user) {
        return null;
    }

    // Execute any given SQL query
    @Override
    public void executeQuery(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }
}
