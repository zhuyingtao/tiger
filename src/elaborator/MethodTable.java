package elaborator;

import java.util.Enumeration;

public class MethodTable {
	private java.util.Hashtable<String, ast.type.T> table;
	String mid; //this is my add

	public MethodTable() {
		this.table = new java.util.Hashtable<String, ast.type.T>();
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
		}

		for (ast.dec.T dec : locals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.out.println("duplicated variable: " + decc.id);
				System.exit(1);
			}
			this.table.put(decc.id, decc.type);
		}

	}

	// return null for non-existing keys
	public ast.type.T get(String id) {
		return this.table.get(id);
	}

	public void dump() {
		System.out.println("====== " + mid + " ======");
		Enumeration<String> id = this.table.keys();
		Enumeration<ast.type.T> type = this.table.elements();
		int i = 0;
		while (id.hasMoreElements()) {
			System.out.print(i + " : " + id.nextElement() + "---");
			System.out.println(type.nextElement().toString());
			i++;
		}
		System.out.println("====== End ======");
	}

	@Override
	public String toString() {
		return this.table.toString();
	}
}
