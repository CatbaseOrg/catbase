package dev.siea.catbase.hdi;

import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;

public abstract class APIKeyProtectedHandler extends RequestHandler {
    protected String apiKey;
    public APIKeyProtectedHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public Object handle() {
        String workingApiKey = req.header("x-api-key");
        if (workingApiKey == null || workingApiKey.isEmpty()) {
            res.status(401);
            return "{\"error\":\"Missing or invalid X-API-Key header\"}";
        }

        this.apiKey = workingApiKey;
        return handleProtected();
    }

    protected abstract Object handleProtected();
}
