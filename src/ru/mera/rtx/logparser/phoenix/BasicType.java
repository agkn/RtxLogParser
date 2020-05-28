package ru.mera.rtx.logparser.phoenix;

import org.w3c.dom.Element;

public abstract class BasicType extends Type {
    final String mName;
    final String mDescription;

    public BasicType(Element aElement) {
        mDescription = aElement.getAttribute("description");
        mName = aElement.getAttribute("name");
    }

    public BasicType(String aName, String aDescription) {
        mName = aName;
        mDescription = aDescription;
    }

    @Override
    String getTypeId() {
        return mName;
    }
}
