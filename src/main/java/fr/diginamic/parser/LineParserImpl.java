package fr.diginamic.parser;

import fr.diginamic.dao.*;
import fr.diginamic.entites.*;
import fr.diginamic.threader.VirtualThread;
import fr.diginamic.token.SyntaxKind;
import fr.diginamic.token.SyntaxToken;
import fr.diginamic.types.ValeurNutritionnelle;
import static fr.diginamic.parser.Cache.*;
import static fr.diginamic.config.CSVParams.EXPECTED_SEPARATORS;
import lombok.SneakyThrows;

import java.util.*;
import java.util.function.Consumer;

/**
 * Classe permettant d'insérer des données en base à partir
 * d'un tableau de SyntaxToken
 * */
public class LineParserImpl implements LineParser{

    /**Liste des tokens de la ligne*/
    private SyntaxToken[] tokens;
    /**Produit à insérer en base à la fin du parcours de la ligne*/
    private Produit produit;
    /**Index actuel du token parcouru dans la ligne*/
    private int tokenIndex;
    /**Liste des threads initialisées pour insérer les entités en base*/
    private final List<Thread> threads = new ArrayList<>();

    /**Classe permettant d'insérer une catégorie en base*/
    private final CategorieDao categorieDao = DaoFactory.getCategorieDao();
    /**Classe permettant d'insérer un allergène en base*/
    private final AllergeneDao allergeneDao = DaoFactory.getAllergeneDao();
    /**Classe permettant d'insérer une marque en base*/
    private final MarqueDao marqueDao = DaoFactory.getMarqueDao();
    /**Classe permettant d'insérer un ingrédient en base*/
    private final IngredientDao ingredientDao = DaoFactory.getIngredientDao();
    /**Classe permettant d'insérer un additif en base*/
    private final AdditifDao additifDao = DaoFactory.getAdditifDao();
    /**Classe permettant d'insérer un produit en base*/
    private final ProduitDao produitDao = DaoFactory.getProduitDao();
    /**
     * Méthode qui va vérifier si une catégorie existe déjà dans le cache
     * Si c'est le cas, renvoie une catégorie associée au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée une nouvelle catégore à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomCategorie : le nom de la catégorie
     * @return Categorie
     * */
    @SneakyThrows
    private Categorie getCategorie(String nomCategorie){
        if(categorieMap.containsKey(nomCategorie)){
            return categorieMap.get(nomCategorie);
        }
        Categorie categorie = new Categorie(nomCategorie);
        categorieMap.put(nomCategorie, categorie);
        //categorieDao.sauvegarder(categorie);
        Thread thread = VirtualThread.getThread("persistence Catégorie", ()->categorieDao.sauvegarder(categorie));
        threads.add(thread);
        return categorie;
    }

    /**
     * Méthode qui va vérifier si un allergène existe déjà dans le cache
     * Si c'est le cas, renvoie un allergène associé au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée un nouvel allergène à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomAllergene : nom de l'allergene
     * @return Allergene
     * */
    @SneakyThrows
    private Allergene getAllergene(String nomAllergene){
        if(allergeneMap.containsKey(nomAllergene)){
            return allergeneMap.get(nomAllergene);
        }
        Allergene allergene = new Allergene(nomAllergene);
        allergeneMap.put(nomAllergene, allergene);
        //allergeneDao.sauvegarder(allergene);
        Thread thread = VirtualThread.getThread("persistence Allergene", ()->{allergeneDao.sauvegarder(allergene);});
        threads.add(thread);
        return allergene;
    }

    /**
     * Méthode qui va vérifier si une marque existe déjà dans le cache
     * Si c'est le cas, renvoie une marque associée au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée une nouvelle marque à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomMarque : nom de la marque
     * @return Marque
     * */
    private Marque getMarque(String nomMarque){
        if(marqueMap.containsKey(nomMarque)){
            return marqueMap.get(nomMarque);
        }
        Marque marque = new Marque(nomMarque);
        marqueMap.put(nomMarque, marque);
        //marqueDao.sauvegarder(marque);
        Thread thread = VirtualThread.getThread("persistence Marque", ()->marqueDao.sauvegarder(marque));
        threads.add(thread);
        return marque;
    }

