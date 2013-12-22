package ast.type;

import java.io.Serializable;

public abstract class T implements ast.Acceptable,Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// boolean: -1
  // int: 0
  // int[]: 1
  // class: 2
  // Such that one can easily tell who is who
  public abstract int getNum();
}
