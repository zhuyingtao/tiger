package codegen.bytecode.stm;

import util.Label;
import codegen.bytecode.Visitor;

//if_icmplt pops the top two ints off the stack and compares them. 
//If value2 is less than value1, 
//execution branches to the address (pc + branchoffset)
public class Ificmplt extends T {
	public Label l;

	public Ificmplt(Label l) {
		this.l = l;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
