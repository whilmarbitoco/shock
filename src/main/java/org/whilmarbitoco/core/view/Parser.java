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
            nodes.add(parseNode());
        }

        return nodes;
    }

    private Node parseNode() {
        Token token = lexer.getCurrentToken();

        switch (token.type) {
            case TEXT:
                lexer.advance();
                return new TextNode(token.value);

            case VARIABLE:
                lexer.advance();
                return new VariableNode(token.value);

            case FOR:
                return parseFor();

            case IF:
                return parseIf();

            default:
                lexer.advance();
                return new TextNode("");
        }
    }

    private ForNode parseFor() {
        String[] parts = lexer.getCurrentToken().value.split(",");
        String varName = parts[0].trim();
        String listName = parts[1].trim();
        lexer.advance(); // consume FOR
        List<Node> body = parseForBody();
        return new ForNode(varName, listName, body);
    }

    private List<Node> parseForBody() {
        List<Node> bodyNodes = new ArrayList<>();

        while (lexer.getCurrentToken().type != TokenType.ENDFOR
                && lexer.getCurrentToken().type != TokenType.EMPTY
                && lexer.getCurrentToken().type != TokenType.EOF) {
            bodyNodes.add(parseNode());
        }

        // Handle {% empty %} block
        List<Node> emptyBody = null;
        if (lexer.getCurrentToken().type == TokenType.EMPTY) {
            lexer.advance();
            emptyBody = new ArrayList<>();
            while (lexer.getCurrentToken().type != TokenType.ENDFOR
                    && lexer.getCurrentToken().type != TokenType.EOF) {
                emptyBody.add(parseNode());
            }
        }

        if (lexer.getCurrentToken().type == TokenType.ENDFOR) {
            lexer.advance();
        }

        if (emptyBody != null && !emptyBody.isEmpty()) {
            bodyNodes.add(new EmptyMarkerNode(emptyBody));
        }

        return bodyNodes;
    }

    private IfNode parseIf() {
        String condition = lexer.getCurrentToken().value.trim();
        lexer.advance(); // consume IF
        List<Node> ifBody = new ArrayList<>();
        List<IfNode.ElseIfBranch> elseIfBranches = new ArrayList<>();
        List<Node> elseBody = null;

        // Parse the if-true body
        while (lexer.getCurrentToken().type != TokenType.ELSEIF
                && lexer.getCurrentToken().type != TokenType.ELSE
                && lexer.getCurrentToken().type != TokenType.ENDIF
                && lexer.getCurrentToken().type != TokenType.EOF) {
            ifBody.add(parseNode());
        }

        // Parse elseif chains
        while (lexer.getCurrentToken().type == TokenType.ELSEIF) {
            String elseIfCondition = lexer.getCurrentToken().value.trim();
            lexer.advance();
            List<Node> elseIfBody = new ArrayList<>();
            while (lexer.getCurrentToken().type != TokenType.ELSEIF
                    && lexer.getCurrentToken().type != TokenType.ELSE
                    && lexer.getCurrentToken().type != TokenType.ENDIF
                    && lexer.getCurrentToken().type != TokenType.EOF) {
                elseIfBody.add(parseNode());
            }
            elseIfBranches.add(new IfNode.ElseIfBranch(elseIfCondition, elseIfBody));
        }

        // Parse else block
        if (lexer.getCurrentToken().type == TokenType.ELSE) {
            lexer.advance();
            elseBody = new ArrayList<>();
            while (lexer.getCurrentToken().type != TokenType.ENDIF
                    && lexer.getCurrentToken().type != TokenType.EOF) {
                elseBody.add(parseNode());
            }
        }

        if (lexer.getCurrentToken().type == TokenType.ENDIF) {
            lexer.advance();
        }

        return new IfNode(condition, ifBody, elseIfBranches, elseBody);
    }
}
