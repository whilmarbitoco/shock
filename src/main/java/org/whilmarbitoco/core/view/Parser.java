package org.whilmarbitoco.core.view;

import org.whilmarbitoco.core.view.node.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public List<Node> parse() {
        List<Node> nodes = new ArrayList<>();

        while (lexer.getCurrentToken().type != TokenType.EOF) {
            Token token = lexer.getCurrentToken();
            switch (token.type) {
                case TEXT:
                    nodes.add(new TextNode(token.value));
                    lexer.advance();
                    break;

                case VARIABLE:
                    nodes.add(new VariableNode(token.value));
                    lexer.advance();
                    break;

                case FOR:
                    lexer.advance();
                    String[] parts = token.value.split(",");
                    String varName = parts[0].trim();
                    String listName = parts[1].trim();
                    List<Node> loopBody = parse();
                    nodes.add(new ForNode(varName, listName, loopBody));
                    break;

                case ENDFOR, ENDIF:
                    lexer.advance();
                    return nodes;

                case IF:
                    lexer.advance();
                    String condition = token.value.trim();
                    List<Node> ifBody = parse();
                    nodes.add(new IfNode(condition, ifBody));
                    break;

                default:
                    lexer.advance();
            }
        }

        return nodes;
    }
}
