package fr.diginamic.types;

/**
 * Permet de créer des fonctions anonymes qui ne prennent pas de paramètres
 * et renvoient un objet.
 * Peut être utile dans le cas où des méthodes demandent de construire un objet.
 * */
public interface Producer<T> {
    T apply();
}
