package org.whilmarbitoco.core.view.node;

import java.util.Map;

public class VariableNode extends Node {

    private final String variableName;

    public VariableNode(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String render(Map<String, Object> context) {
        Object value = context.get(variableName);
        return value != null ? value.toString() : "";
    }
}
