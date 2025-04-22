package dao;

import model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private final Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }


    public boolean createReservation(Reservation r) throws Exception {
        String sql = "INSERT INTO Reservation (dateArrivee, dateDepart, nombreAdultes, nombreEnfants, nombreChambres, idClient, idHebergement, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, r.getDateArrivee());
            ps.setDate(2, r.getDateDepart());
            ps.setInt(3, r.getNombreAdultes());
            ps.setInt(4, r.getNombreEnfants());
            ps.setInt(5, r.getNombreChambres());
            ps.setInt(6, r.getIdClient());
            ps.setInt(7, r.getIdHebergement());
            ps.setString(8, r.getStatut());
            return ps.executeUpdate() == 1;
        }
    }

    public List<Reservation> getReservationsByUser(int idClient) throws Exception {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE idClient = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new Reservation(
                            rs.getInt("idReservation"),
                            rs.getDate("dateArrivee"),
                            rs.getDate("dateDepart"),
                            rs.getInt("nombreAdultes"),
                            rs.getInt("nombreEnfants"),
                            rs.getInt("nombreChambres"),
                            rs.getInt("idClient"),
                            rs.getInt("idHebergement"),
                            rs.getString("statut"),
                            rs.getTimestamp("dateReservation")
                    ));
                }
            }
        }
        return liste;
    }

    public boolean annulerReservation(int idReservation) throws Exception {
        String sql = "DELETE FROM Reservation WHERE idReservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            return ps.executeUpdate() == 1;
        }
    }

    public List<Reservation> getReservationsDerniers3Mois(int idClient) throws Exception {
        List<Reservation> liste = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM Reservation WHERE idClient = ? AND dateReservation >= NOW() - INTERVAL 3 MONTH";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new Reservation(
                            rs.getInt("idReservation"),
                            rs.getDate("dateArrivee"),
                            rs.getDate("dateDepart"),
                            rs.getInt("nombreAdultes"),
                            rs.getInt("nombreEnfants"),
                            rs.getInt("nombreChambres"),
                            rs.getInt("idClient"),
                            rs.getInt("idHebergement"),
                            rs.getString("statut"),
                            rs.getTimestamp("dateReservation")
                    ));
                }
            }
        }
        return liste;
    }



    public Reservation findById(int idReservation) throws Exception {
        String sql = "SELECT * FROM Reservation WHERE idReservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getInt("idReservation"),
                            rs.getDate("dateArrivee"),
                            rs.getDate("dateDepart"),
                            rs.getInt("nombreAdultes"),
                            rs.getInt("nombreEnfants"),
                            rs.getInt("nombreChambres"),
                            rs.getInt("idClient"),
                            rs.getInt("idHebergement"),
                            rs.getString("statut"),
                            rs.getTimestamp("dateReservation")
                    );
                }
            }
        }
        return null;
    }

}
