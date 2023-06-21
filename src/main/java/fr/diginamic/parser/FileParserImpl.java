package fr.diginamic.parser;

import fr.diginamic.entites.*;
import fr.diginamic.token.LineTokeniser;
import fr.diginamic.token.SyntaxKind;
import fr.diginamic.token.SyntaxToken;
import fr.diginamic.types.ValeurNutritionnelle;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileParserImpl implements FileParser {

    private final LineParserImpl lineParser;

    private final Map<String, Marque> marqueMap = new HashMap<>();
    private final Map<String, Ingredient> ingredientMap = new HashMap<>();
    private final Map<String, Categorie> categorieMap = new HashMap<>();
    private final Map<String, Allergene> allergeneMap = new HashMap<>();
    private final Map<String, Additif> additifMap = new HashMap<>();

    public FileParserImpl() {
        LineTokeniser tokeniser = new LineTokeniser();
        lineParser = new LineParserImpl(tokeniser);
    }

    /**
     * Méthode permettant de créer une nouvelle catégorie
     * si elle n'existe ou qui permet de la récupérer de la map
     * */
    private Categorie getCategorie(String nomCategorie){
        if(categorieMap.containsKey(nomCategorie)){
            return categorieMap.get(nomCategorie);
        }
        Categorie categorie = new Categorie(nomCategorie);
        categorieMap.put(nomCategorie, categorie);
        return categorie;
    }

    private Allergene getAllergene(String nomAllergene){
        if(categorieMap.containsKey(nomAllergene)){
            return allergeneMap.get(nomAllergene);
        }
        Allergene allergene = new Allergene(nomAllergene);
        allergeneMap.put(nomAllergene, allergene);
        return allergene;
    }

    private Marque getMarque(String nomMarque){
        if(marqueMap.containsKey(nomMarque)){
            return marqueMap.get(nomMarque);
        }
        Marque marque = new Marque(nomMarque);
        marqueMap.put(nomMarque, marque);
        return marque;
    }

    private Ingredient getIngredient(String nomIngredient){
        if(ingredientMap.containsKey(nomIngredient)){
            return ingredientMap.get(nomIngredient);
        }
        Ingredient ingredient = new Ingredient(nomIngredient);
        ingredientMap.put(nomIngredient, ingredient);
        return ingredient;
    }
    private Additif getAdditif(String code, String nom) {
        if(additifMap.containsKey(code)){
            return additifMap.get(code);
        }
        Additif additif = new Additif(code, nom);
        additifMap.put(code, additif);
        return additif;
    }

    private int set100g(Consumer<String> setter, int i, int j){
        String _100g = "";
        while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
            _100g += lineParser.tokens[i][j].getText();
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

    public void readCsv() throws URISyntaxException, IOException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                        .getResource("open-food-facts.csv"))
                .toURI());
        try(Stream<String> lines = Files.lines(path)){
            lines.forEach(lineParser::parseLine);
            for(int i = 1; i < lineParser.tokens.length; ++i){
                Produit produit = new Produit();
                int j = 0;
                String nomCategorie = "";
                //boucle jusqu'au premier pour récupérer le nom d'une catégorie
                while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    nomCategorie += lineParser.tokens[i][j].getText();
                    ++j;
                }
                produit.setCategorie(getCategorie(nomCategorie));
                ++j;
                String nomMarque = "";
                while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    nomMarque += lineParser.tokens[i][j].getText();
                    ++j;
                }
                produit.setMarque(getMarque(nomMarque));
                ++j;
                String nomProduit = "";
                while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    nomProduit += lineParser.tokens[i][j].getText();
                    ++j;
                }
                produit.setNom(nomProduit);
                ++j;
                if(Objects.equals(lineParser.tokens[i][j].getText(), ValeurNutritionnelle.a.toString())){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.a);
                } else if(lineParser.tokens[i][j].getText() == ValeurNutritionnelle.b.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.b);
                } else if(lineParser.tokens[i][j].getText() == ValeurNutritionnelle.c.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.c);
                } else if(lineParser.tokens[i][j].getText() == ValeurNutritionnelle.d.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.d);
                } else if(lineParser.tokens[i][j].getText() == ValeurNutritionnelle.e.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.e);
                } else if(lineParser.tokens[i][j].getText() == ValeurNutritionnelle.f.toString()){
                    produit.setValeurNutritionnelle(ValeurNutritionnelle.f);
                }
                ++j;
                ++j;
                while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    String nomIngredient = "";
                    while (lineParser.tokens[i][j].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                            lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                        if(lineParser.tokens[i][j].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                        lineParser.tokens[i][j].getKind() == SyntaxKind.ENTITY_FIELD ||
                        lineParser.tokens[i][j].getKind() == SyntaxKind.DESCRIPTOR){
                            nomIngredient += lineParser.tokens[i][j].getText();
                        }
                        j++;
                    }
                    produit.addIngredient(getIngredient(nomIngredient.trim()));
                    if(lineParser.tokens[i][j].getKind() == SyntaxKind.CSV_SEPARATOR){
                        break;
                    }
                    ++j;
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

                produit.setPresenceHuilePalme(!Objects.equals(lineParser.tokens[i][j].getText(), "0"));

                j++;
                j++;

                while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    String nomAllergene = "";
                    while (lineParser.tokens[i][j].getKind() != SyntaxKind.ENTITY_SEPARATOR &&
                            lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                        if(lineParser.tokens[i][j].getKind() == SyntaxKind.WHITESPACE_TOKEN ||
                                lineParser.tokens[i][j].getKind() == SyntaxKind.ENTITY_FIELD ||
                                lineParser.tokens[i][j].getKind() == SyntaxKind.DESCRIPTOR){
                            nomAllergene += lineParser.tokens[i][j].getText();
                        }
                        j++;
                    }
                    produit.addAllergene(getAllergene(nomAllergene.trim()));
                    if(lineParser.tokens[i][j].getKind() == SyntaxKind.CSV_SEPARATOR){
                        break;
                    }
                    ++j;
                }
                ++j;
                while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                    String codeAdditif = "";
                    while (lineParser.tokens[i][j].getKind() != SyntaxKind.MINUS_TOKEN &&
                            lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR){
                        codeAdditif += lineParser.tokens[i][j].getText();
                        ++j;
                    }
                    ++j;
                    String nomAdditif = "";
                    while (lineParser.tokens[i][j].getKind() != SyntaxKind.CSV_SEPARATOR &&
                            lineParser.tokens[i][j].getKind() != SyntaxKind.ENTITY_SEPARATOR ){
                        nomAdditif += lineParser.tokens[i][j].getText();
                        ++j;
                    }
                    produit.addAdditif(getAdditif(codeAdditif.trim(), nomAdditif.trim()));
                    if(lineParser.tokens[i][j].getKind() == SyntaxKind.CSV_SEPARATOR){
                        break;
                    }
                    ++j;
                }
                //log permettant de débug chaque ajout dans le CSV.
                System.out.println(produit);
                System.out.println();
            }
        }
    }

}
