package fr.diginamic.parser;

import fr.diginamic.token.SyntaxToken;

/**
 * méthodes publiques nécessaires pour toutes implémentations
 * d'un parser de ligne
 * */
public interface LineParser {
    void parseLine(SyntaxToken[] text, int lineNumber);
    void setLineCount(int lineCount);
    void closeDaos();
}
