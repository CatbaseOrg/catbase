package dev.siea.catbase.handlers.api.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixelservices.flash.components.ExpectedRequestParameter;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import dev.siea.catbase.Catbase;
import dev.siea.catbase.db.models.User;
import dev.siea.catbase.hdi.InternalHandler;

import java.util.List;

@RouteInfo(endpoint = "/users", method = HttpMethod.GET)
public class GetAllUsersHandler extends InternalHandler {
    ObjectMapper mapper = new ObjectMapper();

    private final ExpectedRequestParameter limitField =
            expectedRequestParameter("limit", "The number of users to return");

    private final ExpectedRequestParameter offsetField =
            expectedRequestParameter("offset", "The number of users to skip");

    private final ExpectedRequestParameter orderByField =
            expectedRequestParameter("orderBy", "The field to order by");

    private final ExpectedRequestParameter ascendingField =
            expectedRequestParameter("ascending", "Whether to sort in ascending order");

    public GetAllUsersHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected Object handleInternalUsage() {
        List<User> users = Catbase.getUserManager().getUsers(
                limitField.getInt(),
                offsetField.getInt(),
                orderByField.getString(),
                ascendingField.getBoolean()
        );

        try {
            res.type("application/json");
            return mapper.writeValueAsString(users);
        } catch (Exception e) {
            res.status(500);
            return "{\"error\":\"Internal server error: " + e.getMessage() + "\"}";
        }

    }
}
