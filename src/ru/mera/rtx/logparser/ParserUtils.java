package ru.mera.rtx.logparser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ParserUtils {
    public static ByteBuffer strToBuffer(String aInStr, ByteBuffer aOutBuffer) {
        aOutBuffer.clear();
        for(String num: aInStr.split(" ")) {
            aOutBuffer.put((byte) Integer.parseInt(num, 16));
        }
        aOutBuffer.limit(aOutBuffer.position());
        return aOutBuffer;
    }

    public static short getMailId(ByteBuffer aBuffer) {
        // // Bad: 10 00 01 80 80 10 00 06 48 00 01 81 42 00 0c

        // Loop until we find the frame start byte

        byte ch;
        do
        {
            ch = aBuffer.get();
        }
        while (ch != 0x10 && aBuffer.hasRemaining());

        if (aBuffer.remaining() < 2) {
            return 0;
        }

        aBuffer.order(ByteOrder.BIG_ENDIAN);
        short len = aBuffer.getShort(); // 10 00
        if (len < 3) {
            aBuffer.position(aBuffer.position() + len);
            return getMailId(aBuffer);
        }

        aBuffer.get(); // Checksum
        aBuffer.getShort(); // header
        aBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return aBuffer.getShort();
    }
}
