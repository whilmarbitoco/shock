package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.session.SessionManager;
import org.whilmarbitoco.exception.InternalServerException;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF protection middleware.
 * Validates CSRF token on state-changing methods (POST, PUT, DELETE).
 * Token is stored in the session and must be submitted as a form field or header.
 */
public class CsrfMiddleware implements Middleware {

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder();

    /**
     * Generate a new CSRF token and store it in the session.
     *
     * @param sessionID the session ID
     * @return the generated token
     */
    public static String generateToken(String sessionID) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = encoder.encodeToString(bytes);
        SessionManager.session(sessionID).put("_csrf", token);
        return token;
    }

    /**
     * Retrieve the CSRF token for a session.
     *
     * @param sessionID the session ID
     * @return the token, or null if not set
     */
    public static String getToken(String sessionID) {
        return SessionManager.session(sessionID).get("_csrf");
    }

    @Override
    public void handle(Request request, Response response) {
        String method = request.getMethod();

        if (!"POST".equals(method) && !"PUT".equals(method) && !"DELETE".equals(method)) {
            return;
        }

        String sessionID = request.getShockSession();
        if (sessionID == null) {
            throw new InternalServerException("CSRF validation failed: no session");
        }

        String sessionToken = getToken(sessionID);
        if (sessionToken == null) {
            throw new InternalServerException("CSRF validation failed: no token in session");
        }

        // Check form body first, then header
        String requestToken = (String) request.getBody("_csrf");
        if (requestToken == null) {
            requestToken = request.getHeader("X-CSRF-Token");
        }

        if (requestToken == null || !requestToken.equals(sessionToken)) {
            throw new InternalServerException("CSRF validation failed: invalid token");
        }
    }
}
