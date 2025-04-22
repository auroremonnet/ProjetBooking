// dao/MailDAO.java
package dao;

import model.Mail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MailDAO {
    private final Connection connection;

    public MailDAO(Connection connection) {
        this.connection = connection;
    }

    /** Envoie un mail (admin→client ou client→admin) */
    public void envoyerMail(Mail mail) throws SQLException {
        String sql = "INSERT INTO Mail (idClient, objet, contenu, dateEnvoi) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mail.getIdClient());
            ps.setString(2, mail.getObjet());
            ps.setString(3, mail.getContenu());
            ps.executeUpdate();
        }
    }

    /** Liste des clients (idClient) ayant déjà une réservation */
    public List<Integer> getClientsAvecReservation() throws SQLException {
        String sql = "SELECT DISTINCT idClient FROM Reservation";
        List<Integer> ids = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("idClient"));
        }
        return ids;
    }

    /** Liste des clients (idClient) n’ayant jamais réservé */
    public List<Integer> getClientsSansReservation() throws SQLException {
        String sql = "SELECT idClient FROM Client "
                + "WHERE idClient NOT IN (SELECT DISTINCT idClient FROM Reservation)";
        List<Integer> ids = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("idClient"));
        }
        return ids;
    }

    /** Récupère les N derniers mails reçus par un client/admin */
    public List<Mail> getDerniersMailsPourClient(int idClient, int limit) throws SQLException {
        String sql = "SELECT idMail, idClient, objet, contenu, dateEnvoi "
                + "FROM Mail WHERE idClient = ? "
                + "ORDER BY dateEnvoi DESC LIMIT ?";
        List<Mail> mails = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mails.add(new Mail(
                            rs.getInt("idMail"),
                            rs.getInt("idClient"),
                            rs.getString("objet"),
                            rs.getString("contenu"),
                            rs.getTimestamp("dateEnvoi")
                    ));
                }
            }
        }
        return mails;
    }

    /** Récupère l’ID d’un administrateur via son prénom + nom */
    public int getIdAdminParPrenomNom(String prenom, String nom) throws SQLException {
        String sql = "SELECT idAdministrateur FROM Administrateur "
                + "WHERE prenom = ? AND nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prenom);
            ps.setString(2, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idAdministrateur");
                } else {
                    throw new SQLException("Administrateur introuvable : " + prenom + " " + nom);
                }
            }
        }
    }

    /** Envoie un mail client→admin (réutilise envoyerMail) */
    public void envoyerMailClientVersAdmin(int idClient, int idAdmin, String objet, String contenu) throws SQLException {
        // Ici on stocke l'idAdmin dans la colonne idClient de la table Mail
        envoyerMail(new Mail(idAdmin, objet, contenu));
    }
}