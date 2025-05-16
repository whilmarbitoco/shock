package org.whilmarbitoco.core.view.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForNode extends Node {

    private final String varName;
    private final String listName;
    private final List<Node> body;

    public ForNode(String varName, String listName, List<Node> body) {
        this.varName = varName;
        this.listName = listName;
        this.body = body;
    }


    @Override
    public String render(Map<String, Object> context) {
        StringBuilder sb = new StringBuilder();
        Object list = context.get(listName);

        if (list instanceof List<?>) {
            for (Object item : (List<?>) list) {
                Map<String, Object> loopContext = new HashMap<>(context);
                loopContext.put(varName, item);

                for (Node node : body) {
                    sb.append(node.render(loopContext));
                }
            }
        }

        return sb.toString();
    }
}
