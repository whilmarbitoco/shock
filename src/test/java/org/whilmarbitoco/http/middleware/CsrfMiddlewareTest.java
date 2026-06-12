package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.exception.InternalServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CsrfMiddleware.
 */
class CsrfMiddlewareTest {

    private CsrfMiddleware middleware;

    @BeforeEach
    void setUp() {
        middleware = new CsrfMiddleware();
    }

    @Test
    @DisplayName("GET requests are allowed without token")
    void getAllowedWithoutToken() {
        Request request = new Request("GET", "/test");
        request.setShockSession("session1");
        Response response = new Response();
        assertDoesNotThrow(() -> middleware.handle(request, response));
    }

    @Test
    @DisplayName("POST without session throws exception")
    void postWithoutSessionThrows() {
        Request request = new Request("POST", "/test");
        Response response = new Response();
        assertThrows(InternalServerException.class, () -> middleware.handle(request, response));
    }

    @Test
    @DisplayName("POST with session but no token throws exception")
    void postWithSessionNoTokenThrows() {
        Request request = new Request("POST", "/test");
        request.setShockSession("session1");
        Response response = new Response();
        assertThrows(InternalServerException.class, () -> middleware.handle(request, response));
    }

    @Test
    @DisplayName("POST with valid CSRF token passes")
    void postWithValidTokenPasses() {
        String sessionId = "session_" + System.nanoTime();
        String token = CsrfMiddleware.generateToken(sessionId);

        Request request = new Request("POST", "/test");
        request.setShockSession(sessionId);
        request.setBody(new String[]{"_csrf=" + token});
        Response response = new Response();
        assertDoesNotThrow(() -> middleware.handle(request, response));
    }

    @Test
    @DisplayName("POST with invalid CSRF token throws exception")
    void postWithInvalidTokenThrows() {
        String sessionId = "session_" + System.nanoTime();
        CsrfMiddleware.generateToken(sessionId);

        Request request = new Request("POST", "/test");
        request.setShockSession(sessionId);
        request.setBody(new String[]{"_csrf=wrong_token"});
        Response response = new Response();
        assertThrows(InternalServerException.class, () -> middleware.handle(request, response));
    }

    @Test
    @DisplayName("generateToken produces non-null token")
    void generateTokenNonNull() {
        String token = CsrfMiddleware.generateToken("test_session");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("getToken returns the token set by generateToken")
    void getTokenReturnsGenerated() {
        String sessionId = "session_" + System.nanoTime();
        String generated = CsrfMiddleware.generateToken(sessionId);
        assertEquals(generated, CsrfMiddleware.getToken(sessionId));
    }

    @Test
    @DisplayName("getToken returns null for unknown session")
    void getTokenNullForUnknown() {
        assertNull(CsrfMiddleware.getToken("nonexistent_session"));
    }

    @Test
    @DisplayName("PUT and DELETE also require CSRF token")
    void putAndDeleteRequireToken() {
        String sessionId = "session_" + System.nanoTime();
        CsrfMiddleware.generateToken(sessionId);

        Request putReq = new Request("PUT", "/test");
        putReq.setShockSession(sessionId);
        Response putRes = new Response();
        assertThrows(InternalServerException.class, () -> middleware.handle(putReq, putRes));

        Request delReq = new Request("DELETE", "/test");
        delReq.setShockSession(sessionId);
        Response delRes = new Response();
        assertThrows(InternalServerException.class, () -> middleware.handle(delReq, delRes));
    }
}
