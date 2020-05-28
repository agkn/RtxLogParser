package ru.mera.rtx.logparser.phoenix;

import com.sun.istack.internal.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Loader {
    private static final String TAG_GROUP = "group";
    final Context mContext;

    public Loader(Context aContext) {
        mContext = aContext;
    }

    public Loader() {
        this(new Context());
    }


    public void process() {

    }
    public static Iterable<Node> iterable(final NodeList nodeList) {
        return () -> new Iterator<Node>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < nodeList.getLength();
            }

            @Override
            public Node next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return nodeList.item(index++);
            }
        };
    }

    public void load(File aFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(aFile);
        processGroups((Element) document.getElementsByTagName(TAG_GROUP).item(0));
    }

    private void processGroups(Element aItem) {
        // System.out.println("Group: " + aItem.getAttribute("name"));

        // Types
        for(Node tt : iterable(aItem.getElementsByTagName("types"))) {
            for(Node t: iterable(tt.getChildNodes())) {
                if (t.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                switch (t.getNodeName()) {
                    case "typedef": addType(new TypeDef((Element)t)); break;
                    case "enum": addType(new TypeEnum((Element)t)); break;
                    case "struct": addType(new TypeStruct((Element)t)); break;
                    default: System.err.printf("Unsupported type %s\n", t.getNodeName());
                }
            }
        }

        // Mails
        for(Node tt : iterable(aItem.getElementsByTagName("mails"))) {
            for(Node t: iterable(tt.getChildNodes())) {
                if (t.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Mail mail = new Mail((Element) t);
                //System.out.println("Mail: " + mail);
                addMail(mail);
            }
        }
    }

    private void addMail(Mail aMail) {
        mContext.addMail(aMail);
    }

    private void addType(Type aType) {
        mContext.addType(aType);
    }


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
/*
        Loader loader = new Loader();
        loader.load(new File("/home/user1/work/dect-ct/14571/xml/ApiAudioModule.xml"));
*/
        Loader loader = new Loader();
        loader.allFiles(new File("/home/user1/work/dect-ct/14571/xml/"));
    }

    public void allFiles(@NotNull File aFile) throws IOException, SAXException, ParserConfigurationException {
        for(String file: aFile.list()) {
            load(new File(aFile.getAbsolutePath(), file));
        }
    }

    private void check() {
        String typeId = "ApiLdsTxDoneEventParameterType";
        ByteBuffer buff = ByteBuffer.wrap(new byte[] {0x10, 00, 0x09, 0x3f, 00, 01, (byte)0x83, 0x42, 00, 00, 02, 01, 0x08});
        buff.getShort(); // 10 00
        buff.getShort(); // 09 3f
        buff.getShort(); // 00 01
        buff.order(ByteOrder.LITTLE_ENDIAN);
        short id = buff.getShort();
        Mail mail = mContext.getMail(id);
        mail.print(mContext, buff, 0);
        //mContext.resolve(typeId).print(mContext, buff, 0);
    }

}
