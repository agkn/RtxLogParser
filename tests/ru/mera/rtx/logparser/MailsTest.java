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
import java.nio.ByteOrder;

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
    @Test
    public void testOutMail5031() {
        ParserUtils.strToBuffer("31 50 04 00 ff 4f 0a 00 0d 00 02 00 0f 10 00 02 01 05", mBuffer).rewind();
        /*
31 50 # API_CC_INFO_IND
 04 00 # ConEi (ApiCcConEiType)
  ff # ProgressInd (ApiCcProgressIndType)
   4f # "ApiCcSignalType" name="Signal"
    0a 00  # VarLen of array #<parameter type="rsuint8" name="InfoElement" vararraysize="rsuint16" -> ApiIeType
    0d 00  # // member name="API_IE_SYSTEM_CALL_ID" value="0x0D" associatedtype="ApiSystemCallIdListType"
       02  # Info element length.
         # ApiSystemCallIdType
         00  # (ApiCallIdentifierSubType) ApiSubId: API_SUB_CALL_ID
         0f  # (rsuint8)ApiSystemCallId
     10 00 # API_IE_CALL_STATUS
       02 # API_IE_CALL_STATUS length
          # (ApiCallStatusType) ApiCallStatus[1]
         01 (ApiCallStatusSubType) CallStatusSubId:
         05 (ApiCallStatusValueType) CallStatusValue: (ApiCallStatusStateType)API_CSS_CALL_CONNECT or (ApiCallStatusReasonType)??
         */
        mBuffer.order(ByteOrder.LITTLE_ENDIAN);
        short id = mBuffer.getShort();
        Mail mail = mContext.getMail(id);
        assertNotNull(mail);
        mail.print(mContext, mBuffer, 0);
    }

    @Test
    public void testMetaType() {
        ParserUtils.strToBuffer("31 50 04 00 ff ff 0c 00 09 00 09 01 01 0a 04 00 24 02 13 00", mBuffer).rewind();
        /*
3150
 0400 ConEi
 ff ProgressInd
 ff Signal
 0c00 Len
  0900 ApiEscapeToProprietaryType
   09  Len
      01 Discriminator
     01 0a EmcCode
      04 00 len
       24 02 13 00"
         */
        mBuffer.order(ByteOrder.LITTLE_ENDIAN);
        short id = mBuffer.getShort();
        Mail mail = mContext.getMail(id);
        assertNotNull(mail);
        mail.print(mContext, mBuffer, 0);
    }
}
