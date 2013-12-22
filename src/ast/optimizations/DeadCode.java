package ast.optimizations;

// Dead code elimination optimizations on an AST.

public class DeadCode implements ast.Visitor {
	public ast.classs.T newClass;
	public ast.mainClass.T mainClass;
	public ast.program.Program program;

	public DeadCode() {
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
		return;
	}

	@Override
	public void visit(ast.exp.And e) {
		return;
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		return;
	}

	@Override
	public void visit(ast.exp.Call e) {
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
		return;
	}

	@Override
	public void visit(ast.exp.Id e) {
		return;
	}

	@Override
	public void visit(ast.exp.Length e) {
		return;
	}

	@Override
	public void visit(ast.exp.Lt e) {
		return;
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		return;
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
	}

	@Override
	public void visit(ast.exp.Num e) {
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		return;
	}

	@Override
	public void visit(ast.exp.Times e) {
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		return;
	}

	@Override
	public void visit(ast.stm.Block s) {
		for (ast.stm.T stm : s.stms) {
			stm.accept(this);
		}
	}

	@Override
	public void visit(ast.stm.If s) {
		if (s.condition.result != null)
			s.condition = s.condition.result;
		if (s.condition instanceof ast.exp.True) {
			s.condition = null;
			s.elsee = null;
		} else if (s.condition instanceof ast.exp.False) {
			s.condition = null;
			s.thenn = null;
		}
		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		if (s.condition.result != null)
			s.condition = s.condition.result;
		if (s.condition instanceof ast.exp.False) {
			s.condition = null;
			s.body = null;
		}
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
		for (ast.dec.T dec : c.decs) {
			dec.accept(this);
		}

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

		if (control.Control.trace.contains("ast.DeadCode")) {
			System.out
					.println("==============Dead Code Elimination============");
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
