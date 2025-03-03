package dev.siea.catbase.hdi;

import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import dev.siea.catbase.Catbase;
import dev.siea.catbase.components.UserManager;
import dev.siea.catbase.db.models.User;

/**
 * Bottom-level HDI that provides API key authentication for handlers that require it, and
 * provides the cached user and role for the authenticated user.
 */
public abstract class APIKeyAuthHandler extends RequestHandler {
    protected String apiKey;
    protected User user;
    protected User.Role userRole;
    protected final UserManager userManager = Catbase.getUserManager();

    public APIKeyAuthHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public Object handle() {
        this.apiKey = req.header("x-api-key");

        if (apiKey == null || apiKey.isEmpty()) {
            this.userRole = User.Role.GUEST;
        } else {
            this.user = userManager.getUser(apiKey);
            if (user == null) {
                res.status(401);
                return "{\"error\":\"Invalid API key\"}";
            }
            this.userRole = user.getRole();
        }

        return handleAuthenticated();
    }

    protected abstract Object handleAuthenticated();
}

