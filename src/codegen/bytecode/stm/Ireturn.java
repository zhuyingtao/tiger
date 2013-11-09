package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pops an int from the top of the stack and pushes it onto the operand 
//stack of the invoker
public class Ireturn extends T {
	public Ireturn() {
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
