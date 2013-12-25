package cfg.stm;

import cfg.Visitor;

public class Length extends T {
	public String dst;
	// type of the destination variable
	public cfg.type.T ty;
	public cfg.operand.T array;

	public Length(String dst, cfg.type.T ty, cfg.operand.T array) {
		// TODO Auto-generated constructor stub
		this.dst = dst;
		this.ty = ty;
		this.array = array;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
