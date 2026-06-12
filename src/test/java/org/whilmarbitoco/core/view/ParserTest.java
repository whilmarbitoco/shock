package org.whilmarbitoco.core.view;

import org.whilmarbitoco.core.view.node.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the template engine Parser.
 */
class ParserTest {

    private List<Node> parse(String template) {
        Lexer lexer = new Lexer(template);
        Parser parser = new Parser(lexer);
        return parser.parse();
    }

    @Test
    @DisplayName("Plain text produces a single TextNode")
    void plainText() {
        List<Node> nodes = parse("Hello World");
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        assertEquals("Hello World", nodes.get(0).render(Map.of()));
    }

    @Test
    @DisplayName("Variable produces a VariableNode")
    void variableNode() {
        List<Node> nodes = parse("{{ name }}");
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof VariableNode);
    }

    @Test
    @DisplayName("IF block produces an IfNode")
    void ifBlock() {
        List<Node> nodes = parse("{% if show %}hello{% endif %}");
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof IfNode);
    }

    @Test
    @DisplayName("FOR block produces a ForNode")
    void forBlock() {
        List<Node> nodes = parse("{% for item in items %}{{ item }}{% endfor %}");
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof ForNode);
    }

    @Test
    @DisplayName("Mixed content: text + variable + text")
    void mixedNodes() {
        List<Node> nodes = parse("Hi {{ name }}!");
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        assertTrue(nodes.get(1) instanceof VariableNode);
        assertTrue(nodes.get(2) instanceof TextNode);
    }

    @Test
    @DisplayName("IF with text body renders correctly when condition is true")
    void ifNodeRendersWhenTrue() {
        List<Node> nodes = parse("{% if show %}visible{% endif %}");
        IfNode ifNode = (IfNode) nodes.get(0);
        String result = ifNode.render(Map.of("show", true));
        assertEquals("visible", result);
    }

    @Test
    @DisplayName("IF with text body renders empty when condition is false")
    void ifNodeRendersWhenFalse() {
        List<Node> nodes = parse("{% if show %}visible{% endif %}");
        IfNode ifNode = (IfNode) nodes.get(0);
        String result = ifNode.render(Map.of("show", false));
        assertEquals("", result);
    }

    @Test
    @DisplayName("IF with text body renders empty when condition key is missing")
    void ifNodeRendersWhenMissing() {
        List<Node> nodes = parse("{% if show %}visible{% endif %}");
        IfNode ifNode = (IfNode) nodes.get(0);
        String result = ifNode.render(Map.of());
        assertEquals("", result);
    }

    @Test
    @DisplayName("FOR loop renders each item")
    void forNodeRendersList() {
        List<Node> nodes = parse("{% for item in items %}{{ item }}{% endfor %}");
        ForNode forNode = (ForNode) nodes.get(0);
        String result = forNode.render(Map.of("items", List.of("a", "b", "c")));
        assertEquals("abc", result);
    }

    @Test
    @DisplayName("FOR loop renders empty when list is empty")
    void forNodeRendersEmptyList() {
        List<Node> nodes = parse("{% for item in items %}{{ item }}{% endfor %}");
        ForNode forNode = (ForNode) nodes.get(0);
        String result = forNode.render(Map.of("items", List.of()));
        assertEquals("", result);
    }

    @Test
    @DisplayName("FOR loop renders empty when list key is missing")
    void forNodeRendersMissingList() {
        List<Node> nodes = parse("{% for item in items %}{{ item }}{% endfor %}");
        ForNode forNode = (ForNode) nodes.get(0);
        String result = forNode.render(Map.of());
        assertEquals("", result);
    }

    @Test
    @DisplayName("Nested IF inside FOR renders correctly")
    void nestedIfInFor() {
        List<Node> nodes = parse("{% for x in xs %}{% if x %}yes{% endif %}{% endfor %}");
        ForNode forNode = (ForNode) nodes.get(0);
        // With boolean true values
        String result = forNode.render(Map.of("xs", List.of(true, false, true)));
        assertEquals("yesyes", result);
    }

    @Test
    @DisplayName("Variable substitution in text")
    void variableSubstitution() {
        List<Node> nodes = parse("Hello {{ name }}!");
        String result = nodes.get(0).render(Map.of("name", "World"))
                + nodes.get(1).render(Map.of("name", "World"))
                + nodes.get(2).render(Map.of("name", "World"));
        assertEquals("Hello World!", result);
    }

    @Test
    @DisplayName("Missing variable renders placeholder")
    void missingVariable() {
        List<Node> nodes = parse("{{ missing }}");
        VariableNode varNode = (VariableNode) nodes.get(0);
        String result = varNode.render(Map.of());
        assertEquals("{{ missing }}", result);
    }

    @Test
    @DisplayName("Multiple IF blocks at the same level")
    void multipleIfBlocks() {
        List<Node> nodes = parse("{% if a %}A{% endif %}{% if b %}B{% endif %}");
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof IfNode);
        assertTrue(nodes.get(1) instanceof IfNode);

        assertEquals("A", nodes.get(0).render(Map.of("a", true, "b", false)));
        assertEquals("", nodes.get(1).render(Map.of("a", true, "b", false)));
    }

    @Test
    @DisplayName("FOR with nested FOR")
    void nestedForLoops() {
        List<Node> nodes = parse("{% for a in outer %}{% for b in inner %}{{ b }}{% endfor %}{% endfor %}");
        ForNode outer = (ForNode) nodes.get(0);
        String result = outer.render(Map.of(
                "outer", List.of(1, 2),
                "inner", List.of("x", "y")
        ));
        // Each outer iteration re-renders the inner loop with the same context
        assertEquals("xyxy", result);
    }
}
