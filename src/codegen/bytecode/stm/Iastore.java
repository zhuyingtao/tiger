package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Takes an int from the stack and stores it in an array of ints. 
//arrayref is a reference to an array of ints. index is an int. 
//value is the int value to be stored in the array. 
//arrayref, index and value are removed from the stack, 
//and value is stored in the array at the given index.
public class Iastore extends T {

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
