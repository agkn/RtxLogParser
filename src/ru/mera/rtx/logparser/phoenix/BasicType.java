package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;

import java.util.regex.Pattern;

public abstract class BasicType extends Type {
    final String mName;
    final String mDescription;
    final int mArraySize;

    public BasicType(Element aElement) {
        mDescription = aElement.getAttribute("description");
        String name = aElement.getAttribute("name");
        int pos = name.indexOf('[');
        if (pos < 0) {
            mName = name;
            mArraySize = 0;
        } else {
            mName = name.substring(0, pos);
            mArraySize = extractSize(name.substring(pos + 1, name.length() - 1));
        }
    }

    private int extractSize(String aStrSize) {
        try {
            return Integer.parseInt(aStrSize);
        } catch (NumberFormatException aE) {
            //TODO lookup defines
            return 1;
        }
    }

    public BasicType(String aName, String aDescription) {
        mDescription = aDescription;
        int pos = aName.indexOf('[');
        if (pos < 0) {
            mName = aName;
            mArraySize = 0;
        } else {
            mName = aName.substring(0, pos);
            mArraySize = Integer.parseInt(aName.substring(pos + 1));
        }
    }

    @Override
    String getTypeId() {
        return mName;
    }
}
