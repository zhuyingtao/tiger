package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pops objectref (a reference to an object or array) off the stack 
//and stores it in local variable <varnum>.
public class Astore extends T {
	public int index;

	public Astore(int index) {
		this.index = index;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
