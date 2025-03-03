package dev.siea.catbase.handlers;

import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import dev.siea.catbase.hdi.RateLimitedHandler;

import java.util.Base64;

@RouteInfo(endpoint = "/api/asset/:id", method = HttpMethod.GET)
public class AssetServingHandler extends RateLimitedHandler {
    public AssetServingHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected Object handleRateLimited() {
        int id = Integer.parseInt(req.getRouteParam("id"));
        // then should have a helper method from db to get an image from it's id blah blah
        byte[] imageBytes = decodeBase64ToBytes("STUFF RETURNED FROM DB : - )"); //swap for db later one if using base64
        String contentType = "img/png"; //harcode for now until db design
        res.status(200).type(contentType).body(imageBytes);
        return res.getBody();
    }

    // ;3
    // silly..
    private static byte[] decodeBase64ToBytes(String base64Image) {
        return Base64.getDecoder().decode(base64Image);
    }
}
