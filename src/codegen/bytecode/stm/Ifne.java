package codegen.bytecode.stm;

import util.Label;
import codegen.bytecode.Visitor;

//ifne pops the top int off the operand stack. 
//If the int does not equal zero, execution branches to the address (pc + branchoffset)
public class Ifne extends T {
	public Label l;

	public Ifne(Label l) {
		this.l = l;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
