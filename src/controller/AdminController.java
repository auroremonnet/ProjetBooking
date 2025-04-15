package controller;

import dao.HebergementDAO;
import dao.ReductionDAO;
import model.Hebergement;
import model.Reduction;

import java.sql.Connection;
import java.util.List;

public class AdminController {
    private final HebergementDAO hebergementDAO;
    private final ReductionDAO reductionDAO;

    public AdminController(Connection conn) {
        this.hebergementDAO = new HebergementDAO(conn);
        this.reductionDAO = new ReductionDAO(conn);
    }

    public boolean ajouterHebergement(Hebergement h) throws Exception {
        return hebergementDAO.ajouter(h);
    }

    public boolean supprimerHebergement(int id) throws Exception {
        return hebergementDAO.supprimer(id);
    }

    public boolean ajouterReduction(Reduction r) throws Exception {
        return reductionDAO.ajouterReduction(r);
    }

    public List<Reduction> listerReductions() throws Exception {
        return reductionDAO.getAll();
    }
}
