package dev.siea.catbase.db;

import dev.siea.catbase.db.models.User;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseAdapter {
    void connect() throws SQLException;
    void createTables() throws SQLException;
    User getUser(int id);
    User getUser(String authToken);
    List<User> getUsers(int limit, int offset, String orderBy, boolean ascending);
    User createUser(String email, String username, String password);
    boolean deleteUser(int id);
    User updateUser(User user);
    User login(User user);
    void executeQuery(String query) throws SQLException;
}

