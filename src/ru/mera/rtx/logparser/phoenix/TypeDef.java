package ru.mera.rtx.logparser.phoenix;


import org.w3c.dom.Element;

import java.awt.image.ImageConsumer;
import java.nio.ByteBuffer;

public class TypeDef extends BasicType {
    final String mType;

    public TypeDef(Element aElement) {
        super(aElement);
        mType = aElement.getAttribute("type");
    }

    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        aContext.out().printf("(%s)", mName);
        aContext.resolve(mType).print(aContext, aByteBuffer, aIndent + INDENT);
    }
}
