package cfg.stm;

import cfg.Visitor;

public class MoveArray extends T {
	public String dst;
	// type of the destination variable
	public cfg.type.T ty;
	public cfg.operand.T index;
	public cfg.operand.T exp;

	public boolean dstIsField;

	public MoveArray(String dst, cfg.type.T ty, cfg.operand.T index,
			cfg.operand.T exp) {
		this.dst = dst;
		this.ty = ty;
		this.index = index;
		this.exp = exp;
	}

	public MoveArray(String dst, cfg.type.T ty, cfg.operand.T index,
			cfg.operand.T exp, boolean dstIsField) {
		// TODO Auto-generated constructor stub
		this(dst, ty, index, exp);
		this.dstIsField = dstIsField;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}
}
