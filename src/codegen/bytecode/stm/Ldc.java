package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//ldc pushes a one-word constant onto the operand stack. 
//ldc takes a single parameter, <value>, which is the value to push.
public class Ldc extends T {
	public int i;

	public Ldc(int i) {
		this.i = i;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
