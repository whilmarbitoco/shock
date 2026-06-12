package org.whilmarbitoco.core.view.node;

import java.util.List;
import java.util.Map;

public class IfNode extends Node {

    private final String condition;
    private final List<Node> body;
    private final List<ElseIfBranch> elseIfBranches;
    private final List<Node> elseBody;

    public IfNode(String condition, List<Node> body) {
        this(condition, body, List.of(), null);
    }

    public IfNode(String condition, List<Node> body,
                  List<ElseIfBranch> elseIfBranches, List<Node> elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseIfBranches = elseIfBranches;
        this.elseBody = elseBody;
    }

    @Override
    public String render(Map<String, Object> context) {
        if (isTruthy(context.get(condition))) {
            return processBody(body, context);
        }

        for (ElseIfBranch branch : elseIfBranches) {
            if (isTruthy(context.get(branch.condition))) {
                return processBody(branch.body, context);
            }
        }

        if (elseBody != null) {
            return processBody(elseBody, context);
        }

        return "";
    }

    private boolean isTruthy(Object value) {
        if (value instanceof Boolean b) return b;
        return value != null;
    }

    protected String processBody(List<Node> nodes, Map<String, Object> context) {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.render(context));
        }
        return sb.toString();
    }

    public static class ElseIfBranch {
        public final String condition;
        public final List<Node> body;

        public ElseIfBranch(String condition, List<Node> body) {
            this.condition = condition;
            this.body = body;
        }
    }
}
