package ru.mera.rtx.logparser.phoenix;

import java.nio.ByteBuffer;

public class InfoElementType extends BasicType {
    private final TypeEnum mReferType;

    public InfoElementType(TypeEnum aReferType) {
        super("InfoElement", "");
        mReferType = aReferType;
    }

    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        while(aByteBuffer.hasRemaining()) {
    //      <struct name="ApiInfoElementType" description="This type is used to hold information elements received from the DECT protocol" >
    //        <member type="ApiIeType" name="Ie" description="Info Element Identification" />
    //        <member type="rsuint8" name="IeData" vararraysize="rsuint8" vararraylengthname="IeLength" description="The Info Data (InfoLength number
            String id = DataType.RSENUM16.readHex(aByteBuffer);
            int len = DataType.RSUINT8.readInt(aByteBuffer);

            TypeEnum.Member val = mReferType.mMembers.get(id);
            if (val != null) {
                if (val.mAssociatedType.isEmpty()) {
                    aContext.out().print(id + " (" + val.mName + ")");
                } else {
                    aContext.outIndent(aIndent).printf("%s as %s // %s", mName, val.mAssociatedType, mDescription);
                    aContext.resolve(val.mAssociatedType).print(aContext, aByteBuffer, aIndent + INDENT);
                }
            } else {
                aContext.out().printf(" (%s) ", mName);
                aContext.out().print(id);
            }
        }
    }
}
