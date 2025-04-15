package controller;

import dao.ClientDAO;
import dao.AdministrateurDAO;
import model.Client;
import model.Administrateur;

import java.sql.Connection;

public class AuthController {
    private final ClientDAO clientDAO;
    private final AdministrateurDAO adminDAO;

    public AuthController(Connection conn) {
        this.clientDAO = new ClientDAO(conn);
        this.adminDAO = new AdministrateurDAO(conn);
    }

    public Client loginClient(String email, String mdp) throws Exception {
        return clientDAO.login(email, mdp);
    }

    public boolean registerClient(Client client) throws Exception {
        return clientDAO.register(client);
    }

    public Administrateur loginAdmin(String email, String mdp) throws Exception {
        return adminDAO.login(email, mdp);
    }
}
