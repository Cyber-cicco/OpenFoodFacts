package fr.diginamic.parser;

import fr.diginamic.token.SyntaxToken;

public interface LineParser {
    void parseLine(SyntaxToken[] text, int lineNumber);

    void closeDaos();
}
