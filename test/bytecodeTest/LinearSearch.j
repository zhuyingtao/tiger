; This is automatically generated by the Tiger compiler.
; Do NOT modify!

.class public LinearSearch
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
    .limit stack 4096
    .limit locals 2
    new LS
    dup
    invokespecial LS/<init>()V
    ldc 10
    invokevirtual LS/Start(I)I
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    return
.end method
