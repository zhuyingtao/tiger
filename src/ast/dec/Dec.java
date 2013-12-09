package ast.dec;

import ast.Visitor;
import ast.exp.Id;

public class Dec extends T {
	public ast.type.T type;
	public String id;

	public int lineNum;

	public ast.exp.Id idRef;

	public Dec(ast.type.T type, String id) {
		this.type = type;
		this.id = id;
		this.idRef = new Id(id, type, false);
	}

	public Dec(ast.type.T type, String id, int lineNum) {
		this(type, id);
		this.lineNum = lineNum;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
