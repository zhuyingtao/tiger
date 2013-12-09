package ast.type;

public abstract class T implements ast.Acceptable
{
  // boolean: -1
  // int: 0
  // int[]: 1
  // class: 2
  // Such that one can easily tell who is who
  public boolean isFinal;
  public abstract int getNum();
}
