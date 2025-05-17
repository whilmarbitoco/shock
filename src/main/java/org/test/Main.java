package org.test;

import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.view.Lexer;
import org.whilmarbitoco.core.view.Parser;
import org.whilmarbitoco.core.view.node.Node;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

//    public static void main(String[] args) {
//        String template = """
//                Hello, {{ username }}! \n
//                {% if showList %}
//                Items:
//                {% for item in items %}
//                - {{ item }}
//                {% endfor %}
//                {% endif %}
//                """;
//
//        Map<String, Object> context = new HashMap<>();
//        context.put("username", "Whilmar");
//        context.put("showList", true);
//        context.put("items", List.of("Apples", "Bananas", "Cherries"));
//
//        Lexer lexer = new Lexer(template);
//        Parser parser = new Parser(lexer);
//        List<Node> nodes = parser.parse();
//
//        StringBuilder output = new StringBuilder();
//        for (Node node : nodes) {
//            output.append(node.render(null));
//        }
//
//        System.out.println(output.toString());
//    }

    public static void main(String[] args) {

        String sample = "shock-session=ca148ad0-7d73-4bca-8d48-19e3d62d6865";

        String res = getCookie(sample, "shock-session");
        System.out.println(res);
    }

    public static String getCookie(String cookies, String cookieName) {
        if (cookies == null) return null;

        String[] cookieArray = cookies.split("; ");
        for (String cookie : cookieArray) {
            String[] parts = cookie.split("=", 2);
            if (parts.length == 2 && parts[0].equals(cookieName)) {
                return parts[1];
            }
        }
        return null;
    }
}
