package ru.mera.rtx.logparser;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogEntryTest {
    @Test
    public void setupOk() {
        LogEntry entry = new LogEntry();
        //String string = "08-17 12:41:14.677 V/Unite Service(  993): onHandleUsdRequest";
        String string = "5-15 15:45:15.071 I/System.out(  845): Raw recv: (10)10 00 06 2e 00 01 bd 43 00 2f";
        assertTrue(entry.setup(string));
        assertEquals(5, entry.getMonth());
        assertEquals(15, entry.getDayOfMonth());
        assertEquals(15, entry.getHour());
        assertEquals(45, entry.getMin());
        assertEquals(15, entry.getSec());
        assertEquals(71, entry.getMsec());
        assertEquals("I", entry.getVerbosity());
        assertEquals("System.out", entry.getModule());
        assertEquals(845, entry.getPid());
        assertEquals("Raw recv: (10)10 00 06 2e 00 01 bd 43 00 2f", entry.getMsg());
    }

}