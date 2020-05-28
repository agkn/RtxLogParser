package ru.mera.rtx.logparser.phoenix;

public enum DataType {
    RSENUM8(1), RSENUM16(2), RSUINT8(1), RSUINT32(4),
    RSENUM32(4), RSUINT16(2), RSBOOL(1), RSINT8(1);

    private final int mBSize;

    DataType(int aBSize) {
        mBSize = aBSize;
    }

    public int getSize() {
        return mBSize;
    }
}
