package dev.siea.catbase.hdi;

import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import dev.siea.catbase.db.DatabaseWrapper;
import dev.siea.catbase.db.models.User;
import dev.siea.catbase.db.models.UserRole;

public abstract class RBACAuthHandler extends RequestHandler {
    protected String apiKey;
    protected UserRole userRole;
    protected User user;

    public RBACAuthHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public final Object handle() {
        this.apiKey = req.header("X-Api-Key");
        this.apiKey = apiKey;
        if(this.apiKey == null || apiKey.isEmpty()){
            this.userRole = UserRole.GUEST;
        } else {
            this.user = DatabaseWrapper.getUser(apiKey);
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
