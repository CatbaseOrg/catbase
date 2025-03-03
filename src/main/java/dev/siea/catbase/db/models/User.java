package dev.siea.catbase.db.models;

public class User {
    private final int id;
    private String username;
    private String passwordHash;
    private Role role;

    public User(int id, String username, String passwordHash, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = Role.valueOf(role.toUpperCase());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        GUEST(100, 60_000),
        NORMAL(1000, 60_000),
        PREMIUM(5000, 60_000),
        ADMIN(Integer.MAX_VALUE, 60_000);

        private final int maxRequests;
        private final long timeWindowMillis;

        Role(int maxRequests, long timeWindowMillis) {
            this.maxRequests = maxRequests;
            this.timeWindowMillis = timeWindowMillis;
        }

        public int getMaxRequests() {
            return maxRequests;
        }

        public long getTimeWindowMillis() {
            return timeWindowMillis;
        }
    }
}
