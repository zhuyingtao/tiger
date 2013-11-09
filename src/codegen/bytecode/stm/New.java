package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//new is used to create object instances.
//new takes a single parameter, <class>,
//the name of the class of object you want to create. 
//<class> is resolved into a Java class
public class New extends T {
	public String c;

	public New(String c) {
		this.c = c;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
