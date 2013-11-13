package codegen.bytecode.type;

import codegen.bytecode.Visitor;

public class Boolean extends T{

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "@boolean";
	}
	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
