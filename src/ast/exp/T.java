package ast.exp;

import java.io.Serializable;

public abstract class T implements ast.Acceptable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int lineNum;

	public T result; // used for optimistic
}
