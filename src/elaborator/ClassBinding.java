package elaborator;

import java.util.Hashtable;

public class ClassBinding {
	public String extendss; // null for non-existing extends
	public Hashtable<String, ast.type.T> fields;
	public Hashtable<String, MethodType> methods;
	public Hashtable<String, Boolean> isUsed; // whether the fields
												// have been used

	public Hashtable<String, Integer> fieldLines; // mark the field lineNum when
													// it is declared

	public ClassBinding(String extendss) {
		this.extendss = extendss;
		this.fields = new Hashtable<String, ast.type.T>();
		this.methods = new Hashtable<String, MethodType>();
		this.isUsed = new Hashtable<String, Boolean>();
		this.fieldLines = new Hashtable<String, Integer>();
	}

	public ClassBinding(String extendss,
			java.util.Hashtable<String, ast.type.T> fields,
			java.util.Hashtable<String, MethodType> methods) {
		this.extendss = extendss;
		this.fields = fields;
		this.methods = methods;
	}

	public void put(String xid, ast.type.T type, int lineNum) {
		if (this.fields.get(xid) != null) {
			System.out.println("duplicated class field: " + xid);
			System.exit(1);
		}
		this.fields.put(xid, type);
		this.isUsed.put(xid, false);
		this.fieldLines.put(xid, lineNum);
	}

	public void put(String mid, MethodType mt) {
		if (this.methods.get(mid) != null) {
			System.out.println("duplicated class method: " + mid);
			System.exit(1);
		}
		this.methods.put(mid, mt);
	}

	@Override
	public String toString() {
		System.out.print("extends: ");
		if (this.extendss != null)
			System.out.println(this.extendss);
		else
			System.out.println("<>");
		System.out.println("fields:  ");
		System.out.println(fields.toString());
		System.out.println("methods:  ");
		System.out.println(methods.toString());

		return "";
	}

}
