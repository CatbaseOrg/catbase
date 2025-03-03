package dev.siea.catbase.handlers;

import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import dev.siea.catbase.hdi.RateLimitedHandler;

@RouteInfo(endpoint = "/api/asset/:id", method = HttpMethod.GET)
public class AssetServingHandler extends RateLimitedHandler {
    public AssetServingHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected Object handleRateLimited() {
        return "this shit is rate limited i think, now implementing db -> res";
    }

    private static byte[] decodeBse64ToBytes(String base64Image) {
        return
    }
}
