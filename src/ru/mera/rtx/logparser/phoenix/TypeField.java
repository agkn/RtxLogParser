package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;

import java.nio.ByteBuffer;

public class TypeField extends BasicType {
    final String mType;
    public TypeField(Element aElement) {
        super(aElement);
        mType = aElement.getAttribute("type");
    }

    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        aContext.outIndent(aIndent);
        aContext.out().printf("%s: ", mName);
        aContext.resolve(mType).print(aContext, aByteBuffer, aIndent + INDENT);
        aContext.out().printf("\t// %s\n", mDescription);
    }
}
