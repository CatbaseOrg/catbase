package dev.siea.catbase.db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    private final int id;
    private final String email;
    private String username;
    private String passwordHash;
    private String apiKey;
    private Long lastLogin;
    private Role role;

    public User(int id, String email, String username, String passwordHash, String apiKey, Long lastLogin, Role role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.apiKey = apiKey;
        this.lastLogin = lastLogin;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonIgnore
    public String getApiKey() {
        return apiKey;
    }

    public Long getLastLogin() {
        return lastLogin;
    }

    public Role getRole() {
        return role;
    }

    public enum Role {
        GUEST(1000, 60_000),
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
