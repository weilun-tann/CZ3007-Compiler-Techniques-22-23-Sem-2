package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import lexer.Lexer;

import org.junit.Test;

import frontend.Token;
import frontend.Token.Type;
import static frontend.Token.Type.*;

/**
 * This class contains unit tests for your lexer. Currently, there is only one test, but you
 * are strongly encouraged to write your own tests.
 */
public class LexerTests {
	// helper method to run tests; no need to change this
	private final void runtest(String input, Token... output) {
		Lexer lexer = new Lexer(new StringReader(input));
		int i=0;
		Token actual=new Token(MODULE, 0, 0, ""), expected;
		try {
			do {
				assertTrue(i < output.length);
				expected = output[i++];
				try {
					actual = lexer.nextToken();
					assertEquals(expected, actual);
				} catch(Error e) {
					if(expected != null)
						fail(e.getMessage());
					/* return; */
				}
			} while(!actual.isEOF());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/** Example unit test. */
	@Test
	public void testKWs() {
		// first argument to runtest is the string to lex; the remaining arguments
		// are the expected tokens
		runtest("module false return while",
				new Token(MODULE, 0, 0, "module"),
				new Token(FALSE, 0, 7, "false"),
				new Token(RETURN, 0, 13, "return"),
				new Token(WHILE, 0, 20, "while"),
				new Token(EOF, 0, 25, ""));
	}

	@Test
	public void testStringLiteralWithDoubleQuote() {
		runtest("\"\"\"",
				new Token(STRING_LITERAL, 0, 0, ""),
				(Token)null,
				new Token(EOF, 0, 3, ""));
	}

	@Test
	public void testStringLiteral() {
		runtest("\"\\n\"", 
				new Token(STRING_LITERAL, 0, 0, "\\n"),
				new Token(EOF, 0, 4, ""));
	}

	@Test
	public void testIntAndOperatorLiteral() {
		runtest("9*5=45", 
				new Token(INT_LITERAL, 0, 0, "9"),
				new Token(TIMES, 0, 1, "*"),
				new Token(INT_LITERAL, 0, 2, "5"),
				new Token(EQL, 0, 3, "="),
				new Token(INT_LITERAL, 0, 4, "45"),
				new Token(EOF, 0, 6, ""));
	}
	
	@Test
	public void testStringLiteralWithDoubleQuote2() {
		runtest("\"Test\"\"",
				new Token(STRING_LITERAL, 0, 0, "Test"),
				(Token)null,
				new Token(EOF, 0, 7, ""));
	}
	
	@Test
	public void testPunctuation() {
		runtest("[],{};()",
				new Token(LBRACKET, 0, 0, "["),
				new Token(RBRACKET, 0, 1, "]"),
				new Token(COMMA, 0, 2, ","),
				new Token(LCURLY, 0, 3, "{"),
				new Token(RCURLY, 0, 4, "}"),
				new Token(SEMICOLON, 0, 5, ";"),
				new Token(LPAREN, 0, 6, "("),
				new Token(RPAREN, 0, 7, ")"),
				new Token(EOF, 0, 8, ""));
	}
	
	@Test
	public void testInvalidIdentifier() {
		runtest("1eqt340_cae  while",
				(Token)null,
				new Token(ID, 0, 1, "eqt340_cae"),
				new Token(WHILE, 0, 13, "while"),
				new Token(EOF, 0, 18, ""));
	}
	
	@Test
	public void testValidIdentifier() {
		runtest("eqt340_cae",
				new Token(ID, 0, 0, "eqt340_cae"),
				new Token(EOF, 0, 10, ""));
	}
	
	@Test
	public void testStringWithSpace() {
		runtest("\"abc  abc123\" while",
				new Token(STRING_LITERAL, 0, 0, "abc  abc123"),
				new Token(WHILE, 0, 14, "while"),
				new Token(EOF, 0, 19, ""));
	}
	/**To understand the hard tests (try to use python print() to see code)
	 * "\"\\\"\\\"\\\"\"" = code("\"\"\"")
	 *	\\ = code(\)
	 * 	\" = code(")
	 */

}
