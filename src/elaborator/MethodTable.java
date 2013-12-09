package elaborator;

import java.util.Iterator;
import java.util.Map.Entry;

import ast.type.T;

class VarInfo {
	ast.type.T type;
	boolean isUsed;
	boolean isFormal;
	int varLine;
}

public class MethodTable {

	private java.util.LinkedHashMap<String, ast.type.T> table;
	private java.util.LinkedHashMap<String, ast.exp.Id> table2;

	private java.util.LinkedHashMap<String, Boolean> isUsed;// exercise 9
	// mark the variable lineNum when it is declared
	private java.util.LinkedHashMap<String, Integer> varLines;
	private java.util.LinkedList<String> formalTable;

	String mid; // mark the name of the method

	// use this structure may be more reasonable,but I am lazy to do that...
	// private java.util.LinkedHashMap<String, VarInfo> varTable;

	public MethodTable() {
		this.table = new java.util.LinkedHashMap<String, ast.type.T>();
		this.table2 = new java.util.LinkedHashMap<String, ast.exp.Id>();
		this.isUsed = new java.util.LinkedHashMap<String, Boolean>();
		this.varLines = new java.util.LinkedHashMap<String, Integer>();
		this.formalTable = new java.util.LinkedList<String>();

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
			this.table2.put(decc.id, decc.idRef);
			// the parameter don't need to check isUsed
			this.formalTable.add(decc.id);
		}

		for (ast.dec.T dec : locals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.out.println("duplicated variable: " + decc.id);
				System.exit(1);
			}
			this.table.put(decc.id, decc.type);
			this.table2.put(decc.id, decc.idRef);
			this.isUsed.put(decc.id, false);
			this.varLines.put(decc.id, decc.lineNum);
		}

	}

	// return null for non-existing keys
	public ast.type.T getType(String id) {
		ast.type.T type = this.table.get(id);
		if (type != null)
			this.isUsed.put(id, true);
		return type;
	}

	public ast.exp.Id getIdRef(String id) {
		ast.exp.Id idRef = this.table2.get(id);
		return idRef;
	}

	public void dump() {
		System.out.println("====== " + mid + " Method ======");
		Iterator<Entry<String, T>> iterator = this.table.entrySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			Entry<String, ast.type.T> entry = iterator.next();
			System.out.print(i + " : " + entry.getKey() + "---");
			System.out.println(entry.getValue().toString());
			i++;
		}
		System.out.println("========== End =========\n");
	}

	@Override
	public String toString() {
		return this.table.toString();
	}

	public void isVariableUsed() {
		Iterator<Entry<String, Boolean>> iterator = this.isUsed.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Boolean> entry = iterator.next();
			String nowId = entry.getKey();
			boolean nowUse = entry.getValue();
			if (!nowUse)
				System.out.println("Warning: the variable ' " + nowId
						+ " ' at line " + this.varLines.get(nowId)
						+ " is never used ! ---- in Method " + mid + "();");
		}
	}

	public boolean isArgument(String id) {
		boolean flag = false;
		if (this.formalTable.contains(id))
			flag = true;
		return flag;
	}
}
