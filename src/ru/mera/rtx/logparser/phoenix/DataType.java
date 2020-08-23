package ru.mera.rtx.logparser.phoenix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public enum DataType {
    RSENUM8(1),
    RSBOOL(1),
    RSINT8(1),
    RSUINT8(1),
    RSENUM16(2),
    RSUINT16(2),
    RSUINT32(4),
    RSENUM32(4);

    private final int mBSize;

    DataType(int aBSize) {
        mBSize = aBSize;
    }

    public int getSize() {
        return mBSize;
    }
    public int readInt(ByteBuffer aByteBuffer) {
        aByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        if (mBSize == 1) {
            return aByteBuffer.get() & 0xff;
        } else if (mBSize == 2) {
            return aByteBuffer.getShort() & 0xffff;
        } else  if (mBSize == 4) {
            return aByteBuffer.getInt();
        }

        throw new IllegalArgumentException();
    }

    public String readHex(ByteBuffer aByteBuffer) {
        return String.format("0x%02X", readInt(aByteBuffer));
    }
}
