package ru.mera.rtx.logparser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Main {

    private static final String FILE_STD = "-";

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {


        String outFile = args.length < 2 ? FILE_STD : args[1];
        String inFile = args.length < 1 ? "-" : args[0];

        Reader in;
        if (FILE_STD.equals(inFile)) {
            in = new InputStreamReader(System.in);
        } else {
            in = new FileReader(inFile);
        }

        PrintStream out;
        if (FILE_STD.equals(outFile)) {
            out = System.out;
        } else {
            out = new PrintStream(new File(outFile));
        }

        LogProcessor processor = new LogProcessor();

        processor.process(in, out);
    }
}
