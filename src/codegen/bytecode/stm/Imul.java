package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pops the top two integers from the operand stack, 
//multiplies them, and pushes the integer result back onto the stack. 
public class Imul extends T {
	public Imul() {
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
