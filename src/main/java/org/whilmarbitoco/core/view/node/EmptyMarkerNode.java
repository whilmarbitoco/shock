package org.whilmarbitoco.core.view.node;

import java.util.List;
import java.util.Map;

/**
 * Internal marker node used by the parser to attach an {% empty %} block
 * to a ForNode. Not part of the public API.
 */
public class EmptyMarkerNode extends Node {
    public final List<Node> emptyBody;

    public EmptyMarkerNode(List<Node> emptyBody) {
        this.emptyBody = emptyBody;
    }

    @Override
    public String render(Map<String, Object> context) {
        return "";
    }
}
