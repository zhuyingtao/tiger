package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//areturn pops objectref off the stack 
//and pushes it onto the operand stack of the invoker
public class Areturn extends T {
	public Areturn() {
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
