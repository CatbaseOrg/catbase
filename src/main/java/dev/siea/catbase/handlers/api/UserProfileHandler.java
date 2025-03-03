package dev.siea.catbase.handlers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import dev.siea.catbase.Catbase;
import dev.siea.catbase.components.UserManager;
import dev.siea.catbase.db.models.User;
import dev.siea.catbase.hdi.RateLimitedHandler;

@RouteInfo(endpoint = "/profile", method = HttpMethod.GET)
public class UserProfileHandler extends RateLimitedHandler {
    ObjectMapper mapper = new ObjectMapper();

    UserManager userManager = Catbase.getUserManager();
    public UserProfileHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected Object handleRateLimited() {
        if(userRole.equals(User.Role.GUEST)){
            return "{\"error\":\"You must be logged in to view your profile\"}";
        }

        try {
            return mapper.writeValueAsString(user);
        } catch (Exception e) {
            res.status(500);
            return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
        }
    }
}
