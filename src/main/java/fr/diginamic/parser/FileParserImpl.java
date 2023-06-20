package fr.diginamic.parser;

import fr.diginamic.token.LineTokeniser;
import fr.diginamic.token.SyntaxToken;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class FileParserImpl implements FileParser {

    private final LineParser lineParser;

    public FileParserImpl() {
        LineTokeniser tokeniser = new LineTokeniser();
        lineParser = new LineParserImpl(tokeniser);
    }

    public void readCsv() throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("open-food-facts.csv"))
                .toURI());
        try(Stream<String> lines = Files.lines(path)){
            lines.forEach(lineParser::parseLine);
            for(int i = 1; i < lineParser.getTokens().length; ++i){
                for(int j = 0; j < lineParser.getTokens()[i].length; ++j) {

                }
            }
        }
    }
}
