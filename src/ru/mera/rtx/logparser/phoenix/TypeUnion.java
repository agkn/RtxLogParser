package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class TypeUnion extends TypeStruct {

    public TypeUnion(Element aElement) {
        super(aElement);
    }
    @Override
    void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        PrintStream out = aContext.out();
        aContext.outIndent(aIndent);
        out.printf("%s (Union) // %s", mName, mDescription);
        int pos = aByteBuffer.position();
        for(TypeField f: mMembers) {
            aByteBuffer.position(pos);
            f.print(aContext, aByteBuffer, aIndent + INDENT);
        }
    }
}
