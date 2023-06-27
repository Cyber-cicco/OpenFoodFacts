package fr.diginamic.types;

/**Permet de déterminer tous les types de valeur nutritionnelle*/
public enum ValeurNutritionnelle {
    A("a"), B("b"), C("c"),D("d"),E("e"),F("f");

    String message;
    ValeurNutritionnelle(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
