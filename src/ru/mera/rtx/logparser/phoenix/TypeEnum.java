package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class TypeEnum extends BasicType {

    static class Member extends BasicType {
        final String mValue;

        Member(Element aElement) {
            super(aElement);
            mValue = aElement.getAttribute("value");
        }

        @Override
        void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {

        }
    }
    final DataType mType;
    final HashMap<String, Member> mMembers = new HashMap<>();

    public TypeEnum(Element aElement) {
        super(aElement);
        DataType type;
        type = DataType.valueOf(aElement.getAttribute("type").toUpperCase());
        mType = type;

        for(Node n: Loader.iterable(aElement.getChildNodes())) {
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Member m = new Member((Element) n);
            mMembers.put(m.mValue, m);
        }
    }

    public TypeEnum(String aName, String aDescription, DataType aType) {
        super(aName, aDescription);
        mType = aType;
    }

    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        aContext.outIndent(aIndent);
        aContext.out().printf("%s: ", mName);

        StringBuilder out = new StringBuilder("0x");
        for(int i = 0; i < mType.getSize(); i++) {
            out.append(String.format("%02X", aByteBuffer.get()));
        }
        String strVal = out.toString();
        Member val = mMembers.get(strVal);
        if (val != null) {
            aContext.out().print(val.mName + "(" + strVal + ")");
        } else {
            aContext.out().print(strVal);
        }
    }
}
