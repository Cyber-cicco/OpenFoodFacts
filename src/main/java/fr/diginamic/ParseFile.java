package fr.diginamic;

import fr.diginamic.parser.FileParser;
import fr.diginamic.parser.FileParserImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class ParseFile {
    public static void main(String[] args) {
        long time = new Date().getTime();
        FileParser fileParser = new FileParserImpl();
        try {
            fileParser.readCsv();
            System.out.println((new Date().getTime() - time)/1000 + "s");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}