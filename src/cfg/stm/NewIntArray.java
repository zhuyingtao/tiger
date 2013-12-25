package cfg.stm;

import cfg.Visitor;

public class NewIntArray extends T {
	public String dst;
	// type of the destination variable
	public cfg.type.T ty;
	public cfg.operand.T exp;

	public NewIntArray(String dst, cfg.type.T ty, cfg.operand.T exp) {
		// TODO Auto-generated constructor stub
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
