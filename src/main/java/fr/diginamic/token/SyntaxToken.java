package fr.diginamic.token;

/**
 * Classe permettant de stocker tous les éléments du texte parse ayant une signification pour
 * le traitement de celui-ci.
 * L'attribut kind va détermminer la façon dont vont être traités les différents tokens.
 * */
public class SyntaxToken {
    /**Le type de token dont il s'agit*/
    private SyntaxKind kind;

    /**La position du token dans la chaine de tokens récupérée par le Parser*/
    private int position;
    /**Le contenu texte du token. Par exemple, le contenu d'un token de type
     * PLUS_OPERATOR sera "+"*/
    private String text;

    public SyntaxToken(SyntaxKind kind, int position, String text) {
        this.kind = kind;
        this.position = position;
        this.text = text;
    }

    public SyntaxKind getKind() {
        return kind;
    }

    public void setKind(SyntaxKind kind) {
        this.kind = kind;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SyntaxToken{" +
                "kind=" + kind +
                ", position=" + position +
                ", text='" + text + '\'' +
                '}';
    }
}
