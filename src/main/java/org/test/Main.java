package org.test;

import org.whilmarbitoco.core.view.Lexer;
import org.whilmarbitoco.core.view.Parser;
import org.whilmarbitoco.core.view.node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String template = """
                Hello, {{ username }}! \n
                {% if showList %} 
                Items:
                {% for item in items %}
                - {{ item }}
                {% endfor %}
                {% endif %}
                """;

        Map<String, Object> context = new HashMap<>();
        context.put("username", "Whilmar");
        context.put("showList", true);
        context.put("items", List.of("Apples", "Bananas", "Cherries"));

        Lexer lexer = new Lexer(template);
        Parser parser = new Parser(lexer);
        List<Node> nodes = parser.parse();

        StringBuilder output = new StringBuilder();
        for (Node node : nodes) {
            output.append(node.render(null));
        }

        System.out.println(output.toString());
    }
}
