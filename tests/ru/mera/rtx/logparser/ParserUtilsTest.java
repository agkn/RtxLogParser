package ru.mera.rtx.logparser;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class ParserUtilsTest {

    @Test
    public void testGetMailId() {
        // 10 00 01 80 80 10 00 06 48 00 01 81 42 00 0c
        ByteBuffer buff = ByteBuffer.allocate(100);
        ParserUtils.strToBuffer("10 00 01 80 80 10 00 06 48 00 01 81 42 00 0c", buff).rewind();
        short id = ParserUtils.getMailId(buff);
        assertEquals(0x4281, id);

        //
        ParserUtils.strToBuffer("10 00 01 81 81", buff).rewind();
        id = ParserUtils.getMailId(buff);
        assertEquals(0, id);

        // 10 00 09 1d 00 01 83 42 00 00 00 01 e4

        ParserUtils.strToBuffer("10 00 09 1d 00 01 83 42 00 00 00 01 e4", buff).rewind();
        id = ParserUtils.getMailId(buff);
        assertEquals(0x4283, id);
    }
}