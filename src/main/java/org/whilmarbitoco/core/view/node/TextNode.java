package org.whilmarbitoco.core.view.node;

import java.util.Map;

public class TextNode extends Node {

    private final String text;

    public TextNode(String text) {
        this.text = text;
    }


    @Override
    public String render(Map<String, Object> context) {
        return text;
    }
}
