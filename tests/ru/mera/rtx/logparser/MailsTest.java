package ru.mera.rtx.logparser;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import ru.mera.rtx.logparser.phoenix.Context;
import ru.mera.rtx.logparser.phoenix.Loader;
import ru.mera.rtx.logparser.phoenix.Mail;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MailsTest {
    final ByteBuffer mBuffer = ByteBuffer.allocate(100);
    private Context mContext;


    @Before
    public void setup() throws ParserConfigurationException, SAXException, IOException {
        Context context = new Context();
        mContext = context;
        Loader loader = new Loader(context);
        loader.allFiles(new File("/home/user1/work/dect-ct/14571/xml/"));
    }

    // primitive="API_RFTEST_GET_CURRENT_POWER_LEVEL_CFM" id="0x43BD"
    @Test
    public void testMail43BD() {
        short id = (short) Integer.parseInt("43BD", 16);
        Mail mail = mContext.getMail(id);
        assertNotNull(mail);
    }

    // 10 00 1e 2a 00 01 31 50 05 00 08 3f 13 00 0d 00 02 00
    @Test
    public void testMail5031() {
        ParserUtils.strToBuffer("10 00 1e 2a 00 01 31 50 05 00 08 3f 13 00 0d 00 02 00", mBuffer).rewind();
        short id = ParserUtils.getMailId(mBuffer);
        Mail mail = mContext.getMail(id);
        assertNotNull(mail);
        mail.print(mContext, mBuffer, 0);
    }
    // Good: 10 00 09 59 00 01 83 42 00 00 00 01 20
    // Bad: 10 00 01 80 80 10 00 06 48 00 01 81 42 00 0c
    //public void test600
}
