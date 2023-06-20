package fr.diginamic.token;

import java.util.ArrayList;
import java.util.List;

public class LineTokeniser {
    /**Texte à tokenier*/
    private String _text;
    /**Position actuelle dans le texte*/
    private int _position;
    /**Liste d'erreurs survenues lors du parsing des tokens.*/
    private List<String> _diagnostics = new ArrayList<>();
    public LineTokeniser(){
    }

    public void setLine(String _text){
        _position = 0;
        this._text = _text;
    }

    private char current(){
        if(_position >= _text.length()){
            return '\0';
        }
        return _text.charAt(_position);
    }

    public SyntaxToken nextToken(){
        if(_position >= _text.length()){
            return new SyntaxToken(SyntaxKind.END_OF_LINE_TOKEN, _position, "\0");
        }
        if(Character.isAlphabetic(current())){
            int start = _position;
            while (Character.isAlphabetic(current())){
                ++_position;
            }
            String text = _text.substring(start, _position);
            return new SyntaxToken(SyntaxKind.ENTITY_FIELD, start, text.toLowerCase());

        }
        if(Character.isDigit(current()) || current() == '.'){
            int start = _position;
            while (Character.isDigit(current()) || current() == '.'){
                ++_position;
            }
            String text = _text.substring(start, _position);
            return new SyntaxToken(SyntaxKind.ENTITY_FIELD, start, text);
        }
        if(Character.isWhitespace(current())){
            int start = _position;
            while (Character.isWhitespace(current())){
                ++_position;
            }
            String text = _text.substring(start, _position);
            return new SyntaxToken(SyntaxKind.WHITESPACE_TOKEN, start, text);
        }
        switch (current()) {
            case '|' -> {return new SyntaxToken(SyntaxKind.CSV_SEPARATOR, _position++, "|");}
            case ',' -> {return new SyntaxToken(SyntaxKind.ENTITY_SEPARATOR, _position++, ",");}
            case '/' -> {return new SyntaxToken(SyntaxKind.ENTITY_SEPARATOR, _position++, "/");}
            case ';' -> {return new SyntaxToken(SyntaxKind.ENTITY_SEPARATOR, _position++, ";");}
            case ':' -> {return new SyntaxToken(SyntaxKind.DESCRIPTOR, _position++, ":");}
            case '_' -> {return new SyntaxToken(SyntaxKind.BAD_TOKEN, _position++, "_");}
            case '-' -> {return new SyntaxToken(SyntaxKind.MINUS_TOKEN, _position++, "/");}
            default ->{
                _diagnostics.add("ERROR : bad charcacter input '" + current() + "'");
                return new SyntaxToken(SyntaxKind.BAD_TOKEN, _position++, _text.substring(_position - 1, _position));
            }
        }
    }
}

