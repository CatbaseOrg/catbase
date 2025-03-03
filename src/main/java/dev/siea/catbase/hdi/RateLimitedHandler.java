package dev.siea.catbase.hdi;


import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import dev.siea.catbase.db.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2nd Level HDI that ensures rate limiting for handlers that require it.
 * Inherits from APIKeyAuthHandler to provide API key authentication and the cached user instance.
 */
public abstract class RateLimitedHandler extends APIKeyAuthHandler {
    private static final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private static final Map<String, Long> requestTimeStamps = new ConcurrentHashMap<>();

    public RateLimitedHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    protected final Object handleAuthenticated() {
        String ip = req.clientAddress().getAddress().getHostAddress();
        String identifier = userRole.equals(User.Role.GUEST) ? ip : apiKey;

        int maxRequests = userRole.getMaxRequests();
        long timeWindowMillis = userRole.getTimeWindowMillis();

        long currentTime = System.currentTimeMillis();
        int currentCount = requestCounts.getOrDefault(identifier, 0);
        long lastReset = requestTimeStamps.getOrDefault(identifier, currentTime);

        if (currentTime - lastReset > timeWindowMillis) {
            requestCounts.put(identifier, 1);
            requestTimeStamps.put(identifier, currentTime);
        } else {
            if (currentCount >= maxRequests) {
                res.status(429);
                res.type("application/json");
                return "{\"error\":\"Rate limit exceeded\"}";
            }
            requestCounts.put(identifier, currentCount + 1);
        }

        return handleRateLimited();
    }

    protected abstract Object handleRateLimited();
}