    /**
     * Méthode qui va vérifier si un ingrédient existe déjà dans le cache
     * Si c'est le cas, renvoie un ingrédient associé au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée un nouvel ingrédient à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomIngredient : nom de l'ingrédient
     * @return Ingredient
     * */
    private Ingredient getIngredient(String nomIngredient){
        if(ingredientMap.containsKey(nomIngredient)){
            return ingredientMap.get(nomIngredient);
        }
        Ingredient ingredient = new Ingredient(nomIngredient);
        ingredientMap.put(nomIngredient, ingredient);
        //ingredientDao.sauvegarder(ingredient);
        Thread thread = VirtualThread.getThread("persistence Ingredient", ()->ingredientDao.sauvegarder(ingredient));
        threads.add(thread);
        return ingredient;
    }
    /**
     * Méthode qui va vérifier si un additif existe déjà dans le cache
     * Si c'est le cas, renvoie un additif associé au code que l'on
     * a passé en paramètre.
     * Sinon, on crée un nouvel additif à partir de son nom et de son code,
     * et on l'insère dans le cache, puis en base.
     * @param code : code unique de l'additif
     * @param nom : nom de l'additif
     * @return Additif
     * */
    private Additif getAdditif(String code, String nom) {
        if(additifMap.containsKey(code)){
            return additifMap.get(code);
        }
        Additif additif = new Additif(code, nom);
        additifMap.put(code, additif);
        //additifDao.sauvegarder(additif);
        Thread thread = VirtualThread.getThread("persistence Additif", ()->additifDao.sauvegarder(additif));
        threads.add(thread);
        return additif;
    }

