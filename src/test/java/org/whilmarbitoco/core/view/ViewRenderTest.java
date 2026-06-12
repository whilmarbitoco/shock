package org.whilmarbitoco.core.view;

import org.whilmarbitoco.core.view.node.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the full template pipeline: Lexer → Parser → Node.render().
 * Tests the full rendering flow without depending on View class (which needs filesystem).
 */
class ViewRenderTest {

    private String renderTemplate(String template, Map<String, Object> context) {
        Lexer lexer = new Lexer(template);
        Parser parser = new Parser(lexer);
        List<Node> nodes = parser.parse();
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes) {
            sb.append(node.render(context));
        }
        return sb.toString();
    }

    @Test
    @DisplayName("Full pipeline: plain text passes through")
    void plainTextPassthrough() {
        assertEquals("Hello World", renderTemplate("Hello World", Map.of()));
    }

    @Test
    @DisplayName("Full pipeline: single variable substitution")
    void singleVariable() {
        String result = renderTemplate("Hello {{ name }}!", Map.of("name", "Alice"));
        assertEquals("Hello Alice!", result);
    }

    @Test
    @DisplayName("Full pipeline: IF true renders body")
    void ifTrue() {
        String result = renderTemplate("{% if show %}yes{% endif %}", Map.of("show", true));
        assertEquals("yes", result);
    }

    @Test
    @DisplayName("Full pipeline: IF false omits body")
    void ifFalse() {
        String result = renderTemplate("{% if show %}yes{% endif %}", Map.of("show", false));
        assertEquals("", result);
    }

    @Test
    @DisplayName("Full pipeline: IF with null/non-boolean truthy value renders body")
    void ifTruthy() {
        String result = renderTemplate("{% if name %}hello{% endif %}", Map.of("name", "Alice"));
        assertEquals("hello", result);
    }

    @Test
    @DisplayName("Full pipeline: FOR iterates over list")
    void forLoop() {
        String result = renderTemplate(
                "{% for item in items %}[{{ item }}]{% endfor %}",
                Map.of("items", List.of("a", "b", "c"))
        );
        assertEquals("[a][b][c]", result);
    }

    @Test
    @DisplayName("Full pipeline: FOR with empty list produces nothing")
    void forEmpty() {
        String result = renderTemplate(
                "{% for item in items %}[{{ item }}]{% endfor %}",
                Map.of("items", List.of())
        );
        assertEquals("", result);
    }

    @Test
    @DisplayName("Full pipeline: nested IF inside FOR")
    void nestedIfInFor() {
        String result = renderTemplate(
                "{% for x in xs %}{% if x %}T{% endif %}{% endfor %}",
                Map.of("xs", List.of(true, false, true))
        );
        assertEquals("TT", result);
    }

    @Test
    @DisplayName("Full pipeline: complex template with text, vars, if, and for")
    void complexTemplate() {
        String template = "Welcome {{ name }}! {% if items %}{% for item in items %}{{ item }} {% endfor %}{% endif %}Done.";
        String result = renderTemplate(template, Map.of(
                "name", "Bob",
                "items", List.of("x", "y")
        ));
        assertEquals("Welcome Bob! x y Done.", result);
    }

    @Test
    @DisplayName("Full pipeline: multiple variables in same template")
    void multipleVariables() {
        String result = renderTemplate(
                "{{ first }} {{ last }}",
                Map.of("first", "John", "last", "Doe")
        );
        assertEquals("John Doe", result);
    }

    @Test
    @DisplayName("Full pipeline: variable not in context renders placeholder")
    void missingVariable() {
        String result = renderTemplate("Hello {{ name }}!", Map.of());
        assertEquals("Hello {{ name }}!", result);
    }

    @Test
    @DisplayName("Full pipeline: if inside for with variable substitution")
    void ifInForWithVariable() {
        String result = renderTemplate(
                "{% for item in items %}{% if item %}{{ item }}!{% endif %}{% endfor %}",
                Map.of("items", List.of(true, false, true))
        );
        assertEquals("true!true!", result);
    }
}
