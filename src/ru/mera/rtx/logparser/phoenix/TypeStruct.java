package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class TypeStruct extends BasicType {
    final LinkedList<TypeField> mMembers = new LinkedList<>();

    public TypeStruct(Element aElement) {
        super(aElement);
        for(Node n: Loader.iterable(aElement.getChildNodes())) {
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            mMembers.add(new TypeField((Element)n));
        }
    }


    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        PrintStream out = aContext.out();
        aContext.out().println();
        aContext.outIndent(aIndent);
        out.println(mName);
        for(TypeField f: mMembers) {
            f.print(aContext, aByteBuffer, aIndent + INDENT);
        }
    }
}
