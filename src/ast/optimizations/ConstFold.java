package ast.optimizations;

// Constant folding optimizations on an AST.

public class ConstFold implements ast.Visitor {
	public ast.classs.T newClass;
	public ast.mainClass.T mainClass;
	public ast.program.Program program;

	public ConstFold() {
		this.newClass = null;
		this.mainClass = null;
		this.program = null;
	}

	// //////////////////////////////////////////////////////
	//
	public String genId() {
		return util.Temp.next();
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(ast.exp.Add e) {
		if (e.left.result != null)
			e.left = e.left.result;
		if (e.right.result != null)
			e.right = e.right.result;

		if (e.left instanceof ast.exp.Num && e.right instanceof ast.exp.Num) {
			int left = ((ast.exp.Num) e.left).num;
			int right = ((ast.exp.Num) e.right).num;
			int sum = left + right;
			e.result = new ast.exp.Num(sum);
		}
	}

	@Override
	public void visit(ast.exp.And e) {
		if (e.left.result != null)
			e.left = e.left.result;
		if (e.right.result != null)
			e.right = e.right.result;

		if (e.left instanceof ast.exp.True && e.right instanceof ast.exp.True)
			e.result = new ast.exp.True();
		else if (e.left instanceof ast.exp.True
				&& e.right instanceof ast.exp.False)
			e.result = new ast.exp.False();
		else if (e.left instanceof ast.exp.False
				&& e.right instanceof ast.exp.True)
			e.result = new ast.exp.False();
		else if (e.left instanceof ast.exp.False
				&& e.right instanceof ast.exp.False)
			e.result = new ast.exp.False();

	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		e.array.accept(this);
		e.index.accept(this);
	}

	@Override
	public void visit(ast.exp.Call e) {
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
	}

	@Override
	public void visit(ast.exp.Id e) {
		return;
	}

	@Override
	public void visit(ast.exp.Length e) {
		e.array.accept(this);
	}

	@Override
	public void visit(ast.exp.Lt e) {
		if (e.left.result != null)
			e.left = e.left.result;
		if (e.right.result != null)
			e.right = e.right.result;

		if (e.left instanceof ast.exp.Num && e.right instanceof ast.exp.Num) {
			int left = ((ast.exp.Num) e.left).num;
			int right = ((ast.exp.Num) e.right).num;
			if (left < right)
				e.result = new ast.exp.True();
			else
				e.result = new ast.exp.False();
		}
		return;
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
		if (e.exp.result != null)
			e.exp = e.exp.result;

		if (e.exp instanceof ast.exp.True)
			e.result = new ast.exp.False();
		else if (e.exp instanceof ast.exp.False)
			e.result = new ast.exp.True();
	}

	@Override
	public void visit(ast.exp.Num e) {
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		if (e.left.result != null)
			e.left = e.left.result;
		if (e.right.result != null)
			e.right = e.right.result;

		if (e.left instanceof ast.exp.Num && e.right instanceof ast.exp.Num) {
			int left = ((ast.exp.Num) e.left).num;
			int right = ((ast.exp.Num) e.right).num;
			int sum = left - right;
			e.result = new ast.exp.Num(sum);
		}
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		return;
	}

	@Override
	public void visit(ast.exp.Times e) {
		if (e.left.result != null)
			e.left = e.left.result;
		if (e.right.result != null)
			e.right = e.right.result;

		if (e.left instanceof ast.exp.Num && e.right instanceof ast.exp.Num) {
			int left = ((ast.exp.Num) e.left).num;
			int right = ((ast.exp.Num) e.right).num;
			int sum = left * right;
			e.result = new ast.exp.Num(sum);
		}
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		s.exp.accept(this);
		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		s.exp.accept(this);
		s.index.accept(this);
	}

	@Override
	public void visit(ast.stm.Block s) {
		for (ast.stm.T stm : s.stms) {
			stm.accept(this);
		}
	}

	@Override
	public void visit(ast.stm.If s) {
		if (s.condition != null)
			s.condition.accept(this);
		if (s.elsee != null)
			s.elsee.accept(this);
		if (s.thenn != null)
			s.thenn.accept(this);
		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		s.exp.accept(this);
		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		if (s.condition != null)
			s.condition.accept(this);
		if (s.body != null)
			s.body.accept(this);
	}

	// type
	@Override
	public void visit(ast.type.Boolean t) {
	}

	@Override
	public void visit(ast.type.Class t) {
	}

	@Override
	public void visit(ast.type.Int t) {
	}

	@Override
	public void visit(ast.type.IntArray t) {
	}

	// dec
	@Override
	public void visit(ast.dec.Dec d) {
		return;
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		for (ast.stm.T stm : m.stms) {
			stm.accept(this);
		}
		return;
	}

	// class
	@Override
	public void visit(ast.classs.Class c) {
		for (ast.method.T method : c.methods) {
			method.accept(this);
		}
		return;
	}

	// main class
	@Override
	public void visit(ast.mainClass.MainClass c) {
		c.stm.accept(this);
		return;
	}

	// program
	@Override
	public void visit(ast.program.Program p) {

		// You should comment out this line of code:
		// this.program = p;
		this.program = (ast.program.Program) (p.copy());
		this.program.mainClass.accept(this);
		for (ast.classs.T classs : this.program.classes) {
			classs.accept(this);
		}

		if (control.Control.trace.contains("ast.ConstFold")) {
			System.out.println("==============Constant"
					+ " Folding============");
			System.out.println("before optimization:");
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			p.accept(pp);
			System.out.println("after optimization:");
			this.program.accept(pp);
			System.out.println("==============End============");
		}
		return;
	}
}