    /**
     * Méthode permettant de gérer tous les fields du produit finissant par 100g
     * @param setter : Consumer permettant d'invoquer le bon setter du produit
     * @param lineNumber: permet de diagnostiquer les erreurs de parsing de nombre
     * */
    private void set100g(Consumer<String> setter,int lineNumber){
        String _100g = "";
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            _100g += tokens[tokenIndex].getText();
            nextToken();
        }
        if(!_100g.isEmpty()){
            try{
                setter.accept(_100g);
            } catch (NumberFormatException e){
                System.out.println("Erreur de parsing de nombre à la ligne " + lineNumber);
            }
        }
        nextToken();
    }

    /**
     * Méthode permettant de rendre plus parlante l'incrémentation de l'index permettant
     * de récupérer les tokens
     * */
    private void nextToken(){
        ++tokenIndex;
    }

    /**
     * Méthode parcourant les tokens associés à une catégorie pour
     * récupérer son nom et l'insérer dans le produit, et éventuellement
     * l'insérer en base.
     * */
    private void createCategorie(){
        String nomCategorie = "";
        //boucle jusqu'au premier pour récupérer le nom d'une catégorie
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            nomCategorie += tokens[tokenIndex].getText();

            nextToken();

        }
        produit.setCategorie(getCategorie(nomCategorie));
    }

    /**
     * Méthode parcourant les tokens associés à une marque pour
     * récupérer son nom et l'insérer dans le produit, et éventuellement
     * l'insérer en base.
     * */
    private void createMarque(){
        String nomMarque = "";
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            nomMarque += tokens[tokenIndex].getText();

            nextToken();

        }
        produit.setMarque(getMarque(nomMarque));
    }

    /**
     * Méthode parcourant les tokens associés au nom d'un produit
     * et le set dans le produit
     * */
    private void setNomProduit(){
        String nomProduit = "";
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            nomProduit += tokens[tokenIndex].getText();

            nextToken();

        }
        produit.setNom(nomProduit);
    }

    /**
     * Méthode permettant d'associé une valeur nutritionnelle au produit en fonction
     * du token associé.
     * */
    private void setValeurNutritonnelle(){
        if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.A.getMessage())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.A);
        } else if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.B.getMessage())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.B);
        } else if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.C.getMessage())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.C);
        } else if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.D.getMessage())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.D);
        } else if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.E.getMessage())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.E);
        } else if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.F.getMessage())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.F);
        }
    }

    /**
     * Méthode permettant de vérifier les tokens suivants ou précédents
     * de la ligne
     * @param offset : nombre de tokens auxquels on souhaite regarder.
     * */
    private SyntaxToken peek(int offset){
        if(tokenIndex + offset >= tokens.length){
            return new SyntaxToken(SyntaxKind.BAD_TOKEN, tokenIndex + offset, null);
        }
        return tokens[tokenIndex + offset];
    }

    /**
     * Méthode permettant de récupérer le nombre de séparateurs CSVs attendus dans
     * la partie sur les ingrédients.
     * En effet, il n'est pas si rare que des petits malins aient insérés des séparateurs
     * CSV dans la liste d'ingrédients, vu que les inputs utilisateurs ne semblent absolument
     * pas filtrés. Va donc soustraire le nombre réel de pipe au nombre de pipe attendus, et rajouter
     * 1.
     * */
    private int getExpectedPipeNumber(){
        int expectedPipes = 1;
        for(int i = 1; i < tokens.length; i++){
            if(peek(i).getKind() == SyntaxKind.CSV_SEPARATOR){
                ++expectedPipes;
            }
        }
        return EXPECTED_SEPARATORS - expectedPipes;
    }

    /**
     * Méthode contenant les règles métier pour parcourir la liste d'ingrédients
     * Va parse les ingrédients selon ces règles, et les associer au produit de
     * la ligne
     * */
    private void createIngredients(){
        //variable permettant de vérifier le nombre de séparateurs CSV rencontrés
        //dans le parcours des ingrédients
        int pipeCount = 0;
        int expectedPipeNumber = getExpectedPipeNumber();
        while (pipeCount != expectedPipeNumber){
            //initialisation du nom de l'ingrédient à insérer dans une chaine vide
            String nomIngredient = "";
            //Tant que l'on ne rencontre pas un token identifié comme séparateur d'ingrédients...
            while (tokens[tokenIndex].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.INGREDIENT_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.DOT_TOKEN){
                //...si le token rencontré est un espace ou un mot...
                if(tokens[tokenIndex].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                        tokens[tokenIndex].getKind() == SyntaxKind.ENTITY_FIELD){
                    //...on ajoute le contenu du token au nom de l'ingrédient...
                    nomIngredient += tokens[tokenIndex].getText();
                }
                //...mais si le token est égal à :...
                if(tokens[tokenIndex].getKind() == SyntaxKind.DESCRIPTOR){
                    //...alors ce que nous avons mis dans le nom de l'ingrédient ne faisait que décrire
                    //le type de l'ingrédient, et on réinitialise la chaine à un caractère vide.
                    nomIngredient = "";
                }

                nextToken();

            }
            //On enlève les espaces au début et à la fin du nom de l'ingrédient...
            nomIngredient = nomIngredient.trim();
            //Et si le nom de l'ingrédient n'est pas vide, on rajoute l'ingrédient au produit
            if(!nomIngredient.isBlank()) produit.addIngredient(getIngredient(nomIngredient));
            //Dans certains cas, un séparateur est directement suivie d'un Séparateur CSV. Dans ce cas,
            //on sort de la boucle.
            if(tokens[tokenIndex].getKind() == SyntaxKind.CSV_SEPARATOR){
                break;
            }

            nextToken();
            //Si on se trouve face à une parenthèse ouvrante, on ignore tous les tokens
            //jusqu'à trouver la parenthèse fermante associée.
            if(tokens[tokenIndex].getKind() == SyntaxKind.L_PARENTHESIS){
                while(tokens[tokenIndex].getKind() != SyntaxKind.L_PARENTHESIS ||
                        tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){

                    nextToken();

                }
                //TODO : rajouter un nextToken()
            }
            //Si notre nouveau token est un séparateur CSV, on incrémente le nombre de séparateur CSV rencontrés.
            if(tokens[tokenIndex].getKind() == SyntaxKind.CSV_SEPARATOR) pipeCount++;
        }
    }

    /**
     * Méthode contenant les règles métier pour parcourir la liste d'allergenes
     * Va parse les allergenes selon ces règles, et les associer au produit de
     * la ligne.
     * */
    private void createAllergene(){
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            String nomAllergene = "";
            while (tokens[tokenIndex].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
                if(tokens[tokenIndex].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                        tokens[tokenIndex].getKind() == SyntaxKind.ENTITY_FIELD ||
                        tokens[tokenIndex].getKind() == SyntaxKind.DESCRIPTOR){
                    nomAllergene += tokens[tokenIndex].getText();
                }
                tokenIndex++;
            }
            produit.addAllergene(getAllergene(nomAllergene.trim()));
            if(tokens[tokenIndex].getKind() == SyntaxKind.CSV_SEPARATOR){
                break;
            }

            nextToken();

        }
    }

    /**
     * Méthode contenant les règles métier pour parcourir la liste d'additifs
     * Va parse les allergenes selon ces règles, et les associer à l'additif de
     * la ligne
     * */
    private void createAdditif(){
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            //initialisation du code de l'additif
            String codeAdditif = "";
            //Le MINUS-TOKEN est dans le cas de l'additif vu comme un séparateur entre
            //le code et le nom
            while (tokens[tokenIndex].getKind() != SyntaxKind.MINUS_TOKEN &&
                    tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
                codeAdditif += tokens[tokenIndex].getText();

                nextToken();

            }

            nextToken();

            //initilisation du nom de l'additif
            String nomAdditif = "";
            while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.ENTITY_SEPARATOR ){
                nomAdditif += tokens[tokenIndex].getText();

                nextToken();

            }
            produit.addAdditif(getAdditif(codeAdditif.trim(), nomAdditif.trim()));
            if(tokens[tokenIndex].getKind() == SyntaxKind.CSV_SEPARATOR){
                break;
            }

            nextToken();

        }
    }

    /**
     * Méthode permettant d'initialiser le parsing d'une ligne,
     * en settant les tokens à parcourir, en créant un nouveau produit
     * à hydrater puis insérer en base, puis en initialisant l'index de
     * parcours des tokens.
     * @param tokens : les tokens de l'objet
     * */
    private void initializeParsing(SyntaxToken[] tokens){
        this.tokens = tokens;
        produit = new Produit();
        tokenIndex = 0;
    }

    /**
     * Méthode pour insérer en base toutes les données d'une ligne du CSV de
     * façon asynchrone
     * @param tokens : l'array de token à parcourir
     * @param lineNumber : le compte de la ligne actuelle, données pour des raisons de debug
     * */
    @SneakyThrows
    @Override
    public void parseLine(SyntaxToken[] tokens, int lineNumber) {
        //initialisation de l'objet
        initializeParsing(tokens);
        //création de la catégorie
        createCategorie();
        nextToken();
        //création de la marque
        createMarque();
        nextToken();
        //ajout du nom du produit
        setNomProduit();
        nextToken();
        //ajout de la valeur nutritionnelle du produit
        setValeurNutritonnelle();
        nextToken();
        nextToken();
        //création des ingrédients
        createIngredients();
        nextToken();
        //ajout de tous les fields finissant par 100g
        set100g((v)-> produit.setEnergie100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setGraisse100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setSucres100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setFibres100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setProteines100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setSel100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitA100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitD100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitE100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitK100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitC100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitB1100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitB2100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitPP100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitB6100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitB9100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setVitB12100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setCalcium100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setMagnesium100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setIron100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setFer100g(Double.parseDouble(v)), lineNumber);
        set100g((v)-> produit.setBetaCarotene100g(Double.parseDouble(v)), lineNumber);
        produit.setPresenceHuilePalme(!Objects.equals(tokens[tokenIndex].getText(), "0"));
        nextToken();
        nextToken();
        //création des allergenes.
        createAllergene();
        nextToken();
        //création des additifs.
        createAdditif();
        //On attend que tous les threads d'insertions en base des entités associées au produit soient terminés.
        for(int k = 0; k < threads.size(); k++){
            threads.get(k).join();
        }
        //On réinitialise les threads à 0.
        threads.clear();
        if(lineNumber%100 == 0) System.out.println("Parsing de la ligne : " + lineNumber);
        //Enfin, on sauvegarde le produit.
        produitDao.sauvegarder(produit);
    }

    /**
     * Méthode permettant de fermer
     * les connexions à la base de données.
     * */
    public void closeDaos(){
        additifDao.close();
        allergeneDao.close();
        ingredientDao.close();
        produitDao.close();
        marqueDao.close();
        categorieDao.close();
    }
}
