package fr.diginamic.parser;

import fr.diginamic.dao.*;
import fr.diginamic.entites.*;
import fr.diginamic.threader.VirtualThread;
import fr.diginamic.token.LineTokeniser;
import fr.diginamic.token.SyntaxKind;
import fr.diginamic.types.ValeurNutritionnelle;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileParserImpl implements FileParser {

    private final FileTokeniserImpl fileTokeniser;
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

    public FileParserImpl() {
        LineTokeniser tokeniser = new LineTokeniser();
        fileTokeniser = new FileTokeniserImpl(tokeniser);
    }

    /**
     * Méthode permettant de créer une nouvelle catégorie
     * si elle n'existe ou qui permet de la récupérer de la map
     * */
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

    private int set100g(Consumer<String> setter, int i, int j){
        String _100g = "";
        while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
            _100g += fileTokeniser.tokens[i][j].getText();
            ++j;
        }
        if(!_100g.isEmpty()){
            try{
                setter.accept(_100g);
            } catch (NumberFormatException e){
                System.out.println(i + " " + j);
            }
        }
        return ++j;
    }

    @SneakyThrows
    public void readCsv() throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("open-food-facts.csv"))
                .toURI());
        try(Stream<String> lines = Files.lines(path)){
            lines.forEach(fileTokeniser::tokenise);
            for(int i = 1; i < fileTokeniser.tokens.length; ++i){
                Produit produit = new Produit();
                int j = 0;
                String nomCategorie = "";
                //boucle jusqu'au premier pour récupérer le nom d'une catégorie
                while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    nomCategorie += fileTokeniser.tokens[i][j].getText();
                    ++j;
                }
                produit.setCategorie(getCategorie(nomCategorie));
                ++j;
                String nomMarque = "";
                while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    nomMarque += fileTokeniser.tokens[i][j].getText();
                    ++j;
                }
                produit.setMarque(getMarque(nomMarque));
                ++j;
                String nomProduit = "";
                while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    nomProduit += fileTokeniser.tokens[i][j].getText();
                    ++j;
                }
                produit.setNom(nomProduit);
                ++j;
                if(Objects.equals(fileTokeniser.tokens[i][j].getText(), ValeurNutritionnelle.a.toString())){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.a);
                } else if(fileTokeniser.tokens[i][j].getText() == ValeurNutritionnelle.b.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.b);
                } else if(fileTokeniser.tokens[i][j].getText() == ValeurNutritionnelle.c.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.c);
                } else if(fileTokeniser.tokens[i][j].getText() == ValeurNutritionnelle.d.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.d);
                } else if(fileTokeniser.tokens[i][j].getText() == ValeurNutritionnelle.e.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.e);
                } else if(fileTokeniser.tokens[i][j].getText() == ValeurNutritionnelle.f.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.f);
                }
                ++j;
                ++j;
                while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    String nomIngredient = "";
                    while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                            fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR &&
                            fileTokeniser.tokens[i][j].getKind() != SyntaxKind.INGREDIENT_SEPARATOR &&
                            fileTokeniser.tokens[i][j].getKind() != SyntaxKind.DOT_TOKEN){
                        if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                        fileTokeniser.tokens[i][j].getKind() == SyntaxKind.ENTITY_FIELD){
                            nomIngredient += fileTokeniser.tokens[i][j].getText();
                        }
                        if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.DESCRIPTOR){
                            nomIngredient = "";
                        }
                        ++j;
                    }
                    nomIngredient = nomIngredient.trim();
                    if(!nomIngredient.isBlank()) produit.addIngredient(getIngredient(nomIngredient));
                    if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.CSV_SEPARATOR){
                        break;
                    }
                    ++j;
                    if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.L_PARENTHESIS){
                        while(fileTokeniser.tokens[i][j].getKind() != SyntaxKind.L_PARENTHESIS ||
                                fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                            ++j;
                        }
                    }
                }
                ++j;
                j = set100g((v)-> produit.setEnergie100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setGraisse100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setSucres100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setFibres100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setProteines100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setSel100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitA100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitD100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitE100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitK100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitC100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitB1100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitB2100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitPP100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitB6100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitB9100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setVitB12100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setCalcium100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setMagnesium100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setIron100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setFer100g(Double.parseDouble(v)), i, j);
                j = set100g((v)-> produit.setBetaCarotene100g(Double.parseDouble(v)), i, j);

                produit.setPresenceHuilePalme(!Objects.equals(fileTokeniser.tokens[i][j].getText(), "0"));

                j++;
                j++;

                while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    String nomAllergene = "";
                    while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                            fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                        if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                                fileTokeniser.tokens[i][j].getKind() == SyntaxKind.ENTITY_FIELD ||
                                fileTokeniser.tokens[i][j].getKind() == SyntaxKind.DESCRIPTOR){
                            nomAllergene += fileTokeniser.tokens[i][j].getText();
                        }
                        j++;
                    }
                    produit.addAllergene(getAllergene(nomAllergene.trim()));
                    if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.CSV_SEPARATOR){
                        break;
                    }
                    ++j;
                }
                ++j;
                while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    String codeAdditif = "";
                    while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.MINUS_TOKEN &&
                            fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                        codeAdditif += fileTokeniser.tokens[i][j].getText();
                        ++j;
                    }
                    ++j;
                    String nomAdditif = "";
                    while (fileTokeniser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR &&
                            fileTokeniser.tokens[i][j].getKind() != SyntaxKind.ENTITY_SEPARATOR ){
                        nomAdditif += fileTokeniser.tokens[i][j].getText();
                        ++j;
                    }
                    produit.addAdditif(getAdditif(codeAdditif.trim(), nomAdditif.trim()));
                    if(fileTokeniser.tokens[i][j].getKind() == SyntaxKind.CSV_SEPARATOR){
                        break;
                    }
                    ++j;
                }
                for(int k = 0; k < threads.size(); k++){
                  threads.get(k).join();
                }
                threads.clear();
                if(i%100 == 0) System.out.println(i);
                produitDao.sauvegarder(produit);
            }
        }
        closeDaos();
    }

    private void closeDaos(){
        additifDao.close();
        allergeneDao.close();
        ingredientDao.close();
        produitDao.close();
        marqueDao.close();
        categorieDao.close();

    }
}
