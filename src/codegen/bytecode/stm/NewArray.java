package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class NewArray extends T {

	public NewArray() {
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
