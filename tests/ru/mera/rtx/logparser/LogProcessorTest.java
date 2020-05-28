package ru.mera.rtx.logparser;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class LogProcessorTest {

    @Test
    public void testBufferRegex() {
        String str = "Raw recv: (10)10 00 06 2e 00 01 bd 43 00 2f";
        Pattern p = LogProcessor.BUFF_PATTERN;
        Matcher matcher = p.matcher(str);
        assertTrue(matcher.matches());

        int size = Integer.valueOf(matcher.group(1));
        String array = matcher.group(2);
        assertEquals(size, 10);
        assertEquals(array, "10 00 06 2e 00 01 bd 43 00 2f");

        ByteBuffer buff = ByteBuffer.allocate(100);
        for(String num: array.split(" ")) {
            buff.put((byte) Integer.parseInt(num, 16));
        }
        buff.rewind();
        assertEquals(0x10, buff.get());
    }

}