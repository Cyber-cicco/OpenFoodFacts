package fr.diginamic.parser;

import fr.diginamic.threader.VirtualThread;
import fr.diginamic.token.FileTokeniserImpl;
import fr.diginamic.token.LineTokeniser;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Classe permettant d'utiliser des THREADS pour transformer le
 * contenu du fichier en données pour la BDD
 * */
public class FileParserImpl implements FileParser {

    /**tokeniser utilisé pour récupérer les tokens de chaque ligne*/
    private final FileTokeniserImpl fileTokeniser;
    /**Parser de ligne permettant de créer les entités d'une ligne*/
    private final LineParser lineParser = new LineParserImpl();
    /**Parser utilisé dans le deuxième thread*/
    private final LineParser lineParser2 = new LineParserImpl();

    /**
     * Constructeur
     * Permet d'initialiser le tokeniser de fichier
     * */
    public FileParserImpl() {
        LineTokeniser tokeniser = new LineTokeniser();
        fileTokeniser = new FileTokeniserImpl(tokeniser);
    }

    /**
     * Méthode publique permettant de transformer le contenu du fichier
     * CSV en entités.
     * */
    @SneakyThrows
    @Override
    public void readCsv() {
        //récupération du fichier dans les ressources
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("open-food-facts.csv"))
                .toURI());
        //Ouverture d'un stream vers le fichier
        try(Stream<String> lines = Files.lines(path)){
            //Parcours de toutes les lignes pour récupérer tous les tokens du fichier
            lines.forEach(fileTokeniser::tokenise);
            lineParser.setLineCount((fileTokeniser.tokens.length/2)-1);
            lineParser2.setLineCount(fileTokeniser.tokens.length/2);
            //Lancement du parcours de la première moitié du fichier dans un premier thread
            Thread firstHalf = VirtualThread.getThread("first", ()->{
                for(int i = 1; i < fileTokeniser.tokens.length/2; ++i){
                    lineParser.parseLine(fileTokeniser.tokens[i], i);
                }});
            Thread secondHalf = VirtualThread.getThread("second", ()->{
                for(int i = fileTokeniser.tokens.length/2; i < fileTokeniser.tokens.length; ++i){
                    lineParser2.parseLine(fileTokeniser.tokens[i], i);
                }});
            //Lancement du parcours de la deuxième moitié du fichier dans un deuxième thread
            firstHalf.join();
            secondHalf.join();
        }
        //On ferme toutes les connexions ouvertes par les DAOS
        lineParser.closeDaos();
        lineParser2.closeDaos();
    }

}
