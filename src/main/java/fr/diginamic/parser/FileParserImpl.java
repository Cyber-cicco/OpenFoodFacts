package fr.diginamic.parser;

import fr.diginamic.threader.VirtualThread;
import fr.diginamic.token.LineTokeniser;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileParserImpl implements FileParser {

    private final FileTokeniserImpl fileTokeniser;
    private final LineParser lineParser = new LineParserImpl();
    private final LineParser lineParser2 = new LineParserImpl();

    public FileParserImpl() {
        LineTokeniser tokeniser = new LineTokeniser();
        fileTokeniser = new FileTokeniserImpl(tokeniser);
    }

    /**
     * Méthode permettant de créer une nouvelle catégorie
     * si elle n'existe ou qui permet de la récupérer de la map
     * */

    @SneakyThrows
    public void readCsv() throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("open-food-facts.csv"))
                .toURI());
        try(Stream<String> lines = Files.lines(path)){
            lines.forEach(fileTokeniser::tokenise);
            Thread firstHalf = VirtualThread.getThread("first", ()->{
                for(int i = 1; i < fileTokeniser.tokens.length/2; ++i){
                    lineParser.parseLine(fileTokeniser.tokens[i], i);
                }});
            Thread secondHalf = VirtualThread.getThread("second", ()->{
                for(int i = fileTokeniser.tokens.length/2; i < fileTokeniser.tokens.length; ++i){
                    lineParser2.parseLine(fileTokeniser.tokens[i], i);
                }});
            firstHalf.join();
            secondHalf.join();
        }
        lineParser.closeDaos();
    }

}
