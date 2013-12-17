package ast.classs;

import java.util.LinkedList;

import ast.Visitor;

public class Class extends T {
	public String id;
	public String extendss; // null for non-existing "extends"
	public LinkedList<String> implementss;
	public LinkedList<ast.dec.T> decs;
	public LinkedList<ast.method.T> methods;

	public boolean isFinal;

	public Class(String id, String extendss, LinkedList<String> implementss,
			LinkedList<ast.dec.T> decs, LinkedList<ast.method.T> methods) {
		this.id = id;
		this.extendss = extendss;
		this.implementss = implementss;
		this.decs = decs;
		this.methods = methods;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
