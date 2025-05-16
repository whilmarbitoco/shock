package org.whilmarbitoco.core.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static final String TOKEN_PATTERN =
            "(\\{%\\s*for\\s+(\\w+)\\s+in\\s+(\\w+)\\s*%\\})|" +  // Group 1 (FOR), Group 2 (var), Group 3 (list)
                    "(\\{%\\s*endfor\\s*%\\})|" +                          // Group 4 (ENDFOR)
                    "(\\{%\\s*if\\s+(\\w+)\\s*%\\})|" +                    // Group 5 (IF), Group 6 (condition)
                    "(\\{%\\s*endif\\s*%\\})|" +                           // Group 7 (ENDIF)
                    "(\\{\\{\\s*(\\w+)\\s*}})|" +                          // Group 8 (VARIABLE), Group 9 (identifier)
                    "([^{}]+)|" +                                         // Group 10 (TEXT)
                    "(\\{%.*?%})|(.+)";                                   // Group 11 (INVALID), Group 12 (unmatched)


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
        if (!matcher.find()) {
            currentToken = new Token(TokenType.EOF, "");
            return;
        }

        if (matcher.group(1) != null) {
            // FOR loop
            String var = matcher.group(2);
            String list = matcher.group(3);
            currentToken = new Token(TokenType.FOR, var + "," + list);
        } else if (matcher.group(4) != null) {
            // ENDFOR
            currentToken = new Token(TokenType.ENDFOR, "");
        } else if (matcher.group(5) != null) {
            // IF
            String condition = matcher.group(6);
            currentToken = new Token(TokenType.IF, condition);
        } else if (matcher.group(7) != null) {
            // ENDIF
            currentToken = new Token(TokenType.ENDIF, "");
        } else if (matcher.group(8) != null) {
            // VARIABLE
            String identifier = matcher.group(9);
            currentToken = new Token(TokenType.VARIABLE, identifier);
        } else if (matcher.group(10) != null) {
            // TEXT
            String text = matcher.group(10);
            currentToken = new Token(TokenType.TEXT, text.trim());
        } else if (matcher.group(11) != null) {
            // INVALID
            currentToken = new Token(TokenType.INVALID, matcher.group(11));
        } else if (matcher.group(12) != null) {
            // Unmatched (catch-all)
            currentToken = new Token(TokenType.INVALID, matcher.group(12));
        }
    }

}
