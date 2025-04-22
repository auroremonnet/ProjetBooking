package controller;

import dao.ClientDAO;
import dao.AdministrateurDAO;
import model.Client;
import model.Administrateur;
import java.sql.Connection;

public class AuthController {
    protected ClientDAO clientDAO;
    protected AdministrateurDAO adminDAO;

    public AuthController(Connection conn) {
        this.clientDAO = new ClientDAO(conn);
        this.adminDAO = new AdministrateurDAO(conn);
    }

    public Client loginClient(String email, String motDePasse) throws Exception {
        return clientDAO.login(email, motDePasse);
    }

    public boolean registerClient(Client client) throws Exception {
        return clientDAO.register(client);
    }

    public Administrateur loginAdmin(String email, String motDePasse) throws Exception {
        Administrateur admin = adminDAO.login(email, motDePasse);
        if (admin != null) {
            System.out.println("Connexion admin r\u00e9ussie : " + admin.getNom() + " (" + admin.getPhoto() + ")");
        }
        return admin;
    }

    public boolean registerAdmin(Administrateur admin) throws Exception {
        return adminDAO.register(admin);
    }
}
