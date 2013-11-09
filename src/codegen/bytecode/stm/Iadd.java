package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class Iadd extends T {

	public Iadd() {
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
