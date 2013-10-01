package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;

public class Parser {
	Lexer lexer;
	Token current;

	String syntax;
	String preSyntax;

	public Parser(String fname, java.io.InputStream fstream) {
		lexer = new Lexer(fname, fstream);
		current = lexer.nextToken();
	}

	// /////////////////////////////////////////////
	// utility methods to connect the lexer
	// and the parser.

	private void advance() {
		current = lexer.nextToken();
	}

	private void eatToken(Kind kind) {
		if (kind == current.kind)
			advance();
		else {
			System.out.println("Expects: " + kind.toString());
			System.out
					.println("But got: "
							+ current.kind.toString()
							+ (current.kind == Token.Kind.TOKEN_ID ? (": " + current.lexeme)
									: "") + current.printPos()
							+ "  , Syntax : " + preSyntax + " --> " + syntax);
			System.exit(1);
		}
	}

	private void error() {
		System.out.println("Syntax error: compilation aborting..."
				+ current.kind.toString() + "  " + this.syntax + "-->"
				+ current.printPos());
		System.exit(1);
		return;
	}

	// ////////////////////////////////////////////////////////////
	// below are method for parsing.

	// A bunch of parsing methods to parse expressions. The messy
	// parts are to deal with precedence and associativity.

	// ExpList -> Exp ExpRest*
	// ->
	// ExpRest -> , Exp
	private void parseExpList() {
		this.preSyntax = this.syntax;
		this.syntax = "parseExpList";
		if (current.kind == Kind.TOKEN_RPAREN)
			return;
		parseExp();
		while (current.kind == Kind.TOKEN_COMMER) {
			advance();
			parseExp();
		}
		return;
	}

