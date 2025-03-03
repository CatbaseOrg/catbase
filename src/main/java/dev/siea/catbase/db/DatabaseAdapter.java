package dev.siea.catbase.db;

import dev.siea.catbase.db.models.User;

import java.sql.SQLException;

public interface DatabaseAdapter {
    void connect() throws SQLException;
    void createTables() throws SQLException;
    User getUser(int id);
    User getUser(String authToken);
    User createUser(String email, String username, String password);
    boolean deleteUser(int id);
    User updateUser(User user);
    User login(User user);
    void executeQuery(String query) throws SQLException;
}

