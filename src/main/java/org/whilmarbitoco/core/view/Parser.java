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
                    List<Node> loopBody = parseLoopBody();
                    nodes.add(new ForNode(varName, listName, loopBody));
                    break;

                case IF:
                    lexer.advance();
                    String condition = token.value.trim();
                    List<Node> ifBody = parseIfBody();
                    nodes.add(new IfNode(condition, ifBody));
                    break;

                default:
                    lexer.advance();
            }
        }

        return nodes;
    }

    private List<Node> parseIfBody() {
        List<Node> bodyNodes = new ArrayList<>();

        while (lexer.getCurrentToken().type != TokenType.ENDIF && lexer.getCurrentToken().type != TokenType.EOF) {
            Token token = lexer.getCurrentToken();

            switch (token.type) {
                case TEXT:
                    bodyNodes.add(new TextNode(token.value));
                    lexer.advance();
                    break;

                case VARIABLE:
                    bodyNodes.add(new VariableNode(token.value));
                    lexer.advance();
                    break;

                case FOR:
                    String[] parts = token.value.split(",");
                    String varName = parts[0].trim();
                    String listName = parts[1].trim();
                    lexer.advance();
                    List<Node> nestedLoopBody = parseLoopBody();
                    bodyNodes.add(new ForNode(varName, listName, nestedLoopBody));
                    break;

                case IF:
                    lexer.advance();
                    String condition = token.value.trim();
                    List<Node> ifBody = parseIfBody();
                    bodyNodes.add(new IfNode(condition, ifBody));
                    break;

                case ENDIF:
                    // End of the current IF block
                    return bodyNodes;

                default:
                    lexer.advance();
            }
        }

        return bodyNodes;
    }

    private List<Node> parseLoopBody() {
        List<Node> bodyNodes = new ArrayList<>();

        while (lexer.getCurrentToken().type != TokenType.ENDFOR) {
            Token token = lexer.getCurrentToken();

            switch (token.type) {
                case TEXT:
                    bodyNodes.add(new TextNode(token.value));
                    break;
                case VARIABLE:
                    bodyNodes.add(new VariableNode(token.value));
                    break;
                case FOR:
                    String[] parts = token.value.split(",");
                    String varName = parts[0].trim();
                    String listName = parts[1].trim();
                    lexer.advance();
                    List<Node> nestedLoopBody = parseLoopBody();
                    bodyNodes.add(new ForNode(varName, listName, nestedLoopBody));
                    break;
                case IF:
                    break;
            }

            lexer.advance();
        }

        lexer.advance();
        return bodyNodes;
    }

}
