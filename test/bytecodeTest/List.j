; This is automatically generated by the Tiger compiler.
; Do NOT modify!

.class public List
.super java/lang/Object

.field public elem LElement;
.field public next LList;
.field public end Z
.method public <init>()V
    aload 0
    invokespecial java/lang/Object/<init>()V
    return
.end method


.method public Init()Z
.limit stack 4096
.limit locals 2
    aload 0
    ldc 1
    putfield List/end Z
    ldc 1
    ireturn
.end method
.method public InitNew(LElement;LList;Z)Z
.limit stack 4096
.limit locals 5
    aload 0
    iload 3
    putfield List/end Z
    aload 0
    aload 1
    putfield List/elem LElement;
    aload 0
    aload 2
    putfield List/next LList;
    ldc 1
    ireturn
.end method
.method public Insert(LElement;)LList;
.limit stack 4096
.limit locals 6
    aload 0
    astore 3
    new List
    dup
    invokespecial List/<init>()V
    astore 4
    aload 4
    aload 1
    aload 3
    ldc 0
    invokevirtual List/InitNew(LElement;LList;Z)Z
    istore 2
    aload 4
    areturn
.end method
.method public SetNext(LList;)Z
.limit stack 4096
.limit locals 3
    aload 0
    aload 1
    putfield List/next LList;
    ldc 1
    ireturn
.end method
.method public Delete(LElement;)LList;
.limit stack 4096
.limit locals 12
    aload 0
    astore 2
    ldc 0
    istore 3
    ldc 0
    ldc 1
    isub
    istore 9
    aload 0
    astore 5
    aload 0
    astore 6
    aload 0
    getfield List/end Z
    istore 7
    aload 0
    getfield List/elem LElement;
    astore 8
L_21:
    iload 7
    ldc 1
    ixor
    iload 3
    ldc 1
    ixor
    iand
    ifeq L_20
    aload 1
    aload 8
    invokevirtual Element/Equal(LElement;)Z
    ifeq L_22
    ldc 1
    istore 3
    iload 9
    ldc 0
    if_icmplt L_26
L_27:
    ldc 0
    goto L_28
L_26:
    ldc 1
L_28:
    ifeq L_24
    aload 5
    invokevirtual List/GetNext()LList;
    astore 2
    goto L_25
L_24:
    ldc 0
    ldc 555
    isub
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    aload 6
    aload 5
    invokevirtual List/GetNext()LList;
    invokevirtual List/SetNext(LList;)Z
    istore 4
    ldc 0
    ldc 555
    isub
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
L_25:
    goto L_23
L_22:
    ldc 0
    istore 10
L_23:
    iload 3
    ldc 1
    ixor
    ifeq L_29
    aload 5
    astore 6
    aload 5
    invokevirtual List/GetNext()LList;
    astore 5
    aload 5
    invokevirtual List/GetEnd()Z
    istore 7
    aload 5
    invokevirtual List/GetElem()LElement;
    astore 8
    ldc 1
    istore 9
    goto L_30
L_29:
    ldc 0
    istore 10
L_30:
    goto L_21
L_20:
    aload 2
    areturn
.end method
.method public Search(LElement;)I
.limit stack 4096
.limit locals 8
    ldc 0
    istore 2
    aload 0
    astore 3
    aload 0
    getfield List/end Z
    istore 5
    aload 0
    getfield List/elem LElement;
    astore 4
L_32:
    iload 5
    ldc 1
    ixor
    ifeq L_31
    aload 1
    aload 4
    invokevirtual Element/Equal(LElement;)Z
    ifeq L_33
    ldc 1
    istore 2
    goto L_34
L_33:
    ldc 0
    istore 6
L_34:
    aload 3
    invokevirtual List/GetNext()LList;
    astore 3
    aload 3
    invokevirtual List/GetEnd()Z
    istore 5
    aload 3
    invokevirtual List/GetElem()LElement;
    astore 4
    goto L_32
L_31:
    iload 2
    ireturn
.end method
.method public GetEnd()Z
.limit stack 4096
.limit locals 2
    aload 0
    getfield List/end Z
    ireturn
.end method
.method public GetElem()LElement;
.limit stack 4096
.limit locals 2
    aload 0
    getfield List/elem LElement;
    areturn
.end method
.method public GetNext()LList;
.limit stack 4096
.limit locals 2
    aload 0
    getfield List/next LList;
    areturn
.end method
.method public Print()Z
.limit stack 4096
.limit locals 5
    aload 0
    astore 1
    aload 0
    getfield List/end Z
    istore 2
    aload 0
    getfield List/elem LElement;
    astore 3
L_36:
    iload 2
    ldc 1
    ixor
    ifeq L_35
    aload 3
    invokevirtual Element/GetAge()I
    getstatic java/lang/System/out Ljava/io/PrintStream;
    swap
    invokevirtual java/io/PrintStream/println(I)V
    aload 1
    invokevirtual List/GetNext()LList;
    astore 1
    aload 1
    invokevirtual List/GetEnd()Z
    istore 2
    aload 1
    invokevirtual List/GetElem()LElement;
    astore 3
    goto L_36
L_35:
    ldc 1
    ireturn
.end method
