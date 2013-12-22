package ast.optimizations;

// Algebraic simplification optimizations on an AST.

public class AlgSimp implements ast.Visitor {
	public ast.classs.T newClass;
	public ast.mainClass.T mainClass;
	public ast.program.Program program;

	public AlgSimp() {
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

		if (e.left instanceof ast.exp.Num) {
			if (((ast.exp.Num) e.left).num == 0)
				e.result = e.right;
		}
		if (e.right instanceof ast.exp.Num) {
			if (((ast.exp.Num) e.right).num == 0)
				e.result = e.left;
		}
	}

	@Override
	public void visit(ast.exp.And e) {
		e.left.accept(this);
		e.right.accept(this);
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		e.index.accept(this);
		e.array.accept(this);
	}

	@Override
	public void visit(ast.exp.Call e) {
		e.exp.accept(this);
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
	}

	@Override
	public void visit(ast.exp.Lt e) {
		e.left.accept(this);
		e.right.accept(this);
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

		if (e.right instanceof ast.exp.Num) {
			if (((ast.exp.Num) e.right).num == 0) {
				e.result = e.left;
			}
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

		if (e.left instanceof ast.exp.Num) {
			if (((ast.exp.Num) e.left).num == 0) {
				e.result = new ast.exp.Num(0);
			} else if (((ast.exp.Num) e.left).num == 1) {
				e.result = e.right;
			}
		}

		if (e.right instanceof ast.exp.Num) {
			if (((ast.exp.Num) e.right).num == 0) {
				e.result = new ast.exp.Num(0);
			} else if (((ast.exp.Num) e.right).num == 1) {
				e.result = e.left;
			}
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
		s.index.accept(this);
		s.exp.accept(this);
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

		if (control.Control.trace.contains("ast.AlgSimp")) {
			System.out.println("==============Algebraic Simplification"
					+ " Elimination============");
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
