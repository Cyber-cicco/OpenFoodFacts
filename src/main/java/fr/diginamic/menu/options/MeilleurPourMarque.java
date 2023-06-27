package fr.diginamic.menu.options;

import fr.diginamic.dao.*;

/**
 * Classe implémentant les fonctionnalités de l'option
 * N meilleures pour la marque du menu
 * */
public class MeilleurPourMarque extends Option{
    private ProduitDao dao = DaoFactory.getProduitDao();
    @Override
    public void executeOption() {
        dao.extraireNMeilleursParMarque(100);
    }
}
