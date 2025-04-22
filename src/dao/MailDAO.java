package dao;

import model.Mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class MailDAO {
    private final Connection connection;

    public MailDAO(Connection connection) {
        this.connection = connection;
    }

    public void envoyerMail(Mail mail) throws Exception {
        String sql = "INSERT INTO Mail (idClient, objet, contenu) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, mail.getIdClient());
            ps.setString(2, mail.getObjet());
            ps.setString(3, mail.getContenu());
            ps.executeUpdate();
        }
    }

    public List<Integer> getClientsAvecReservation() throws Exception {
        String sql = "SELECT DISTINCT idClient FROM Reservation";
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("idClient"));
            }
        }
        return ids;
    }
    public List<Integer> getClientsSansReservation() throws Exception {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT idClient FROM Client WHERE idClient NOT IN (SELECT DISTINCT idClient FROM Reservation)";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("idClient"));
            }
        }
        return ids;
    }
    public List<Mail> getDerniersMailsPourClient(int idClient, int limit) throws Exception {
        List<Mail> mails = new ArrayList<>();
        String sql = "SELECT * FROM mail WHERE idClient = ? ORDER BY dateEnvoi DESC LIMIT ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mails.add(new Mail(
                        rs.getInt("idClient"),
                        rs.getString("objet"),
                        rs.getString("message")
                ));
            }
        }
        return mails;
    }

    public int getIdAdminParPrenomNom(String prenom, String nom) throws Exception {
        String sql = "SELECT idAdmin FROM administrateur WHERE prenom = ? AND nom = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prenom);
            ps.setString(2, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("idAdmin");
            else throw new Exception("Administrateur introuvable.");
        }
    }

    public void envoyerMailClientVersAdmin(int idClient, int idAdmin, String objet, String message) throws Exception {
        String sql = "INSERT INTO mail (idClient, idAdmin, objet, message, dateEnvoi) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.setInt(2, idAdmin);
            ps.setString(3, objet);
            ps.setString(4, message);
            ps.executeUpdate();
        }
    }


}
