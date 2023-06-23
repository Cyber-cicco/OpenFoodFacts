package fr.diginamic;

import fr.diginamic.parser.FileParser;
import fr.diginamic.parser.FileParserImpl;

import java.io.IOException;
import java.net.URISyntaxException;

public class ParseFile {
    public static void main(String[] args) {
        FileParser fileParser = new FileParserImpl();
        try {
            fileParser.readCsv();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}