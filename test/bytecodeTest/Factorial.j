; This is automatically generated by the Tiger compiler.
; Do NOT modify!

.class public Factorial
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
    .limit stack 4096
    .limit locals 2
    new Fac
    dup
    invokespecial Fac/<init>()V
    ldc 10
    invokevirtual Fac/ComputeFac(I)I
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    return
.end method
