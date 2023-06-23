package fr.diginamic.token;

import java.util.ArrayList;
import java.util.List;

public class FileTokeniserImpl implements FileTokeniser {

    /**Permet de récupérer les tokens d'une ligne*/
    private final LineTokeniser lineTokeniser;
    /**Array d'array de tokens*/
    public SyntaxToken[][] tokens = new SyntaxToken[13433][];
    /**Position dans l'array d'array de tokens*/
    private int position;

    /**
     * Constructeur avec injection de dépendance du tokeniser de ligne
     * @param lineTokeniser
     * */
    public FileTokeniserImpl(LineTokeniser lineTokeniser){
        this.lineTokeniser = lineTokeniser;
    }

    /**
     * Récupère une ligne de token à partir d'un tokeniser de ligne et l'ajoute dans
     * un array d'array de tokens.
     * @param text
     * */
    public void tokenise(String text){
        lineTokeniser.setLine(text);
        List<SyntaxToken> tokens = new ArrayList<>();
        SyntaxToken token;
        do{
            token = lineTokeniser.nextToken();
            tokens.add(token);
        } while( token.getKind() != SyntaxKind.END_OF_LINE_TOKEN);
        this.tokens[position++] = tokens.toArray(new SyntaxToken[tokens.size()]);
    }

}
