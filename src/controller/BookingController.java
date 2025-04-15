package controller;

import dao.HebergementDAO;
import model.Hebergement;

import java.sql.Connection;
import java.util.List;

public class BookingController {
    private HebergementDAO hebergementDAO;

    public BookingController(Connection connection) {
        this.hebergementDAO = new HebergementDAO(connection);
    }

    public List<Hebergement> afficherTousLesHebergements() throws Exception {
        return hebergementDAO.getAllHebergements();
    }
}
