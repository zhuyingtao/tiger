package elaborator;

import java.util.Enumeration;
import java.util.Hashtable;

public class MethodTable {
	private java.util.Hashtable<String, ast.type.T> table;
	private java.util.Hashtable<String, Boolean> isUsed;// exercise 9
	String mid; // this is my add

	private Hashtable<String, Integer> varLines;// mark the variable lineNum
												// when it is declared

	public MethodTable() {
		this.table = new java.util.Hashtable<String, ast.type.T>();
		this.isUsed = new java.util.Hashtable<String, Boolean>();
		this.varLines = new Hashtable<String, Integer>();
	}

	// Duplication is not allowed
	public void put(java.util.LinkedList<ast.dec.T> formals,
			java.util.LinkedList<ast.dec.T> locals, String mid) {
		this.mid = mid;
		for (ast.dec.T dec : formals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.out.println("duplicated parameter: " + decc.id);
				System.exit(1);
			}
			this.table.put(decc.id, decc.type);
			// the parameter don't need to check isUsed
		}

		for (ast.dec.T dec : locals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.out.println("duplicated variable: " + decc.id);
				System.exit(1);
			}
			this.table.put(decc.id, decc.type);
			this.isUsed.put(decc.id, false);
			this.varLines.put(decc.id, decc.lineNum);
		}

	}

	// return null for non-existing keys
	public ast.type.T get(String id) {
		ast.type.T type = this.table.get(id);
		if (type != null)
			this.isUsed.put(id, true);
		return type;
	}

	public void dump() {
		System.out.println("====== " + mid + " Method ======");
		Enumeration<String> id = this.table.keys();
		Enumeration<ast.type.T> type = this.table.elements();
		int i = 0;
		while (id.hasMoreElements()) {
			System.out.print(i + " : " + id.nextElement() + "---");
			System.out.println(type.nextElement().toString());
			i++;
		}
		System.out.println("========== End =========");
	}

	@Override
	public String toString() {
		return this.table.toString();
	}

	public void isVariableUsed() {
		Enumeration<String> id = this.isUsed.keys();
		Enumeration<Boolean> use = this.isUsed.elements();
		while (id.hasMoreElements()) {
			String nowId = id.nextElement();
			boolean nowUse = use.nextElement();
			if (!nowUse)
				System.out.println("Warning: the variable ' " + nowId
						+ " ' at line " + this.varLines.get(nowId)
						+ " is never used ! ---- in Method " + mid + "();");
		}
	}
}
