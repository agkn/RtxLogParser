package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class TypeEnum extends BasicType {

    static class Member extends BasicType {
        final String mValue;
        final String mAssociatedType;

        Member(Element aElement) {
            super(aElement);
            mValue = aElement.getAttribute("value");
            mAssociatedType = aElement.getAttribute("associatedtype");
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
        String strVal = mType.readHex(aByteBuffer);
        Member val = mMembers.get(strVal);
        if (val != null) {
            if (val.mAssociatedType.isEmpty()) {
                aContext.out().print(strVal + " (" + val.mName + ")");
            } else {
                aContext.outIndent(aIndent).printf("%s as %s // %s", mName, val.mAssociatedType, mDescription);
                aContext.resolve(val.mAssociatedType).print(aContext, aByteBuffer, aIndent + INDENT);
            }
        } else {
            aContext.out().printf(" (%s) ", mName);
            aContext.out().print(strVal);
        }
    }
}
