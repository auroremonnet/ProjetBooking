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

    public void envoyerMail(Mail mail) throws SQLException {
        String sql = """
            INSERT INTO Mail
              (idClient, idAdministrateur, objet, contenu, dateEnvoi)
            VALUES
              (?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mail.getIdClient());
            ps.setInt(2, mail.getIdAdministrateur());
            ps.setString(3, mail.getObjet());
            ps.setString(4, mail.getContenu());
            ps.executeUpdate();
        }
    }

    public void envoyerMailClientVersAdmin(int idClient, int idAdmin, String objet, String contenu) throws SQLException {
        envoyerMail(new Mail(idClient, idAdmin, objet, contenu));
    }

    public void envoyerMailAdminVersClient(int idAdmin, int idClient, String objet, String contenu) throws SQLException {
        envoyerMail(new Mail(idClient, idAdmin, objet, contenu));
    }

    public int getIdAdminParPrenomNom(String prenom, String nom) throws SQLException {
        String sql = "SELECT idAdministrateur FROM Administrateur WHERE prenom = ? AND nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prenom);
            ps.setString(2, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("idAdministrateur");
        }
        throw new SQLException("Administrateur introuvable : " + prenom + " " + nom);
    }

    public String getClientNomPrenom(int idClient) throws SQLException {
        String sql = "SELECT prenom, nom FROM Client WHERE idClient = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("prenom") + " " + rs.getString("nom");
        }
        return "Client#" + idClient;
    }

    public String getAdminNomPrenom(int idAdmin) throws SQLException {
        String sql = "SELECT prenom, nom FROM Administrateur WHERE idAdministrateur = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idAdmin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("prenom") + " " + rs.getString("nom");
        }
        return "Admin#" + idAdmin;
    }

    public List<Integer> getClientsAvecReservation() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT idClient FROM Reservation";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("idClient"));
        }
        return ids;
    }

    public List<Integer> getClientsSansReservation() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = """
            SELECT idClient FROM Client
             WHERE idClient NOT IN (SELECT DISTINCT idClient FROM Reservation)
        """;
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("idClient"));
        }
        return ids;
    }

    // ✅ Clients de type "ancien"
    public List<Integer> getClientsAnciens() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT idClient FROM Client WHERE typeClient = 'ancien'";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ids.add(rs.getInt("idClient"));
            }
        }
        return ids;
    }

    public List<Mail> getMailsRecusParAdmin(int idAdmin, int limit) throws SQLException {
        String sql = """
            SELECT * FROM Mail
             WHERE idAdministrateur = ? AND idClient IS NOT NULL
             ORDER BY dateEnvoi DESC
             LIMIT ?
        """;
        return fetchMails(sql, idAdmin, limit);
    }

    public List<Mail> getMailsEnvoyesParAdmin(int idAdmin, int limit) throws SQLException {
        String sql = """
            SELECT * FROM Mail
             WHERE idAdministrateur = ? AND idClient IS NOT NULL
             ORDER BY dateEnvoi DESC
             LIMIT ?
        """;
        return fetchMails(sql, idAdmin, limit);
    }

    public List<Mail> getMailsRecusParClient(int clientId, int limit) throws SQLException {
        String sql = """
            SELECT * FROM Mail
             WHERE idClient = ? AND idAdministrateur IS NOT NULL
             ORDER BY dateEnvoi DESC
             LIMIT ?
        """;
        return fetchMails(sql, clientId, limit);
    }

    public List<Mail> getMailsEnvoyesParClient(int clientId, int limit) throws SQLException {
        String sql = """
            SELECT * FROM Mail
             WHERE idClient = ? AND idAdministrateur IS NOT NULL
             ORDER BY dateEnvoi DESC
             LIMIT ?
        """;
        return fetchMails(sql, clientId, limit);
    }

    private List<Mail> fetchMails(String sql, int id, int limit) throws SQLException {
        List<Mail> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Mail(
                        rs.getInt("idMail"),
                        rs.getInt("idClient"),
                        rs.getInt("idAdministrateur"),
                        rs.getString("objet"),
                        rs.getString("contenu"),
                        rs.getTimestamp("dateEnvoi")
                ));
            }
        }
        return list;
    }
}
