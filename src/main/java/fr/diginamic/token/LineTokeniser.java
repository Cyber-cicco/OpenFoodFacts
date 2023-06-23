package fr.diginamic.token;

/**
 * Classe dont le but est de parcourir chaque caractère d'une ligne
 * et de créer des objets associés au rôle de chaque caractère ou suite
 * de caractère.
 * */
public class LineTokeniser {
    /**Texte à tokenier*/
    private String _text;
    /**Position actuelle dans la ligne*/
    private int _position;
    /**Constructeur*/
    public LineTokeniser(){
    }

    /**Fonction appelée nécessairement avant chaque parsing de ligne*/
    public void setLine(String _text){
        _position = 0;
        this._text = _text;
    }

    /**
     * Permet de récupérer le caractère courant.
     * Renvoie un END_OF_LINE_TOKEN si jamais on
     * est arrivé à la fin de la ligne
     * */
    private char current(){
        if(_position >= _text.length()){
            return '\0';
        }
        return _text.charAt(_position);
    }

    /**
     * Permet de récupérer un token en parcourant les caractères de la
     * ligne du tokeniser à partir de sa dernière position.
     * */
    public SyntaxToken nextToken(){
        //Renvoie un token signifiant la fin de la ligne si l'on est arrivé à la fin du parcours de notre ligne
        if(_position >= _text.length()){
            return new SyntaxToken(SyntaxKind.END_OF_LINE_TOKEN, _position, "\0");
        }
        //Si le caractère est une lettre, on parcours jusqu'à ce qu'on se soit plus sur une lettre,
        //et on renvoie un token de type ENTITY_FIELD
        if(Character.isAlphabetic(current())){
            int start = _position;
            while (Character.isAlphabetic(current()) || Character.isDigit(current())){
                ++_position;
            }
            String text = _text.substring(start, _position);
            //Le token peut également etre un séparateur, dans le cas où il est égal à et ou ou.
            return new SyntaxToken((text.equals("et") || text.equals("ou")) ? SyntaxKind.INGREDIENT_SEPARATOR : SyntaxKind.ENTITY_FIELD, start, text.toLowerCase());
        }
        //Si le caractère est un chiffre, on parcours la ligne jusqu'à ce que le caractère ne soit plus un chiffre
        //ou un point. En effet, un point peut représenter un séparateur entre la partie entière et la partie
        //décimale d'un chiffre
        if(Character.isDigit(current())){
            int start = _position;
            while (Character.isDigit(current()) || current() == '.'){
                ++_position;
            }
            String text = _text.substring(start, _position);
            return new SyntaxToken(SyntaxKind.NUMBER_FIELD, start, text);
        }
        //Si le caractère est un un espace, on renvoie un token signifiant qu'il est un espace.
        if(Character.isWhitespace(current())){
            int start = _position;
            while (Character.isWhitespace(current())){
                ++_position;
            }
            String text = _text.substring(start, _position);
            return new SyntaxToken(SyntaxKind.WHITESPACE_TOKEN, start, text);
        }
        //Switch permettant de récupérer le sens d'un caractère s'il n'est ni un chiffre ni une lettre
        switch (current()) {
            case '|' -> {return new SyntaxToken(SyntaxKind.CSV_SEPARATOR, _position++, "|");}
            case '.' -> {return new SyntaxToken(SyntaxKind.DOT_TOKEN, _position++, ".");}
            case ',' -> {return new SyntaxToken(SyntaxKind.ENTITY_SEPARATOR, _position++, ",");}
            case '/' -> {return new SyntaxToken(SyntaxKind.ENTITY_SEPARATOR, _position++, "/");}
            case ';' -> {return new SyntaxToken(SyntaxKind.ENTITY_SEPARATOR, _position++, ";");}
            case ':' -> {return new SyntaxToken(SyntaxKind.DESCRIPTOR, _position++, ":");}
            case '_' -> {return new SyntaxToken(SyntaxKind.IGNORED_TOKEN, _position++, "_");}
            case '*' -> {return new SyntaxToken(SyntaxKind.IGNORED_TOKEN, _position++, "*");}
            case '%' -> {return new SyntaxToken(SyntaxKind.IGNORED_TOKEN, _position++, "%");}
            case '-' -> {return new SyntaxToken(SyntaxKind.MINUS_TOKEN, _position++, "-");}
            case '(' -> {return new SyntaxToken(SyntaxKind.L_PARENTHESIS, _position++, ")");}
            case ')' -> {return new SyntaxToken(SyntaxKind.R_PARENTHESIS, _position++, "(");}
            default ->{
                return new SyntaxToken(SyntaxKind.BAD_TOKEN, _position++, _text.substring(_position - 1, _position));
            }
        }
    }
}

