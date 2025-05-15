package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.File;

public class View {

    public void render(String view) {
        String file = File.loadContent(view);
    }
}
