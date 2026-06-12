package org.whilmarbitoco.core.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static final String TOKEN_PATTERN =
                    "(\\{%\\s*for\\s+(\\w+)\\s+in\\s+(\\w+)\\s*%})|" +      // FOR loop
                    "(\\{%\\s*endfor\\s*%})|" +                            // ENDFOR
                    "(\\{%\\s*empty\\s*%})|" +                             // EMPTY
                    "(\\{%\\s*if\\s+(\\w+)\\s*%})|" +                      // IF
                    "(\\{%\\s*elseif\\s+(\\w+)\\s*%})|" +                  // ELSEIF
                    "(\\{%\\s*else\\s*%})|" +                               // ELSE
                    "(\\{%\\s*endif\\s*%})|" +                             // ENDIF
                    "(\\{\\{\\s*(\\w+)\\s*}})|" +                           // VARIABLE
                    "(\\{%.*?%})|" +                                       // INVALID/Unknown Template Tag
                    "([^{}]+)|" +                                          // TEXT
                    "(\\{[^%][^}]*})";                                     // CSS/JS or non-template block

    private final Matcher matcher;
    private Token currentToken;

    public Lexer(String template) {
        matcher = Pattern.compile(TOKEN_PATTERN).matcher(template);
        advance();
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void advance() {
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                String var = matcher.group(2);
                String list = matcher.group(3);
                currentToken = new Token(TokenType.FOR, var + "," + list);
                return;
            } else if (matcher.group(4) != null) {
                currentToken = new Token(TokenType.ENDFOR, "");
                return;
            } else if (matcher.group(5) != null) {
                currentToken = new Token(TokenType.EMPTY, "");
                return;
            } else if (matcher.group(6) != null) {
                String condition = matcher.group(7);
                currentToken = new Token(TokenType.IF, condition);
                return;
            } else if (matcher.group(8) != null) {
                String condition = matcher.group(9);
                currentToken = new Token(TokenType.ELSEIF, condition);
                return;
            } else if (matcher.group(10) != null) {
                currentToken = new Token(TokenType.ELSE, "");
                return;
            } else if (matcher.group(11) != null) {
                currentToken = new Token(TokenType.ENDIF, "");
                return;
            } else if (matcher.group(12) != null) {
                String identifier = matcher.group(13);
                currentToken = new Token(TokenType.VARIABLE, identifier);
                return;
            } else if (matcher.group(15) != null) {
                String text = matcher.group(15);
                if (!text.isEmpty()) {
                    currentToken = new Token(TokenType.TEXT, text);
                    return;
                }
            } else if (matcher.group(16) != null) {
                currentToken = new Token(TokenType.TEXT, matcher.group(16));
                return;
            }
        }

        currentToken = new Token(TokenType.EOF, "");
    }
}
