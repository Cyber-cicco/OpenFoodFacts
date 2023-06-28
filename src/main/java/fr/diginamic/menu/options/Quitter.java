package fr.diginamic.menu.options;

import fr.diginamic.dao.DaoFactory;

public class Quitter extends Option{
    public Quitter() {
        super("quitter l'application");
    }

    @Override
    public boolean executeOption() {
        //On ferme toutes les connexions ouvertes par les DAOS
        DaoFactory.closeDaos();
        return false;
    }
}
