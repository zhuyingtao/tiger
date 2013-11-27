package codegen.C.method;

import codegen.C.Visitor;

public class Method extends T {
	public codegen.C.type.T retType;
	public String classId;
	public String id;
	public java.util.LinkedList<codegen.C.dec.T> formals;
	public java.util.LinkedList<codegen.C.dec.T> locals;
	public java.util.LinkedList<codegen.C.stm.T> stms;
	public codegen.C.exp.T retExp;

	// aString and lString are memory GC maps:
	// one GC map for method arguments and another one for method locals.
	public StringBuffer aString;
	public StringBuffer lString;

	public Method(codegen.C.type.T retType, String classId, String id,
			java.util.LinkedList<codegen.C.dec.T> formals,
			java.util.LinkedList<codegen.C.dec.T> locals,
			java.util.LinkedList<codegen.C.stm.T> stms, codegen.C.exp.T retExp,
			StringBuffer aString, StringBuffer lString) {
		this.retType = retType;
		this.classId = classId;
		this.id = id;
		this.formals = formals;
		this.locals = locals;
		this.stms = stms;
		this.retExp = retExp;
		this.aString = aString;
		this.lString = lString;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
