package controller;

import dao.ClientDAO;
import dao.HebergementDAO;
import model.Client;
import model.Hebergement;

import java.sql.Connection;
import java.util.List;

public class AdminController {
    private final HebergementDAO hebergementDAO;
    private final ClientDAO clientDAO;

    public AdminController(Connection conn) {
        this.hebergementDAO = new HebergementDAO(conn);
        this.clientDAO = new ClientDAO(conn);
    }

    // Hebergements
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

    // Clients
    public boolean ajouterClient(Client client) throws Exception {
        return clientDAO.ajouter(client);
    }

    public boolean supprimerClient(int idClient) throws Exception {
        return clientDAO.supprimer(idClient);
    }

    public List<Client> listerClients() throws Exception {
        return clientDAO.getAllClients();
    }

    public boolean modifierClient(Client client) throws Exception {
        return clientDAO.mettreAJour(client);
    }
}
