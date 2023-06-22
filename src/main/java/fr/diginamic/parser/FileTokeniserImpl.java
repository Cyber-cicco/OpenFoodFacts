package fr.diginamic.parser;

import fr.diginamic.token.LineTokeniser;
import fr.diginamic.token.SyntaxKind;
import fr.diginamic.token.SyntaxToken;

import java.util.ArrayList;
import java.util.List;

public class FileTokeniserImpl implements FileTokeniser {

    private final LineTokeniser lineTokeniser;
    public SyntaxToken[][] tokens = new SyntaxToken[13433][];
    private int position;

    public FileTokeniserImpl(LineTokeniser lineTokeniser){
        this.lineTokeniser = lineTokeniser;
    }

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
