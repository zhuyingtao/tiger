import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;
import parser.Parser;
import control.CommandLine;
import control.Control;
import control.Control.Codegen_Kind_t;

public class Tiger {

	public static void main(String[] args) {
		InputStream fstream;
		Parser parser;

		// /////////////////////////////////////////////////////
		// handle command line arguments
		CommandLine cmd = new CommandLine();
		String fname = cmd.scan(args);

//		 String fname = "test/javaSrc/TreeVisitor.java";
		// /////////////////////////////////////////////////////
		// to test the pretty printer on the "test/Fac.java" program
		if (control.Control.testFac) {
			System.out
					.println("Testing the Tiger compiler on Fac.java starting:");
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			ast.Fac.prog.accept(pp);

			// elaborate the given program, this step is necessary
			// for that it will annotate the AST with some
			// informations used by later phase.
			elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
			ast.Fac.prog.accept(elab);

			// Compile this program to C.
			System.out.println("Translate the program to C");
			codegen.C.TranslateVisitor trans2C = new codegen.C.TranslateVisitor();
			// pass this visitor to the "Fac.java" program.
			ast.Fac.prog.accept(trans2C);
			// this visitor will return an AST for C.
			codegen.C.program.T cast = trans2C.program;
			// output the AST for C.
			codegen.C.PrettyPrintVisitor ppc = new codegen.C.PrettyPrintVisitor();
			cast.accept(ppc);

			System.out
					.println("Testing the Tiger compiler on Fac.java finished.");
			System.exit(1);
		}

		if (fname == null) {
			cmd.usage();
			return;
		}

		Control.fileName = fname.substring(fname.lastIndexOf("/") + 1);

		// /////////////////////////////////////////////////////
		// it would be helpful to be able to test the lexer
		// independently.
		if (control.Control.testlexer) {
			System.out.println("Testing the lexer. All tokens:");
			try {
				fstream = new BufferedInputStream(new FileInputStream(fname));
				Lexer lexer = new Lexer(fname, fstream);
				Token token = lexer.nextToken();
				while (token.kind != Kind.TOKEN_EOF) {
					System.out.println(token.toString());
					token = lexer.nextToken();
				}
				fstream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(1);
		}

		// /////////////////////////////////////////////////////////
		// normal compilation phases.
		ast.program.T theAst = null;

		// parsing the file, get an AST.
		try {
			fstream = new BufferedInputStream(new FileInputStream(fname));
			parser = new Parser(fname, fstream);

			theAst = parser.parse();

			fstream.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// pretty printing the AST, if necessary
		if (control.Control.dumpAst) {
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			theAst.accept(pp);
		}

		// elaborate the AST, report all possible errors.
		elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
		theAst.accept(elab);

		codegen.bytecode.PrettyPrintVisitor ppbc = null;
		codegen.C.PrettyPrintVisitor ppc = null;
		// code generation
		switch (control.Control.codegen) {
		case Bytecode:
			codegen.bytecode.TranslateVisitor trans = new codegen.bytecode.TranslateVisitor();
			theAst.accept(trans);
			codegen.bytecode.program.T bytecodeAst = trans.program;
			ppbc = new codegen.bytecode.PrettyPrintVisitor();
			bytecodeAst.accept(ppbc);
			break;
		case C:
			codegen.C.TranslateVisitor transC = new codegen.C.TranslateVisitor();
			theAst.accept(transC);
			codegen.C.program.T cAst = transC.program;
			ppc = new codegen.C.PrettyPrintVisitor();
			cAst.accept(ppc);
			break;
		case Dalvik:
			codegen.dalvik.TranslateVisitor transDalvik = new codegen.dalvik.TranslateVisitor();
			theAst.accept(transDalvik);
			codegen.dalvik.program.T dalvikAst = transDalvik.program;
			codegen.dalvik.PrettyPrintVisitor ppDalvik = new codegen.dalvik.PrettyPrintVisitor();
			dalvikAst.accept(ppDalvik);
			break;
		case X86:
			// similar
			break;
		default:
			break;
		}

		// Lab3, exercise 6: add some glue code to
		// call gcc to compile the generated C or x86
		// file, or call java to run the bytecode file.
		// Your code:
		Runtime run = Runtime.getRuntime();
		if (control.Control.codegen == Codegen_Kind_t.C) {
			try {
				String file = "test/ccodeTest/" + Control.fileName;
				run.exec("gcc " + file + ".c  runtime/runtime.c -o " + file
						+ ".exe");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else if (control.Control.codegen == Codegen_Kind_t.Bytecode) {
			try {
				for (int i = 0; i < ppbc.jNames.size(); i++) {
					run.exec("java -jar jasmin.jar " + ppbc.jNames.get(i)
							+ " -d test/bytecodeTest");
				}
				// String[] ss=ppbc.jNames.get(0).split("\\.");
				// the regex . can present any character,so use "\\."
				// run.exec("java " + ppbc.jNames.get(0).split("\\.")[0]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
