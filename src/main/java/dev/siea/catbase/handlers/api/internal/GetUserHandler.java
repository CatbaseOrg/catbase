package dev.siea.catbase.handlers.api.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import dev.siea.catbase.Catbase;
import dev.siea.catbase.db.models.User;
import dev.siea.catbase.hdi.InternalHandler;

@RouteInfo(endpoint = "/user/:id", method = HttpMethod.GET)
public class GetUserHandler extends InternalHandler {
    ObjectMapper mapper = new ObjectMapper();

    public GetUserHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected final Object handleInternalUsage() {
        res.type("application/json");
        int id = Integer.parseInt(req.getRouteParam("id"));
        User user = Catbase.getUserManager().getUser(id);
        try {
            return mapper.writeValueAsString(user);
        } catch (Exception e) {
            res.status(500);
            return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
        }
    }
}
