package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pops an integer off the stack and stores it in local variable <varnum>.
public class Istore extends T
{
  public int index;

  public Istore(int index)
  {
    this.index = index;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
