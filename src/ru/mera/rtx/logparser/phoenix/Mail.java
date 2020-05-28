package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Mail {
    private final String mDescription;
    private final String mPrimitive;
    final LinkedList<TypeField> mMembers = new LinkedList<>();
    private final String mId;

    public Mail(Element aElement) {
        mDescription = aElement.getAttribute("description");
        mPrimitive = aElement.getAttribute("primitive");
        mId = aElement.getAttribute("id");

        for(Node n: Loader.iterable(aElement.getChildNodes())) {
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            mMembers.add(new TypeField((Element)n));
        }
    }

    public void print(Context aContext, ByteBuffer aByteBuffer, int aIndent) {
        PrintStream out = aContext.out();
        aContext.out().println();
        aContext.outIndent(aIndent);
        out.printf("# %s\n%s (%s)\n", mDescription, mPrimitive, mId);
        for(TypeField f: mMembers) {
            f.print(aContext, aByteBuffer, aIndent + Type.INDENT);
        }
        out.println();
    }

    public String getMailId() {
        return mId;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "mPrimitive='" + mPrimitive + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }
}
