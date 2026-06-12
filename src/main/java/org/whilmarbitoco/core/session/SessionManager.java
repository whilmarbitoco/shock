package org.whilmarbitoco.core.session;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory session and flash storage.
 *
 * NOTE: Sessions are stored in static HashMaps and are lost on server restart.
 * They are also not shared across multiple server instances.
 * For production use, replace this with a persistent store (Redis, database, etc.).
 */
public class SessionManager {
    private static final Map<String, Map<String, String>> sessions = new HashMap<>();
    private static final Map<String, Map<String, String>> flash = new HashMap<>();


    public static Map<String, String> session(String sessionID) {
        return sessions.computeIfAbsent(sessionID, e -> new HashMap<>());
    }

    public static Map<String, String> flash(String sessionID) {
        return flash.computeIfAbsent(sessionID, e -> new HashMap<>());
    }

    public static boolean hasSession(String session) {
        return sessions.containsKey(session);
    }

    public static boolean hashFlash(String session) {
        return !flash.get(session).isEmpty();
    }

    public static Map<String, Map<String, String>> getSessions() {
        return sessions;
    }

    public static Map<String, Map<String, String>> getFlash() {
        return flash;
    }
}
