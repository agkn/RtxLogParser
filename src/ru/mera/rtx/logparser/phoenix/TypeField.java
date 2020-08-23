package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*
@ApiTypes.xml
    <enum name="ApiIeType" type="RSENUM16" description="This type is used to hold information elements received from the DECT protocol" >

      <struct name="ApiInfoElementType" description="This type is used to hold information elements received from the DECT protocol" >
        <member type="ApiIeType" name="Ie" description="Info Element Identification" />
        <member type="rsuint8" name="IeData" vararraysize="rsuint8" vararraylengthname="IeLength" description="The Info Data (InfoLength number
of bytes)." />


@ApiCc.xml
      <mail primitive="API_CC_INFO_IND" id="0x5031" direction="out" description="This mail is used to send various information from FP to PP." >
        <parameter type="ApiCcConEiType" name="ConEi" description="The Instance of the connection defined by the setup_req.
For this call/connection the selected connection instance should be used for following call handling mails" />
        <parameter type="ApiCcProgressIndType" name="ProgressInd" description="Inband information" />
        <parameter type="ApiCcSignalType" name="Signal" description="Alert signal. This is used to request the MMI to start a certain local tone
        <parameter type="rsuint8" name="InfoElement" vararraysize="rsuint16" vararraylengthname="InfoElementLength" metatype="InfoElement" descr

 */
public class TypeField extends BasicType {
    final String mType;
    private final String mMetaType;
    private final String mVarLenName;
    private final String mVarLenSize;

    public TypeField(Element aElement) {
        super(aElement);
        mType = aElement.getAttribute("type");
        mMetaType = aElement.getAttribute("metatype");
        mVarLenName = aElement.getAttribute("vararraylengthname");
        mVarLenSize = aElement.getAttribute("vararraysize");
    }

    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        aContext.outIndent(aIndent);
        if (mVarLenSize.isEmpty()) {
            printType(aContext, aByteBuffer, aIndent);
        } else {
            int len = DataType.valueOf(mVarLenSize.toUpperCase()).readInt(aByteBuffer);
            int limit = aByteBuffer.limit();
            aByteBuffer.limit(aByteBuffer.position() + len);
            while (aByteBuffer.hasRemaining()) {
                printType(aContext, aByteBuffer, aIndent);
            }
            aByteBuffer.limit(limit);
        }
    }

    private void printType(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        if (!mMetaType.isEmpty()) {
            aContext.out().printf("%s (%s)\t// %s", mName, mMetaType, mDescription);
            aContext.resolve(mMetaType).print(aContext, aByteBuffer, aIndent + INDENT);
        } else if (mArraySize > 0) {
            for(int i = 0; i < mArraySize; i++) {
                aContext.out().printf("%s[%d]\t// %s", mName, i, mDescription);
                aContext.resolve(mType).print(aContext, aByteBuffer, aIndent + INDENT);
            }
        } else {
            aContext.out().printf("%s: ", mName);
            aContext.resolve(mType).print(aContext, aByteBuffer, aIndent + INDENT);
        }

    }
}
