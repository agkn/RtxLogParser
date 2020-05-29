package ru.mera.rtx.logparser;

import org.xml.sax.SAXException;
import ru.mera.rtx.logparser.phoenix.Context;
import ru.mera.rtx.logparser.phoenix.Loader;
import ru.mera.rtx.logparser.phoenix.Mail;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogProcessor {
    //Raw recv: (10)10 00 06 2e 00 01 bd 43 00 2f
    final static Pattern BUFF_PATTERN = Pattern.compile(".+\\((\\d+)\\)\\s*(([0-9a-f]{2}\\s?)+).*");
    //RAW MAIL: 80 42 01 01 00 01 00
    final static Pattern BUFF_OUT_PATTERN = Pattern.compile(".+RAW MAIL:\\s*(([0-9a-f]{2}\\s?)+).*");
    final ByteBuffer mBuffer = ByteBuffer.allocate(100);

    public void process(Reader aInput, PrintStream aOut) throws IOException, ParserConfigurationException, SAXException {
        BufferedReader in = new BufferedReader(aInput);
        Context context = new Context(aOut);


        Loader loader = new Loader(context);
        loader.allFiles(new File("/home/user1/work/dect-ct/14571/xml/"));

        LogEntry entry = new LogEntry();

        String line = in.readLine();
        while (line != null) {
            if (line.length() < 3) {
                line = in.readLine();
                continue;
            }

            if (entry.setup(line)) {
                aOut.println(line);
                handle(entry, context);

            } else {
                // print not recognised lines with "-- " prefix.
                aOut.print("-- ");
                aOut.println(line);
            }
            line = in.readLine();
        }
    }

    private void handle(LogEntry aEntry, Context aContext) {
        // 05-15 15:45:15.071 I/System.out(  845): Raw recv: (10)10 00 06 2e 00 01 bd 43 00 2f
        if ("System.out".equals(aEntry.getModule())) {
            decodeInMail(aEntry, aContext);
        }

        //05-28 12:49:58.958   777   815 I MTX     : AudioInitPcmReq { mId = 0x4280, IsMaster = true, PcmCh0Delay = 0, exception } RAW MAIL: 80 42 01 01 00 01 00
        if ("MTX".equals(aEntry.getModule())) {
            decodeOutMail(aEntry, aContext);
        }

    }

    private void decodeOutMail(LogEntry aEntry, Context aContext) {
        Matcher matcher = BUFF_OUT_PATTERN.matcher(aEntry.getMsg());
        if(!matcher.matches()) {
            return;
        }
        String array = matcher.group(1);
        ParserUtils.strToBuffer(array, mBuffer).rewind();
        if (mBuffer.limit() < 2) {
            return;
        }

        mBuffer.order(ByteOrder.LITTLE_ENDIAN);
        short id = mBuffer.getShort();
        outMail(aContext, id, aEntry);
    }

    private void decodeInMail(LogEntry aEntry, Context aContext) {

        Matcher matcher = BUFF_PATTERN.matcher(aEntry.getMsg());
        if(!matcher.matches()) {
            return;
        }
        int size = Integer.parseInt(matcher.group(1));

        if (size < 8) {
            return;
        }

        String array = matcher.group(2);

        ParserUtils.strToBuffer(array, mBuffer).rewind();
        short id = ParserUtils.getMailId(mBuffer);
        outMail(aContext, id, aEntry);
    }

    private void outMail(Context aContext, short aMailId, LogEntry aEntry) {
        Mail mail = aContext.getMail(aMailId);
        if (mail == null) {
            aContext.out().printf("!!! Can't find mail %s\n", Integer.toHexString(aMailId));
        } else {
            try {
                mail.print(aContext, mBuffer, 0);
            } catch (Exception aE) {
                System.err.println("!!! Can't parse:  " + aEntry + ": " + aE);
            }
        }
    }
}
