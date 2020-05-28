package ru.mera.rtx.logparser.phoenix;

import java.nio.ByteBuffer;

public class TypeSimple extends Type {
    final DataType mType;

    public TypeSimple(DataType aType) {
        mType = aType;
    }

    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        aContext.out().print("0x");
        for(int i = 0; i < mType.getSize(); i++) {
            aContext.out().printf("%02X", 0xff & aByteBuffer.get());
        }
    }

    @Override
    String getTypeId() {
        return mType.name().toUpperCase();
    }
}
