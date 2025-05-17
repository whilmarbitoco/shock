package org.whilmarbitoco.core.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

//    DevNote: Do not remove labels,
//    I can't read regex :>

    private static final String TOKEN_PATTERN =
            "(\\{%\\s*for\\s+(\\w+)\\s+in\\s+(\\w+)\\s*%\\})|" +           // FOR loop
                    "(\\{%\\s*endfor\\s*%\\})|" +                          // ENDFOR
                    "(\\{%\\s*if\\s+(\\w+)\\s*%\\})|" +                    // IF
                    "(\\{%\\s*endif\\s*%\\})|" +                           // ENDIF
                    "(\\{\\{\\s*(\\w+)\\s*}})|" +                          // VARIABLE (strict identifier pattern)
                    "(\\{%.*?%})|" +                                       // INVALID/Unknown Template Tag
                    "([^{}]+)|" +                                          // TEXT
                    "(\\{[^%][^}]*})";                                     // CSS/JS or any non-template block


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
                String condition = matcher.group(6);
                currentToken = new Token(TokenType.IF, condition);
                return;
            } else if (matcher.group(7) != null) {
                currentToken = new Token(TokenType.ENDIF, "");
                return;
            } else if (matcher.group(8) != null) {
                String identifier = matcher.group(9);
                currentToken = new Token(TokenType.VARIABLE, identifier);
                return;
            }  else if (matcher.group(11) != null) {
                String text = matcher.group(11);
                if (!text.isEmpty()) {
                    currentToken = new Token(TokenType.TEXT, text);
                    return;
                }
            } else if (matcher.group(12) != null) {
                currentToken = new Token(TokenType.TEXT, matcher.group(12));
                return;
            }
        }

        currentToken = new Token(TokenType.EOF, "");
    }


}
