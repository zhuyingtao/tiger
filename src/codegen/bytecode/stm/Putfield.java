package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class Putfield extends T {
	
	public String fieldLoc;
	public String fieldType;

	public Putfield(String location, String type) {
		this.fieldLoc = location;
		this.fieldType = type;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
