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
        String sql = """
            INSERT INTO Mail
              (idClient, idAdministrateur, objet, contenu, dateEnvoi)
            VALUES
              (?,        ?,               ?,     ?,       CURRENT_TIMESTAMP)
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mail.getIdClient());
            ps.setInt(2, mail.getIdAdministrateur());
            ps.setString(3, mail.getObjet());
            ps.setString(4, mail.getContenu());
            ps.executeUpdate();
        }
    }

    /** Récupère l’ID administrateur à partir de son prénom + nom */
    public int getIdAdminParPrenomNom(String prenom, String nom) throws SQLException {
        String sql = """
            SELECT idAdministrateur
              FROM Administrateur
             WHERE prenom = ? AND nom = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prenom);
            ps.setString(2, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idAdministrateur");
                }
            }
        }
        throw new SQLException("Administrateur introuvable : " + prenom + " " + nom);
    }

    /** Récupère le nom + prénom d’un client via son ID */
    public String getClientNomPrenom(int idClient) throws SQLException {
        String sql = "SELECT prenom, nom FROM Client WHERE idClient = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("prenom") + " " + rs.getString("nom");
                }
            }
        }
        return "Client#" + idClient;
    }

    /** Récupère le nom + prénom d’un administrateur via son ID */
    public String getAdminNomPrenom(int idAdmin) throws SQLException {
        String sql = "SELECT prenom, nom FROM Administrateur WHERE idAdministrateur = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idAdmin);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("prenom") + " " + rs.getString("nom");
                }
            }
        }
        return "Admin#" + idAdmin;
    }

    /** Clients ayant déjà une réservation */
    public List<Integer> getClientsAvecReservation() throws SQLException {
        String sql = "SELECT DISTINCT idClient FROM Reservation";
        List<Integer> ids = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("idClient"));
        }
        return ids;
    }

    /** Clients n’ayant jamais réservé */
    public List<Integer> getClientsSansReservation() throws SQLException {
        String sql = """
            SELECT idClient
              FROM Client
             WHERE idClient NOT IN (
               SELECT DISTINCT idClient FROM Reservation
             )
            """;
        List<Integer> ids = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getInt("idClient"));
        }
        return ids;
    }

    /** N derniers mails reçus PAR un administrateur (destinataires) */
    public List<Mail> getDerniersMailsRecusPourAdmin(int adminId, int limit) throws SQLException {
        String sql = """
            SELECT idMail, idClient, idAdministrateur, objet, contenu, dateEnvoi
              FROM Mail
             WHERE idClient = ?
             ORDER BY dateEnvoi DESC
             LIMIT ?
            """;
        List<Mail> mails = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mails.add(new Mail(
                            rs.getInt("idMail"),
                            rs.getInt("idClient"),
                            rs.getInt("idAdministrateur"),
                            rs.getString("objet"),
                            rs.getString("contenu"),
                            rs.getTimestamp("dateEnvoi")
                    ));
                }
            }
        }
        return mails;
    }
    public List<Mail> getDerniersMailsRecusPourClient(int clientId, int limit) throws SQLException {
        String sql = """
            SELECT idMail, idClient, idAdministrateur, objet, contenu, dateEnvoi
              FROM Mail
             WHERE idClient = ?
             ORDER BY dateEnvoi DESC
             LIMIT ?
            """;
        List<Mail> mails = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mails.add(new Mail(
                            rs.getInt("idMail"),
                            rs.getInt("idClient"),
                            rs.getInt("idAdministrateur"),
                            rs.getString("objet"),
                            rs.getString("contenu"),
                            rs.getTimestamp("dateEnvoi")
                    ));
                }
            }
        }
        return mails;
    }

    /**
     * Pour envoyer un mail client→admin en réutilisant envoyerMail(...).
     * @param idClient  l’ID du client (expéditeur)
     * @param idAdmin   l’ID de l’admin (destinataire)
     * @param objet     l’objet du mail
     * @param contenu   le corps du message
     */
    public void envoyerMailClientVersAdmin(int idClient, int idAdmin,
                                           String objet, String contenu) throws SQLException {
        // on crée un Mail où idClient = expéditeur (le client) ; idAdministrateur = destinataire (l’admin)
        envoyerMail(new Mail(idClient, idAdmin, objet, contenu));
    }

    /** N derniers mails envoyés PAR un administrateur (expéditeurs) */
    public List<Mail> getDerniersMailsEnvoyesParAdmin(int adminId, int limit) throws SQLException {
        String sql = """
            SELECT idMail, idClient, idAdministrateur, objet, contenu, dateEnvoi
              FROM Mail
             WHERE idAdministrateur = ?
             ORDER BY dateEnvoi DESC
             LIMIT ?
            """;
        List<Mail> mails = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mails.add(new Mail(
                            rs.getInt("idMail"),
                            rs.getInt("idClient"),
                            rs.getInt("idAdministrateur"),
                            rs.getString("objet"),
                            rs.getString("contenu"),
                            rs.getTimestamp("dateEnvoi")
                    ));
                }
            }
        }
        return mails;
    }
}
