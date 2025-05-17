package org.whilmarbitoco.core.view.node;

import java.util.List;
import java.util.Map;

public class IfNode extends Node {

    private final String condition;
    private final List<Node> body;

    public IfNode(String condition, List<Node> body) {
        this.condition = condition;
        this.body = body;
    }


    @Override
    public String render(Map<String, Object> context) {
        Object condValue = context.get(condition);

        if (condValue instanceof Boolean && (Boolean) condValue) {
          return processBody(context);
        }

        if (condValue != null) {
            return processBody(context);
        }

        return "";
    }

    protected String processBody(Map<String, Object> context) {
        StringBuilder sb = new StringBuilder();
        for (Node node : body) {
            sb.append(node.render(context));
        }
        return sb.toString();
    }
}
