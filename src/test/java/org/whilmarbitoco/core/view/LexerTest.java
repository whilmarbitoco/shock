package org.whilmarbitoco.core.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the template engine Lexer.
 */
class LexerTest {

    @Test
    @DisplayName("Plain text produces a single TEXT token followed by EOF")
    void plainText() {
        Lexer lexer = new Lexer("Hello World");
        assertEquals(TokenType.TEXT, lexer.getCurrentToken().type);
        assertEquals("Hello World", lexer.getCurrentToken().value);
        lexer.advance();
        assertEquals(TokenType.EOF, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("Variable token is recognized")
    void variableToken() {
        Lexer lexer = new Lexer("{{ name }}");
        assertEquals(TokenType.VARIABLE, lexer.getCurrentToken().type);
        assertEquals("name", lexer.getCurrentToken().value);
    }

    @Test
    @DisplayName("IF token is recognized")
    void ifToken() {
        Lexer lexer = new Lexer("{% if show %}");
        assertEquals(TokenType.IF, lexer.getCurrentToken().type);
        assertEquals("show", lexer.getCurrentToken().value);
    }

    @Test
    @DisplayName("ENDIF token is recognized")
    void endifToken() {
        Lexer lexer = new Lexer("{% endif %}");
        assertEquals(TokenType.ENDIF, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("FOR token captures variable and list name")
    void forToken() {
        Lexer lexer = new Lexer("{% for item in items %}");
        assertEquals(TokenType.FOR, lexer.getCurrentToken().type);
        assertEquals("item,items", lexer.getCurrentToken().value);
    }

    @Test
    @DisplayName("ENDFOR token is recognized")
    void endforToken() {
        Lexer lexer = new Lexer("{% endfor %}");
        assertEquals(TokenType.ENDFOR, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("Mixed content: text, variable, text")
    void mixedTokens() {
        Lexer lexer = new Lexer("Hello {{ name }}!");
        assertEquals(TokenType.TEXT, lexer.getCurrentToken().type);
        assertEquals("Hello ", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.VARIABLE, lexer.getCurrentToken().type);
        assertEquals("name", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.TEXT, lexer.getCurrentToken().type);
        assertEquals("!", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.EOF, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("IF block with text inside")
    void ifBlockWithText() {
        Lexer lexer = new Lexer("{% if show %}visible{% endif %}");
        assertEquals(TokenType.IF, lexer.getCurrentToken().type);
        assertEquals("show", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.TEXT, lexer.getCurrentToken().type);
        assertEquals("visible", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.ENDIF, lexer.getCurrentToken().type);

        lexer.advance();
        assertEquals(TokenType.EOF, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("FOR block with variable inside")
    void forBlockWithVariable() {
        Lexer lexer = new Lexer("{% for item in items %}{{ item }}{% endfor %}");
        assertEquals(TokenType.FOR, lexer.getCurrentToken().type);

        lexer.advance();
        assertEquals(TokenType.VARIABLE, lexer.getCurrentToken().type);
        assertEquals("item", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.ENDFOR, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("Empty template produces EOF immediately")
    void emptyTemplate() {
        Lexer lexer = new Lexer("");
        assertEquals(TokenType.EOF, lexer.getCurrentToken().type);
    }

    @Test
    @DisplayName("Nested IF inside FOR tokens")
    void nestedIfInFor() {
        Lexer lexer = new Lexer("{% for x in xs %}{% if x %}yes{% endif %}{% endfor %}");
        assertEquals(TokenType.FOR, lexer.getCurrentToken().type);

        lexer.advance();
        assertEquals(TokenType.IF, lexer.getCurrentToken().type);

        lexer.advance();
        assertEquals(TokenType.TEXT, lexer.getCurrentToken().type);
        assertEquals("yes", lexer.getCurrentToken().value);

        lexer.advance();
        assertEquals(TokenType.ENDIF, lexer.getCurrentToken().type);

        lexer.advance();
        assertEquals(TokenType.ENDFOR, lexer.getCurrentToken().type);
    }
}
