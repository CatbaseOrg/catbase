package dev.siea.catbase;

import com.pixelservices.config.YamlConfig;
import com.pixelservices.flash.components.FlashServer;
import com.pixelservices.flash.components.fileserver.DynamicFileServerConfiguration;
import com.pixelservices.flash.components.fileserver.SourceType;
import dev.siea.catbase.components.UserManager;
import dev.siea.catbase.db.DatabaseWrapper;
import dev.siea.catbase.handlers.api.UserProfileHandler;
import dev.siea.catbase.handlers.api.internal.GetUsersHandler;

public class Catbase {
    private static final YamlConfig config = new YamlConfig("config.yml");
    private static final UserManager userManager = new UserManager(100, 60_000);
    public Catbase() {
        config.save();
        DatabaseWrapper.connect(config.getConfigurationSection("database"));

        FlashServer server = new FlashServer(config.getInt("port"));

        server.enableCORS(
                "*",
                "GET, POST, PUT, DELETE, OPTIONS",
                "Authorization, Content-Type"
        );

        server.serveDynamic("/", new DynamicFileServerConfiguration(
                true,
                "frontend",
                "index.html",
                SourceType.RESOURCESTREAM
        ));

        // TESTING ONLY
        server.route("/api")
               .register(UserProfileHandler.class)
               .register(GetUsersHandler.class);

        server.start();
    }

    public static void main(String[] args) {
        new Catbase();
    }

    public static YamlConfig getConfig() {
        return config;
    }

    public static UserManager getUserManager() {
        return userManager;
    }
}
