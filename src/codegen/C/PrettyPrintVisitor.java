package codegen.C;

import java.util.ArrayList;
import java.util.HashMap;

import control.Control;

public class PrettyPrintVisitor implements Visitor {
	private int indentLevel;
	private java.io.BufferedWriter writer;
	private ArrayList<String> localNames;
	// the field map for every class
	private HashMap<String, String> fStrings = new HashMap<>();
	private String lString;

	public PrettyPrintVisitor() {
		this.indentLevel = 2;
	}

	private void indent() {
		this.indentLevel += 2;
	}

	private void unIndent() {
		this.indentLevel -= 2;
	}

	private void printSpaces() {
		int i = this.indentLevel;
		while (i-- != 0)
			this.say(" ");
	}

	private void sayln(String s) {
		say(s);
		try {
			this.writer.write("\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void say(String s) {
		try {
			this.writer.write(s);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(codegen.C.exp.Add e) {
		e.left.accept(this);
		this.say("+");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.C.exp.And e) {
		e.left.accept(this);
		this.say("&&");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.C.exp.ArraySelect e) {
		e.array.accept(this);
		this.say("[");
		e.index.accept(this);
		this.say("]");
	}

	@Override
	public void visit(codegen.C.exp.Call e) {
		this.say("(frame." + e.assign + "=");
		e.exp.accept(this);
		this.say(", ");
		this.say("frame." + e.assign + "->vptr->" + e.id + "(frame." + e.assign);
		int size = e.args.size();
		if (size == 0) {
			this.say("))");
			return;
		}
		for (codegen.C.exp.T x : e.args) {
			this.say(", ");
			x.accept(this);
		}
		this.say("))");
		return;
	}

	@Override
	public void visit(codegen.C.exp.Id e) {
		if (e.isField)
			this.say("this->");
		else if (localNames.contains(e.id)) { // e.id may also be an argument
			int index = localNames.indexOf(e.id);
			char isRef = lString.charAt(index);
			if (isRef == '1')
				this.say("frame.");
		}
		this.say(e.id);
	}

	@Override
	public void visit(codegen.C.exp.Length e) {
		this.say("Length(");
		e.array.accept(this);
		this.say(")");
	}

	@Override
	public void visit(codegen.C.exp.Lt e) {
		e.left.accept(this);
		this.say(" < ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(codegen.C.exp.NewIntArray e) {
		this.say("(int *)Tiger_new_array(");
		e.exp.accept(this);
		this.say(")");
	}

	@Override
	public void visit(codegen.C.exp.NewObject e) {
		this.say("((struct " + e.id + "*)(Tiger_new (&" + e.id
				+ "_vtable_, sizeof(struct " + e.id + "))))");
		return;
	}

	@Override
	public void visit(codegen.C.exp.Not e) {
		this.say("!(");
		e.exp.accept(this);
		this.say(")");
	}

	@Override
	public void visit(codegen.C.exp.Num e) {
		this.say(Integer.toString(e.num));
		return;
	}

	@Override
	public void visit(codegen.C.exp.Sub e) {
		e.left.accept(this);
		this.say(" - ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(codegen.C.exp.This e) {
		this.say("this");
	}

	@Override
	public void visit(codegen.C.exp.Times e) {
		e.left.accept(this);
		this.say(" * ");
		e.right.accept(this);
		return;
	}

	// statements
	@Override
	public void visit(codegen.C.stm.Assign s) {
		this.printSpaces();
		if (s.idIsField)
			this.say("this->");
		else if (localNames.contains(s.id)) {
			int index = localNames.indexOf(s.id);
			char isRef = lString.charAt(index);
			if (isRef == '1')
				this.say("frame.");
		}
		this.say(s.id + " = ");
		s.exp.accept(this);
		this.sayln(";");
		return;
	}

	@Override
	public void visit(codegen.C.stm.AssignArray s) {
		this.printSpaces();
		if (s.idIsField)
			this.say("this->");
		else if (localNames.contains(s.id)) {
			int index = localNames.indexOf(s.id);
			char isRef = lString.charAt(index);
			if (isRef == '1')
				this.say("frame.");
		}
		this.say(s.id + "[");
		s.index.accept(this);
		this.say("] = ");
		s.exp.accept(this);
		this.sayln(";");
	}

	@Override
	public void visit(codegen.C.stm.Block s) {
		for (codegen.C.stm.T stm : s.stms) {
			stm.accept(this);
		}
	}

	@Override
	public void visit(codegen.C.stm.If s) {
		this.printSpaces();
		this.say("if (");
		s.condition.accept(this);
		this.sayln("){");
		this.indent();
		s.thenn.accept(this);
		this.unIndent();
		this.sayln("");
		this.printSpaces();
		this.sayln("}else{");
		this.indent();
		s.elsee.accept(this);

		this.unIndent();
		this.printSpaces();
		this.sayln("}");
		return;
	}

	@Override
	public void visit(codegen.C.stm.Print s) {
		this.printSpaces();
		this.say("System_out_println (");
		s.exp.accept(this);
		this.sayln(");");
		return;
	}

	@Override
	public void visit(codegen.C.stm.While s) {
		this.printSpaces();
		this.say("while (");
		s.condition.accept(this);
		this.sayln(")");

		this.printSpaces();
		this.sayln("{");

		this.indent();
		s.body.accept(this);
		this.unIndent();

		this.printSpaces();
		this.sayln("}");
	}

	// type
	@Override
	public void visit(codegen.C.type.Class t) {
		this.say("struct " + t.id + " *");
	}

	@Override
	public void visit(codegen.C.type.Int t) {
		this.say("int");
	}

	@Override
	public void visit(codegen.C.type.IntArray t) {
		this.say("int*  ");
	}

	// dec
	@Override
	public void visit(codegen.C.dec.Dec d) {
		d.type.accept(this);
		this.say(d.id);
	}

	// method
	@Override
	public void visit(codegen.C.method.Method m) {
		String methodName = m.classId + "_" + m.id;

		this.createGCStack(m);
		this.createGCMaps(m);

		this.sayln("void *prev;");// a global pointer

		// Print the method
		m.retType.accept(this);
		this.say(" " + methodName + "(");
		int size = m.formals.size();
		for (codegen.C.dec.T d : m.formals) {
			codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
			size--;
			dec.type.accept(this);
			this.say(" " + dec.id);
			if (size > 0)
				this.say(", ");
		}
		this.sayln(")");
		this.sayln("{");

		this.initialGCStack(m);

		// to put locals of reference types onto the GC stack but others onto
		// the C call stack.
		localNames = new ArrayList<String>();
		lString = m.lString.toString();
		for (codegen.C.dec.T d : m.locals) {
			codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
			if (dec.type instanceof codegen.C.type.Class
					|| dec.type instanceof codegen.C.type.IntArray) {
				// this.sayln("  frame." + dec.id + " = " + dec.id+";");
			} else {
				this.say("  ");
				dec.type.accept(this);
				this.say(" " + dec.id + ";\n");
			}
			localNames.add(dec.id);
		}

		this.sayln("");
		for (codegen.C.stm.T s : m.stms)
			s.accept(this);

		// don't forget to pop off the GC stack frame just before the return
		// statement.
		this.popGCStack();
		this.say("  return ");
		m.retExp.accept(this);
		this.sayln(";");
		this.sayln("}\n");

		return;
	}

	@Override
	public void visit(codegen.C.mainMethod.MainMethod m) {
		// does the main need gc_frame yet?
		this.sayln("struct main_gc_frame{");
		this.sayln("  void *prev;");
		this.sayln("  char *arguments_gc_map;");
		this.sayln("  void *arguments_base_address;");
		this.sayln("  int locals_gc_map;");
		for (codegen.C.dec.T d : m.locals) {
			codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
			if (dec.type instanceof codegen.C.type.Class
					|| dec.type instanceof codegen.C.type.IntArray) {
				this.say("  ");
				dec.type.accept(this);
				this.say(" " + dec.id + ";\n");
			}
		}
		this.sayln("};");
		this.sayln("void *prev;");// a global pointer

		this.sayln("int Tiger_main ()");
		this.sayln("{");
		// for (codegen.C.dec.T dec : m.locals) {
		// this.say("  ");
		// codegen.C.dec.Dec d = (codegen.C.dec.Dec) dec;
		// d.type.accept(this);
		// this.say(" ");
		// this.sayln(d.id + ";");
		// }
		this.sayln("  struct  main_gc_frame frame;\n");
		this.sayln("  frame.prev = prev;");
		this.sayln("  prev = &frame;");
		this.sayln("  frame.arguments_gc_map = NULL;");
		this.sayln("  frame.arguments_base_address = 0;");
		this.sayln("  frame.locals_gc_map = " + m.locals.size() + ";\n");
		m.stm.accept(this);
		this.sayln("");
		this.popGCStack();
		this.sayln("}\n");
		return;
	}

	// vtables
	@Override
	public void visit(codegen.C.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable");
		this.sayln("{");

		// class GC map specifying the number of fields this class has and among
		// those which are references

		this.sayln("  char *" + v.id + "_gc_map;");
		// virtual methods as before
		for (codegen.C.Ftuple t : v.ms) {
			this.say("  ");
			t.ret.accept(this);
			this.sayln(" (*" + t.id + ")();");
		}
		this.sayln("};\n");
		return;
	}

	private void outputVtable(codegen.C.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable " + v.id + "_vtable_ = ");
		this.sayln("{");

		// initialize the class gc map;
		String fString = fStrings.get(v.id);
		if (fString.equals(""))
			this.sayln("  \"\",");
		else
			this.sayln("  \"" + fString + "\",");

		for (codegen.C.Ftuple t : v.ms) {
			this.say("  ");
			this.sayln(t.classs + "_" + t.id + ",");
		}
		this.sayln("};\n");
		return;
	}

	public void declareVtable(codegen.C.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable " + v.id + "_vtable_ ;");
	}

	// class

	@Override
	public void visit(codegen.C.classs.Class c) {
		String fString = "";
		this.sayln("struct " + c.id);
		this.sayln("{");
		// as each struct has a header with four words,we should add them
		// manual;
		this.sayln("  struct " + c.id + "_vtable *vptr;");
		this.sayln("  int isObjOrArray;");
		this.sayln("  int length;");
		this.sayln("  void *forwarding;");

		for (codegen.C.Tuple t : c.decs) {
			if (t.type instanceof codegen.C.type.Class
					|| t.type instanceof codegen.C.type.IntArray)
				fString += "1";
			else
				fString += "0";
			this.say("  ");
			t.type.accept(this);
			this.say(" ");
			this.sayln(t.id + ";");
		}
		fStrings.put(c.id, fString);
		this.sayln("};");
		return;
	}

	// program
	@Override
	public void visit(codegen.C.program.Program p) {
		// we'd like to output to a file, rather than the "stdout".
		try {
			String outputName = null;
			if (Control.outputName != null)
				outputName = Control.outputName;
			else if (Control.fileName != null)
				outputName = "test/ccodeTest/" + Control.fileName + ".c";
			else
				outputName = "a.c";

			this.writer = new java.io.BufferedWriter(
					new java.io.OutputStreamWriter(
							new java.io.FileOutputStream(outputName)));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.sayln("// This is automatically generated by the Tiger compiler.");
		this.sayln("// Do NOT modify!\n");

		this.sayln("#define NULL ((void*)0)\n");
		this.sayln("#include <string.h>");

		this.sayln("// structures");
		for (codegen.C.classs.T c : p.classes) {
			c.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables structures");
		for (codegen.C.vtable.T v : p.vtables) {
			v.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables declared");
		for (codegen.C.vtable.T v : p.vtables) {
			declareVtable((codegen.C.vtable.Vtable) v);
		}
		this.sayln("");

		this.sayln("// methods");
		for (codegen.C.method.T m : p.methods) {
			m.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables");
		for (codegen.C.vtable.T v : p.vtables) {
			outputVtable((codegen.C.vtable.Vtable) v);
		}
		this.sayln("");

		this.sayln("// main method");
		p.mainMethod.accept(this);
		this.sayln("");

		this.say("\n\n");

		try {
			this.writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void createGCMaps(codegen.C.method.Method m) {
		String methodName = m.classId + "_" + m.id;
		// aString and lString are memory GC maps:
		// one GC map for method arguments and another one for method locals.
		this.sayln("char *" + methodName + "_arguments_gc_map=\""
				+ m.aString.toString() + "\";");
		this.sayln("char *" + methodName + "_locals_gc_map=\""
				+ m.lString.toString() + "\";");
	}

	public void createGCStack(codegen.C.method.Method m) {
		String methodName = m.classId + "_" + m.id;
		// Lab 4,exercise 6:define a data structure declaration for f's GC frame
		this.sayln("struct " + methodName + "_gc_frame{");
		// dynamic chain, pointing to f's caller's GC frame
		this.sayln("  void *prev;");
		// should be assigned the value of "f_arguments_gc_map"
		this.sayln("  char *arguments_gc_map;");
		// address of the first argument
		this.sayln("  void *arguments_base_address;");
		// should be assigned the value of "f_locals_gc_map"
		// this.sayln("  char *locals_gc_map;");
		this.sayln("  int locals_gc_map;");
		// remaining fields are method locals
		for (codegen.C.dec.T d : m.locals) {
			codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
			if (dec.type instanceof codegen.C.type.Class
					|| dec.type instanceof codegen.C.type.IntArray) {
				this.say("  ");
				dec.type.accept(this);
				this.say(" " + dec.id + ";\n");
			}
		}
		this.sayln("};");
		// /////////////////////////////////////////////////////////////////
	}

	public void initialGCStack(codegen.C.method.Method m) {
		String methodName = m.classId + "_" + m.id;
		// put the GC stack frame onto the call stack
		// note that this frame contains the original locals in this method
		this.sayln("  struct " + methodName + "_gc_frame frame;\n");

		// push this frame onto the GC stack by setting up "prev"
		this.sayln("  memset(&frame,0,sizeof(frame));");
		this.sayln("  frame.prev = prev;");
		this.sayln("  prev = &frame;");

		// setting up memory GC maps and corresponding base addresses
		this.sayln("  frame.arguments_gc_map = " + methodName
				+ "_arguments_gc_map;");
		this.sayln("  frame.arguments_base_address =&this;");
		// this.sayln("  frame.locals_gc_map = " + methodName +
		// "_locals_gc_map;");
		this.sayln("  frame.locals_gc_map = "
				+ this.countLocalRefs(m.lString.toString()) + ";\n");
	}

	public void popGCStack() {
		this.sayln("  prev=frame.prev;");
		// this.sayln("  frame = NULL ;"); //how to pop off GC stack?
	}

	public int countLocalRefs(String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '1')
				count++;
		}
		return count;
	}
}
