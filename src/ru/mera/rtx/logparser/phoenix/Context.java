package ru.mera.rtx.logparser.phoenix;

import java.io.PrintStream;
import java.util.HashMap;

public class Context {
    final PrintStream mOut;
    final HashMap<String, Type> mTypes = new HashMap<>();
    final HashMap<String, Mail> mMailes = new HashMap<>();


    public Context() {
        this(System.out);
    }

    public Context(PrintStream aOut) {
        mOut = aOut;
        addType(new TypeEnum("RsStatusType", "RsStatusType", DataType.RSENUM8));
    }

    public PrintStream out() {
        return mOut;
    }

    public PrintStream outIndent(int aNum) {
        mOut.println();
        for(int i = 0; i < aNum; i++) {
            mOut.print(' ');
        }
        return mOut;
    }

    public void addType(Type aType) {
        String id = aType.getTypeId().toUpperCase();
        if (mTypes.containsKey(id)) {
            System.err.println("Duplicate type: " + id);
        } else {
            mTypes.put(id, aType);
        }
    }

    public Type resolve(String aTypeId) {

        Type type = mTypes.get(aTypeId.toUpperCase());
        if (type != null) {
            return type;
        }
        if ("INFOELEMENT".equals(aTypeId.toUpperCase())) {
            type = new InfoElementType((TypeEnum) mTypes.get("APIIETYPE"));
        } else {
            type = new TypeSimple(DataType.valueOf(aTypeId.toUpperCase()));
        }

        addType(type);
        return type;
    }

    public void addMail(Mail aMail) {
        String id = aMail.getMailId();
        if (mMailes.containsKey(id)) {
            System.err.println("Duplicate mail: " + id);
        } else {
            mMailes.put(id, aMail);
        }
    }

    public Mail getMail(Short aMailId) {
        String id = "0x" + Integer.toHexString(0xffff & aMailId).toUpperCase();
        return mMailes.get(id);
    }
}
