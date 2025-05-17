package org.whilmarbitoco.core.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static Map<String, Map<String, String>> sessions = new HashMap<>();
    private static Map<String, Map<String, String>> flash = new HashMap<>();


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
