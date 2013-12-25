package cfg.stm;

import cfg.Visitor;

public class ArraySelect extends T {
	public String dst;
	// type of the destination variable
	public cfg.type.T ty;
	public cfg.operand.T array;
	public cfg.operand.T index;

	public ArraySelect(String dst, cfg.type.T ty, cfg.operand.T array,
			cfg.operand.T index) {
		// TODO Auto-generated constructor stub
		this.dst = dst;
		this.ty = ty;
		this.array = array;
		this.index = index;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
