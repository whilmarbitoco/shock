package org.whilmarbitoco.core.view.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForNode extends Node {

    private final String varName;
    private final String listName;
    private final List<Node> body;
    private List<Node> emptyBody;

    public ForNode(String varName, String listName, List<Node> body) {
        this.varName = varName;
        this.listName = listName;
        this.body = body;
        this.emptyBody = null;

        // Extract empty block from marker nodes
        for (int i = 0; i < body.size(); i++) {
            if (body.get(i) instanceof EmptyMarkerNode marker) {
                this.emptyBody = marker.emptyBody;
                body.remove(i);
                break;
            }
        }
    }

    @Override
    public String render(Map<String, Object> context) {
        StringBuilder sb = new StringBuilder();
        Object list = context.get(listName);

        if (list instanceof List<?> items) {
            if (items.isEmpty() && emptyBody != null) {
                for (Node node : emptyBody) {
                    sb.append(node.render(context));
                }
            } else {
                for (Object item : items) {
                    Map<String, Object> loopContext = new HashMap<>(context);
                    loopContext.put(varName, item);
                    for (Node node : body) {
                        sb.append(node.render(loopContext));
                    }
                }
            }
        }

        return sb.toString();
    }
}
