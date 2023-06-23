package fr.diginamic.menu.options;

import fr.diginamic.dao.*;

public class MeilleurPourMarque extends Option{
    private ProduitDao dao = DaoFactory.getProduitDao();
    @Override
    public void executeOption() {
        dao.extraireNMeilleursParMarque(100);
    }
}
