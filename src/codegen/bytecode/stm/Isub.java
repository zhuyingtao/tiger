package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pops two ints off the operand stack, 
//subtracts the top one from the second (i.e. computes value2 - value1),
public class Isub extends T {
	public Isub() {
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
