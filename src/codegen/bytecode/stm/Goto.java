package codegen.bytecode.stm;

import util.Label;
import codegen.bytecode.Visitor;

//Causes execution to branch to the instruction at the address (pc + branchoffset)
public class Goto extends T {
	public Label l;

	public Goto(Label l) {
		this.l = l;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
