package ru.mera.rtx.logparser.phoenix;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;

public class TypeFieldTest {
    @Test
    public void testArrayField() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        {
            Element e = document.createElement("parameter");
            e.setAttribute("name", "FieldName[10]");
            TypeField field = new TypeField(e);
            assertEquals(field.mName, "FieldName");
            assertEquals(field.mArraySize, 10);
        }
        {
            Element e = document.createElement("parameter");
            e.setAttribute("name", "FieldName[1]");
            TypeField field = new TypeField(e);
            assertEquals(field.mName, "FieldName");
            assertEquals(field.mArraySize, 1);
        }
    }

}