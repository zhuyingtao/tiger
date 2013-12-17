package ast.classs;

import java.util.LinkedList;

import ast.Visitor;

public class Interface extends T {
	public String id;
	public String extendss; // null for non-existing "extends"
	public LinkedList<ast.dec.T> decs;
	public LinkedList<ast.method.T> methods;

	public Interface(String id, String extendss, LinkedList<ast.dec.T> decs,
			LinkedList<ast.method.T> methods) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.extendss = extendss;
		this.decs = decs;
		this.methods = methods;
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		v.visit(this);
	}

}
