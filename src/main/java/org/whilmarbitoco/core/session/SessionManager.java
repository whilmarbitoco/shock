package org.whilmarbitoco.core.session;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private final Map<String, Object> data = new HashMap<>();
    private final Map<String, Object> flashData = new HashMap<>();

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void flash(String key, Object value) {
        flashData.put(key, value);
    }

    public Object getFlash(String key) {
        return flashData.remove(key);
    }

    public void clearFlashData() {
        flashData.clear();
    }
}
