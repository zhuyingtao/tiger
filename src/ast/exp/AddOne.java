package ast.exp;

public class AddOne {
	public String id;
	public boolean idFirst;
	public int lineNum;

	public AddOne(String id, boolean idFirst, int lineNum) {
		this.id = id;
		this.idFirst = idFirst;
		this.lineNum = lineNum;
	}

	// @Override
	// public void accept(ast.Visitor v) {
	// v.visit(this);
	// return;
	// }

}
