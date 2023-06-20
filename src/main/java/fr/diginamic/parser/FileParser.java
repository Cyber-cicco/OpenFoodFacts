package fr.diginamic.parser;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileParser {
    void readCsv() throws URISyntaxException, IOException;
}
