package ru.mera.rtx.logparser.phoenix;

import java.nio.ByteBuffer;

public abstract class Type {
    public static final int INDENT = 4;

    abstract void print(Context aContext, ByteBuffer aByteBuffer, int aIndent);
    abstract String getTypeId();
}
