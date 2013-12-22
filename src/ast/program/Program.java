package ast.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ast.Visitor;

public class Program extends T {

	public ast.mainClass.T mainClass;
	public java.util.LinkedList<ast.classs.T> classes;

	public Program(ast.mainClass.T mainClass,
			java.util.LinkedList<ast.classs.T> classes) {
		this.mainClass = mainClass;
		this.classes = classes;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
		return;
	}

	public Object copy() {
		Object o = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			o = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
}
