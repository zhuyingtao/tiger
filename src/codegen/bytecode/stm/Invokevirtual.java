package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//invokevirtual <method-spec>
//
//<method-spec> is a method specification. It is a single token made up of three parts
//: a classname, a methodname and a descriptor. e.g.
//
//    java/lang/StringBuffer/charAt(I)C
//
//is the method called "charAt" in the class called "java.lang.StringBuffer",
//and it has the descriptor "(I)C" (i.e. it takes an integer argument and returns a char result).
//In Jasmin, the characters up to the '(' character in <method-spec> 
//form the classname and methodname (the classname is all the characters up to the last '/' character,
//and the methodname is all the characters between the last '/' and the '(' character).
//The characters from '(' to the end of the string are the descriptor.
//This is illustrated in the following diagram:
//
//
//    foo/baz/Myclass/myMethod(Ljava/lang/String;)V
//    ---------------         ---------------------
//          |         --------         |
//          |            |             |
//        classname  methodname    descriptor

//invokevirtual dispatches a Java method. It is used in Java to invoke all methods 
//except interface methods (which use invokeinterface), 
//static methods (which use invokestatic),
//and the few special cases handled by invokespecial.
public class Invokevirtual extends T {
	public String f;
	public String c;
	public java.util.LinkedList<codegen.bytecode.type.T> at;
	public codegen.bytecode.type.T rt;

	public Invokevirtual(String f, String c,
			java.util.LinkedList<codegen.bytecode.type.T> at,
			codegen.bytecode.type.T rt) {
		this.f = f;
		this.c = c;
		this.at = at;
		this.rt = rt;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
