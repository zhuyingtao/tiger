package cfg.stm;

import java.util.ArrayList;

import cfg.Visitor;

public class Block extends T {

	public ArrayList<Object> stmOrTransfer;

	public Block(ArrayList<Object> stmOrTransfer) {
		// TODO Auto-generated constructor stub
		this.stmOrTransfer = stmOrTransfer;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub

	}

}
