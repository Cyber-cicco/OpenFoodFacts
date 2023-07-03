package fr.diginamic.menu.options;

import fr.diginamic.parser.FileParser;
import fr.diginamic.parser.FileParserImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class ImportFichier extends Option{
    @Override
    public boolean executeOption() {
        long time = new Date().getTime();
        FileParser fileParser = new FileParserImpl();
        try {
            long timePersing = new Date().getTime();
            fileParser.readCsv();
            System.out.println("temps depuis le démarrage de l'application : " + (new Date().getTime() - time)/1000 + "s");
            System.out.println("temps pris par les insertions en base : " + (new Date().getTime() - timePersing)/1000 + "s");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public ImportFichier() {
        super("importer les données du fichier CSV");
    }
}
