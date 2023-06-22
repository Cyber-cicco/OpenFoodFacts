package fr.diginamic.parser;

import fr.diginamic.dao.*;
import fr.diginamic.entites.*;
import fr.diginamic.threader.VirtualThread;
import fr.diginamic.token.SyntaxKind;
import fr.diginamic.token.SyntaxToken;
import fr.diginamic.types.ValeurNutritionnelle;
import lombok.SneakyThrows;

import java.util.*;
import java.util.function.Consumer;

public class LineParserImpl implements LineParser{

    private SyntaxToken[] tokens;
    private Produit produit;
    private int tokenIndex;
    private final Map<String, Marque> marqueMap = new HashMap<>();
    private final Map<String, Ingredient> ingredientMap = new HashMap<>();
    private final Map<String, Categorie> categorieMap = new HashMap<>();
    private final Map<String, Allergene> allergeneMap = new HashMap<>();
    private final Map<String, Additif> additifMap = new HashMap<>();
    private final List<Thread> threads = new ArrayList<>();

    private final CategorieDao categorieDao = DaoFactory.getCategorieDao();
    private final AllergeneDao allergeneDao = DaoFactory.getAllergeneDao();
    private final MarqueDao marqueDao = DaoFactory.getMarqueDao();
    private final IngredientDao ingredientDao = DaoFactory.getIngredientDao();
    private final AdditifDao additifDao = DaoFactory.getAdditifDao();
    private final ProduitDao produitDao = DaoFactory.getProduitDao();
    @SneakyThrows
    private Categorie getCategorie(String nomCategorie){
        if(categorieMap.containsKey(nomCategorie)){
            return categorieMap.get(nomCategorie);
        }
        Categorie categorie = new Categorie(nomCategorie);
        categorieMap.put(nomCategorie, categorie);
        //categorieDao.sauvegarder(categorie);
        Thread thread = VirtualThread.getThread("persistence Catégorie", ()->{categorieDao.sauvegarder(categorie);});
        threads.add(thread);
        return categorie;
    }

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

    private Marque getMarque(String nomMarque){
        if(marqueMap.containsKey(nomMarque)){
            return marqueMap.get(nomMarque);
        }
        Marque marque = new Marque(nomMarque);
        marqueMap.put(nomMarque, marque);
        //marqueDao.sauvegarder(marque);
        Thread thread = VirtualThread.getThread("persistence Marque", ()->{marqueDao.sauvegarder(marque);});
        threads.add(thread);
        return marque;
    }

    private Ingredient getIngredient(String nomIngredient){
        if(ingredientMap.containsKey(nomIngredient)){
            return ingredientMap.get(nomIngredient);
        }
        Ingredient ingredient = new Ingredient(nomIngredient);
        ingredientMap.put(nomIngredient, ingredient);
        ingredientDao.sauvegarder(ingredient);
        //Thread thread = VirtualThread.getThread("persistence Ingredient", ()->{
        //  ingredientDao.sauvegarder(ingredient);});
        //threads.add(thread);
        return ingredient;
    }
    private Additif getAdditif(String code, String nom) {
        if(additifMap.containsKey(code)){
            return additifMap.get(code);
        }
        Additif additif = new Additif(code, nom);
        additifMap.put(code, additif);
        //additifDao.sauvegarder(additif);
        Thread thread = VirtualThread.getThread("persistence Additif", ()->{additifDao.sauvegarder(additif);});
        threads.add(thread);
        return additif;
    }

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

    private void nextToken(){
        ++tokenIndex;
    }

    private void createCategorie(){
        String nomCategorie = "";
        //boucle jusqu'au premier pour récupérer le nom d'une catégorie
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            nomCategorie += tokens[tokenIndex].getText();

            nextToken();

        }
        produit.setCategorie(getCategorie(nomCategorie));
    }

    private void createMarque(){
        String nomMarque = "";
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            nomMarque += tokens[tokenIndex].getText();

            nextToken();

        }
        produit.setMarque(getMarque(nomMarque));
    }

    private void setNomProduit(){
        String nomProduit = "";
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            nomProduit += tokens[tokenIndex].getText();

            nextToken();

        }
        produit.setNom(nomProduit);
    }

    private void setValeurNutritonnelle(){
        if(Objects.equals(tokens[tokenIndex].getText(), ValeurNutritionnelle.a.toString())){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.a);
        } else if(tokens[tokenIndex].getText() == ValeurNutritionnelle.b.toString()){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.b);
        } else if(tokens[tokenIndex].getText() == ValeurNutritionnelle.c.toString()){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.c);
        } else if(tokens[tokenIndex].getText() == ValeurNutritionnelle.d.toString()){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.d);
        } else if(tokens[tokenIndex].getText() == ValeurNutritionnelle.e.toString()){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.e);
        } else if(tokens[tokenIndex].getText() == ValeurNutritionnelle.f.toString()){
            produit.setValeurNutritionnelle(ValeurNutritionnelle.f);
        }
    }

    private void createIngredient(){
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            String nomIngredient = "";
            while (tokens[tokenIndex].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.INGREDIENT_SEPARATOR &&
                    tokens[tokenIndex].getKind() != SyntaxKind.DOT_TOKEN){
                if(tokens[tokenIndex].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                        tokens[tokenIndex].getKind() == SyntaxKind.ENTITY_FIELD){
                    nomIngredient += tokens[tokenIndex].getText();
                }
                if(tokens[tokenIndex].getKind() == SyntaxKind.DESCRIPTOR){
                    nomIngredient = "";
                }

                nextToken();

            }
            nomIngredient = nomIngredient.trim();
            if(!nomIngredient.isBlank()) produit.addIngredient(getIngredient(nomIngredient));
            if(tokens[tokenIndex].getKind() == SyntaxKind.CSV_SEPARATOR){
                break;
            }

            nextToken();

            if(tokens[tokenIndex].getKind() == SyntaxKind.L_PARENTHESIS){
                while(tokens[tokenIndex].getKind() != SyntaxKind.L_PARENTHESIS ||
                        tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){

                    nextToken();

                }
            }
        }
    }

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

    private void createAdditif(){
        while (tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
            String codeAdditif = "";
            while (tokens[tokenIndex].getKind() != SyntaxKind.MINUS_TOKEN &&
                    tokens[tokenIndex].getKind() != SyntaxKind.CSV_SEPARATOR){
                codeAdditif += tokens[tokenIndex].getText();

                nextToken();

            }

            nextToken();

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

    private void initializeParsing(SyntaxToken[] tokens){
        this.tokens = tokens;
        produit = new Produit();
        tokenIndex = 0;
    }

    @SneakyThrows
    @Override
    public void parseLine(SyntaxToken[] tokens, int lineNumber) {
        initializeParsing(tokens);
        createCategorie();
        nextToken();
        createMarque();
        nextToken();
        setNomProduit();
        nextToken();
        setValeurNutritonnelle();
        nextToken();
        nextToken();
        createIngredient();
        nextToken();
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
        createAllergene();
        nextToken();
        createAdditif();
        for(int k = 0; k < threads.size(); k++){
            threads.get(k).join();
        }
        threads.clear();
        if(lineNumber%100 == 0) System.out.println(lineNumber);
        produitDao.sauvegarder(produit);
    }

    public void closeDaos(){
        additifDao.close();
        allergeneDao.close();
        ingredientDao.close();
        produitDao.close();
        marqueDao.close();
        categorieDao.close();

    }
}
