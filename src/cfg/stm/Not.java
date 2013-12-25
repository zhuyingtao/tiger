package cfg.stm;

import cfg.Visitor;

public class Not extends T {
	public String dst;
	// type of the destination variable
	public cfg.type.T ty;
	public cfg.operand.T exp;

	public Not(String dst, cfg.type.T ty, cfg.operand.T exp) {
		this.dst = dst;
		this.ty = ty;
		this.exp = exp;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
}
