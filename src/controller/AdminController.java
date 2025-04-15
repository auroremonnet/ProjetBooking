package controller;

import dao.HebergementDAO;
import model.Hebergement;

import java.sql.Connection;
import java.util.List; //test

public class AdminController {
    private final HebergementDAO hebergementDAO;

    public AdminController(Connection conn) {
        this.hebergementDAO = new HebergementDAO(conn);
    }

    public boolean ajouterHebergement(Hebergement h) throws Exception {
        return hebergementDAO.ajouter(h);
    }

    public boolean supprimerHebergement(int idHebergement) throws Exception {
        return hebergementDAO.supprimer(idHebergement);
    }

    public List<Hebergement> listerHebergements() throws Exception {
        return hebergementDAO.getAllHebergements();
    }

    public boolean modifierHebergement(Hebergement h) throws Exception {
        return hebergementDAO.mettreAJour(h);
    }
}
