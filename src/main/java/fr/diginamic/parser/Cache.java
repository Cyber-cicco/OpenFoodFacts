package fr.diginamic.parser;

import fr.diginamic.entites.*;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    public static final Map<String, Marque> marqueMap = new HashMap<>();
    public static final Map<String, Ingredient> ingredientMap = new HashMap<>();
    public static final Map<String, Categorie> categorieMap = new HashMap<>();
    public static final Map<String, Allergene> allergeneMap = new HashMap<>();
    public static final Map<String, Additif> additifMap = new HashMap<>();
}
