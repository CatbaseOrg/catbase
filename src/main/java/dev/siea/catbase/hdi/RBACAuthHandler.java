package dev.siea.catbase.hdi;

import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import dev.siea.catbase.Catbase;
import dev.siea.catbase.components.UserManager;
import dev.siea.catbase.db.DatabaseWrapper;
import dev.siea.catbase.db.models.User;

public abstract class RBACAuthHandler extends RequestHandler {
    private final UserManager userManager = Catbase.getUserManager();
    protected String apiKey;
    protected User.Role userRole;
    protected User user;

    public RBACAuthHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public final Object handle() {
        this.apiKey = req.header("X-Api-Key");
        this.apiKey = apiKey;
        if(this.apiKey == null || apiKey.isEmpty()){
            this.userRole = User.Role.GUEST;
        } else {
            this.user = userManager.getUser(apiKey);
            if(user == null){
                res.status(401);
                res.type("application/json");
                return "{\"error\":\"Invalid API key\"}";
            } else {
                this.userRole = user.getRole();
            }
        }

        return handleRBAC();
    }

    protected abstract Object handleRBAC();
}
