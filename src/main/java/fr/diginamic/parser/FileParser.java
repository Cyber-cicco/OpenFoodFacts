package fr.diginamic.parser;

import java.io.IOException;
import java.net.URISyntaxException;

/**Un fileParser doit pouvoir lire un CSV*/
public interface FileParser {
    void readCsv() throws URISyntaxException, IOException;
}