	// AtomExp -> (exp)
	// -> INTEGER_LITERAL
	// -> true
	// -> false
	// -> this
	// -> id
	// -> new int [exp]
	// -> new id ()
	private void parseAtomExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseAtomExp";
		switch (current.kind) {
		case TOKEN_LPAREN:
			advance();
			parseExp();
			eatToken(Kind.TOKEN_RPAREN);
			return;
		case TOKEN_NUM:
			advance();
			return;
		case TOKEN_TRUE:
			advance();
			return;
		case TOKEN_FALSE: // My add
			advance();
			return;
		case TOKEN_THIS:
			advance();
			return;
		case TOKEN_ID:
			advance();
			return;
		case TOKEN_NEW: {
			advance();
			switch (current.kind) {
			case TOKEN_INT:
				advance();
				eatToken(Kind.TOKEN_LBRACK);
				parseExp();
				eatToken(Kind.TOKEN_RBRACK);
				return;
			case TOKEN_ID:
				advance();
				eatToken(Kind.TOKEN_LPAREN);
				eatToken(Kind.TOKEN_RPAREN);
				return;
			default:
				error();
				return;
			}
		}
		default:
			error();
			return;
		}
	}

	// NotExp -> AtomExp
	// -> AtomExp .id (expList)
	// -> AtomExp [exp]
	// -> AtomExp .length
	private void parseNotExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseNotExp";
		parseAtomExp();
		while (current.kind == Kind.TOKEN_DOT
				|| current.kind == Kind.TOKEN_LBRACK) {
			if (current.kind == Kind.TOKEN_DOT) {
				advance();
				if (current.kind == Kind.TOKEN_LENGTH) {
					advance();
					return;
				}
				eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_LPAREN);
				parseExpList();
				eatToken(Kind.TOKEN_RPAREN);
			} else {
				advance();
				parseExp();
				eatToken(Kind.TOKEN_RBRACK);
			}
		}
		return;
	}

	// TimesExp -> ! TimesExp
	// -> NotExp
	private void parseTimesExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseTimesExp";
		while (current.kind == Kind.TOKEN_NOT) {
			advance();
		}
		parseNotExp();
		return;
	}

	// AddSubExp -> TimesExp * TimesExp
	// -> TimesExp
	private void parseAddSubExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseAddSubExp";
		parseTimesExp();
		while (current.kind == Kind.TOKEN_TIMES) {
			advance();
			parseTimesExp();
		}
		return;
	}

	// LtExp -> AddSubExp + AddSubExp
	// -> AddSubExp - AddSubExp
	// -> AddSubExp
	private void parseLtExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseLtExp";
		parseAddSubExp();
		while (current.kind == Kind.TOKEN_ADD || current.kind == Kind.TOKEN_SUB) {
			advance();
			parseAddSubExp();
		}
		return;
	}

	// AndExp -> LtExp < LtExp
	// -> LtExp
	private void parseAndExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseAndExp";
		parseLtExp();
		while (current.kind == Kind.TOKEN_LT) {
			advance();
			parseLtExp();
		}
		return;
	}

	// Exp -> AndExp && AndExp
	// -> AndExp
	private void parseExp() {
		this.preSyntax = this.syntax;
		this.syntax = "parseExp";
		parseAndExp();
		while (current.kind == Kind.TOKEN_AND) {
			advance();
			parseAndExp();
		}
		return;
	}

	// Statement -> { Statement* }
	// -> if ( Exp ) Statement else Statement
	// -> while ( Exp ) Statement
	// -> System.out.println ( Exp ) ;
	// -> id = Exp ;
	// -> id [ Exp ]= Exp ;
	boolean comeFromVarDecls = false;// define this var to judge whether this is

	// first time entering the Statement part or not
	// base on it,we can distinguish the token "id"

	private void parseStatement() {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a statement.
		/************ My Code *****************/
		this.preSyntax = this.syntax;
		this.syntax = "parseStatement";
		switch (current.kind) {
		case TOKEN_LBRACE:
			eatToken(Kind.TOKEN_LBRACE);
			this.parseStatements();
			eatToken(Kind.TOKEN_RBRACE);
			break;
		case TOKEN_IF:
			eatToken(Kind.TOKEN_IF);
			eatToken(Kind.TOKEN_LPAREN);
			this.parseExp();
			eatToken(Kind.TOKEN_RPAREN);
			this.parseStatement();
			eatToken(Kind.TOKEN_ELSE);
			this.parseStatement();
			break;
		case TOKEN_WHILE:
			eatToken(Kind.TOKEN_WHILE);
			eatToken(Kind.TOKEN_LPAREN);
			this.parseExp();
			eatToken(Kind.TOKEN_RPAREN);
			this.parseStatement();
			break;
		case TOKEN_SYSTEM:
			eatToken(Kind.TOKEN_SYSTEM);
			eatToken(Kind.TOKEN_DOT);
			eatToken(Kind.TOKEN_OUT);
			eatToken(Kind.TOKEN_DOT);
			eatToken(Kind.TOKEN_PRINTLN);
			eatToken(Kind.TOKEN_LPAREN);
			this.parseExp();
			eatToken(Kind.TOKEN_RPAREN);
			eatToken(Kind.TOKEN_SEMI);
			break;
		case TOKEN_ID:
			if (comeFromVarDecls) {
				current = idAfter;
				comeFromVarDecls = false;
			} else {
				eatToken(Kind.TOKEN_ID);
			}
			if (current.kind == Kind.TOKEN_LBRACK) {
				eatToken(Kind.TOKEN_LBRACK);
				this.parseExp();
				eatToken(Kind.TOKEN_RBRACK);
			}
			eatToken(Kind.TOKEN_ASSIGN);
			this.parseExp();
			eatToken(Kind.TOKEN_SEMI);
			break;
		default:
			error();
		}
	}

	// Statements -> Statement Statements
	// ->
	private void parseStatements() {
		this.preSyntax = this.syntax;
		this.syntax = "parseStatements";
		while (current.kind == Kind.TOKEN_LBRACE
				|| current.kind == Kind.TOKEN_IF
				|| current.kind == Kind.TOKEN_WHILE
				|| current.kind == Kind.TOKEN_SYSTEM
				|| current.kind == Kind.TOKEN_ID) {
			parseStatement();
		}
		return;
	}

	// Type -> int []
	// -> boolean
	// -> int
	// -> id
	Token idAfter;
	boolean comeFromVar = false;

	private void parseType() {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a type.
		/************** My Code ****************/
		this.preSyntax = this.syntax;
		this.syntax = "parseType";
		switch (current.kind) {
		case TOKEN_INT:
			eatToken(Kind.TOKEN_INT);
			if (current.kind == Kind.TOKEN_LBRACK) {
				advance();
				eatToken(Kind.TOKEN_RBRACK);
			}
			break;
		case TOKEN_BOOLEAN:
			eatToken(Kind.TOKEN_BOOLEAN);
			break;
		case TOKEN_ID:
			if (comeFromVar) {
				current = idAfter;
				comeFromVar = false;
			} else
				eatToken(Kind.TOKEN_ID);
			break;
		default:
			error();
			break;
		}
	}

	// VarDecl -> Type id ;
	private void parseVarDecl() {
		// to parse the "Type" nonterminal in this method, instead of writing
		// a fresh one.
		this.preSyntax = this.syntax;
		this.syntax = "parseVarDecl";
		parseType();
		eatToken(Kind.TOKEN_ID);
		eatToken(Kind.TOKEN_SEMI);
		return;
	}

	// VarDecls -> VarDecl VarDecls
	// ->

	private void parseVarDecls() {
		this.preSyntax = this.syntax;
		this.syntax = "parseVarDecls";
		// VarDecl* and Statement* can both start with "id",so it is ambiguous.
		// To distinguish,we can read the next token.
		while (current.kind == Kind.TOKEN_INT
				|| current.kind == Kind.TOKEN_BOOLEAN
				|| current.kind == Kind.TOKEN_ID) {
			if (current.kind == Kind.TOKEN_ID) {
				idAfter = lexer.nextToken();
				if (idAfter.kind == Kind.TOKEN_ASSIGN
						|| idAfter.kind == Kind.TOKEN_LBRACK) {
					comeFromVarDecls = true;
					break;
				}
				comeFromVar = true;
				comeFromVarDecls = true;
			}
			parseVarDecl();
		}
		return;
	}

	// FormalList -> Type id FormalRest*
	// ->
	// FormalRest -> , Type id
	private void parseFormalList() {
		this.preSyntax = this.syntax;
		this.syntax = "parseFormalList";
		if (current.kind == Kind.TOKEN_INT
				|| current.kind == Kind.TOKEN_BOOLEAN
				|| current.kind == Kind.TOKEN_ID) {
			parseType();
			eatToken(Kind.TOKEN_ID);
			while (current.kind == Kind.TOKEN_COMMER) {
				advance();
				parseType();
				eatToken(Kind.TOKEN_ID);
			}
		}
		return;
	}

	// Method -> public Type id ( FormalList )
	// { VarDecl* Statement* return Exp ;}
	private void parseMethod() {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a method.
		/************** My Code ****************/
		this.preSyntax = this.syntax;
		this.syntax = "parseMethod";
		this.comeFromVarDecls = false;
		eatToken(Kind.TOKEN_PUBLIC);
		this.parseType();
		eatToken(Kind.TOKEN_ID);
		eatToken(Kind.TOKEN_LPAREN);
		this.parseFormalList();
		eatToken(Kind.TOKEN_RPAREN);
		eatToken(Kind.TOKEN_LBRACE);
		this.parseVarDecls();
		this.parseStatements();
		eatToken(Kind.TOKEN_RETURN);
		this.parseExp();
		eatToken(Kind.TOKEN_SEMI);
		eatToken(Kind.TOKEN_RBRACE);
		return;
	}

	// MethodDecls -> MethodDecl MethodDecls
	// ->
	private void parseMethodDecls() {
		this.preSyntax = this.syntax;
		this.syntax = "parseMethodDecls";
		while (current.kind == Kind.TOKEN_PUBLIC) {
			parseMethod();
		}
		return;
	}

	// ClassDecl -> class id { VarDecl* MethodDecl* }
	// -> class id extends id { VarDecl* MethodDecl* }
	private void parseClassDecl() {
		this.preSyntax = this.syntax;
		this.syntax = "parseClassDecl";
		eatToken(Kind.TOKEN_CLASS);
		eatToken(Kind.TOKEN_ID);
		if (current.kind == Kind.TOKEN_EXTENDS) {
			eatToken(Kind.TOKEN_EXTENDS);
			eatToken(Kind.TOKEN_ID);
		}
		eatToken(Kind.TOKEN_LBRACE);
		parseVarDecls();
		parseMethodDecls();
		eatToken(Kind.TOKEN_RBRACE);
		return;
	}

	// ClassDecls -> ClassDecl ClassDecls
	// ->
	private void parseClassDecls() {
		this.preSyntax = this.syntax;
		this.syntax = "parseClassDecls";
		while (current.kind == Kind.TOKEN_CLASS) {
			parseClassDecl();
		}
		return;
	}

	// MainClass -> class id
	// {
	// public static void main ( String [] id )
	// {
	// Statement
	// }
	// }
	private void parseMainClass() {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a main class as described by the
		// grammar above.
		/**************** My Code ******************/
		this.preSyntax = this.syntax;
		this.syntax = "parseMainClass";
		eatToken(Kind.TOKEN_CLASS);
		eatToken(Kind.TOKEN_ID);
		eatToken(Kind.TOKEN_LBRACE);
		eatToken(Kind.TOKEN_PUBLIC);
		eatToken(Kind.TOKEN_STATIC);
		eatToken(Kind.TOKEN_VOID);
		eatToken(Kind.TOKEN_MAIN);
		eatToken(Kind.TOKEN_LPAREN);
		eatToken(Kind.TOKEN_STRING);
		eatToken(Kind.TOKEN_LBRACK);
		eatToken(Kind.TOKEN_RBRACK);
		eatToken(Kind.TOKEN_ID);
		eatToken(Kind.TOKEN_RPAREN);
		eatToken(Kind.TOKEN_LBRACE);
		this.parseStatement();
		eatToken(Kind.TOKEN_RBRACE);
		eatToken(Kind.TOKEN_RBRACE);
		return;
		// new util.Todo();
	}

	// Program -> MainClass ClassDecl*
	private void parseProgram() {
		this.preSyntax = this.syntax;
		this.syntax = "parseProgram";
		parseMainClass();
		parseClassDecls();
		eatToken(Kind.TOKEN_EOF);
		return;
	}

	public void parse() {
		parseProgram();
		System.out.println("Your Program is Valid!!");
		return;
	}
}
