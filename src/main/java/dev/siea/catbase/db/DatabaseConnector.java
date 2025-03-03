package dev.siea.catbase.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.simpleyaml.configuration.ConfigurationSection;


import java.sql.Connection;
import java.sql.SQLException;

class DatabaseConnector {
    private final HikariDataSource dataSource;

    DatabaseConnector(ConfigurationSection databaseSection) {
        String dbType = databaseSection.getString("type");
        String dbName = databaseSection.getString("name");
        String host = databaseSection.getString("host");
        String dbUrl = buildDatabaseUrl(dbType, host, dbName);
        HikariConfig config = new HikariConfig();
        switch (dbType.toLowerCase()) {
            case "mysql":
                config.setJdbcUrl(dbUrl);
                config.setUsername(databaseSection.getString("username"));
                config.setPassword(databaseSection.getString("password"));
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                break;
            case "sqlite":
                config.setJdbcUrl(dbUrl);
                config.setDriverClassName("org.sqlite.JDBC");
                break;
            case "postgresql":
                config.setJdbcUrl(dbUrl);
                config.setUsername(databaseSection.getString("credentials.username"));
                config.setPassword(databaseSection.getString("credentials.password"));
                config.setDriverClassName("org.postgresql.Driver");
                break;
            case "h2":
                config.setJdbcUrl(dbUrl);
                config.setDriverClassName("org.h2.Driver");
                break;
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
        this.dataSource = new HikariDataSource(config);
    }

    Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private String buildDatabaseUrl(String dbType, String host, String dbName) {
        if (dbType == null || host == null || dbName == null) {
            throw new IllegalArgumentException("Database type, host, and name must be specified");
        }
        return switch (dbType.toLowerCase()) {
            case "mysql" -> "jdbc:mysql://" + host + "/" + dbName;
            case "sqlite" -> "jdbc:sqlite:" + dbName;
            case "postgresql" -> "jdbc:postgresql://" + host + "/" + dbName;
            case "h2" -> "jdbc:h2:" + host + "/" + dbName;
            default -> throw new IllegalArgumentException("Unsupported database type: " + dbType);
        };
    }
}