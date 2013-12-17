package ast.dec;

import ast.Visitor;
import ast.exp.Id;

public class Dec extends T {
	public ast.type.T type;
	public String id;
	public ast.stm.Assign assign; // used only for print;

	public int lineNum;

	public ast.exp.Id idRef; // used for check the id's initialization;

	public Dec(ast.type.T type, String id) {
		this.type = type;
		this.id = id;
		this.idRef = new Id(id, type, false);
	}

	public Dec(ast.type.T type, String id, int lineNum) {
		this(type, id);
		this.lineNum = lineNum;
	}

	public void initial(ast.stm.Assign assign) {
		this.assign = assign;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
