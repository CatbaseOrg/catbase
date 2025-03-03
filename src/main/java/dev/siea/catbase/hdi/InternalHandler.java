package dev.siea.catbase.hdi;

import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import dev.siea.catbase.Catbase;

/**
 * Bottom-level HDI that provides API key authentication handlers intended for internal use, and
 * provides the apiKey used for authentication.
 */
public class InternalHandler extends RequestHandler {
    protected String apiKey;
    private final String INTERNAL_API_KEY = Catbase.getConfig().getString("INTERNAL_API_KEY");
    public InternalHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public final Object handle() {
        this.apiKey = req.header("x-api-key");

        if (this.apiKey == null || this.apiKey.isEmpty()) {
            res.status(401);
            res.type("application/json");
            return "{\"error\":\"Missing or invalid API key\"}";
        }
        if (!this.apiKey.equals(INTERNAL_API_KEY)) {
            res.status(401);
            res.type("application/json");
            return "{\"error\":\"Unauthorized.\"}";
        }
        return handleInternalUsage();
    }

    protected Object handleInternalUsage() {
        return null;
    }

}
