package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

//Pops an int off the stack and stores it in local variable <varnum>.
//and pushes the int result back onto the stack.
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
