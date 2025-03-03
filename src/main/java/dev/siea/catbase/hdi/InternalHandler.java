package dev.siea.catbase.hdi;

import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import dev.siea.catbase.Catbase;

public class InternalHandler extends APIKeyProtectedHandler {
    private final String INTERNAL_API_KEY = Catbase.getConfig().getString("INTERNAL_API_KEY");
    public InternalHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected final Object handleProtected() {
        if (!apiKey.equals(INTERNAL_API_KEY)) {
            res.status(401);
            return "{\"error\":\"Unauthorized.\"}";
        }
        return handleInternalUsage();
    }

    protected Object handleInternalUsage() {
        return null;
    }

}
