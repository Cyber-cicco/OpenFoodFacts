package fr.diginamic.parser;

import fr.diginamic.token.SyntaxToken;

public interface LineParser {
    void parseLine(String text);
     SyntaxToken[][] getTokens();
}
