package dev.siea.catbase.components;

import dev.siea.catbase.db.DatabaseWrapper;
import dev.siea.catbase.db.models.User;

import java.util.List;

public class UserManager {
    private final Cache<User> userCache;

    public UserManager(int maxCacheSize, long retentionPeriodMillis) {
        this.userCache = new Cache<>(maxCacheSize, retentionPeriodMillis);
    }

    public User getUser(int id) {
        String key = String.valueOf(id);
        User cachedUser = userCache.get(key);
        if (cachedUser != null) {
            return cachedUser;
        }

        User userFromDb = DatabaseWrapper.getUser(id);
        if (userFromDb != null) {
            userCache.put(key, userFromDb);
        }
        return userFromDb;
    }

    public User getUser(String apiKey) {
        User cachedUser = userCache.get(apiKey);
        if (cachedUser != null) {
            return cachedUser;
        }

        User userFromDb = DatabaseWrapper.getUser(apiKey);
        if (userFromDb != null) {
            userCache.put(apiKey, userFromDb);
        }
        return userFromDb;
    }

    public List<User> getUsers(int limit, int offset, String orderBy, boolean ascending) {
        return DatabaseWrapper.getUsers(limit, offset, orderBy, ascending);
    }

    public void updateUser(User user) {
        DatabaseWrapper.updateUser(user);

        // Update the cache to reflect the change
        String key = String.valueOf(user.getId());
        userCache.put(key, user);
    }

    public void deleteUser(int id) {
        boolean deleted = DatabaseWrapper.deleteUser(id);

        if (deleted) {
            String key = String.valueOf(id);
            userCache.put(key, null);
        }
    }
}

