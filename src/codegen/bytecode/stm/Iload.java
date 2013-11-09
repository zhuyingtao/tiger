package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pushes the int value held in a local variable onto the operand stack.
public class Iload extends T {
	public int index;

	public Iload(int index) {
		this.index = index;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
