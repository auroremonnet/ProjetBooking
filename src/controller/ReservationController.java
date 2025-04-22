package controller;

import dao.ReservationDAO;
import model.Reservation;

import java.sql.*;
import java.util.List;

public class ReservationController {
    private final ReservationDAO reservationDAO;
    private final Connection conn;

    public ReservationController(Connection conn) {
        this.reservationDAO = new ReservationDAO(conn);
        this.conn = conn;  // Ajouté pour pouvoir faire des requêtes directes
    }

    public boolean reserver(Reservation reservation) throws Exception {
        return reservationDAO.createReservation(reservation);
    }

    public List<Reservation> historiqueParClient(int idClient) throws Exception {
        return reservationDAO.getReservationsByUser(idClient);
    }

    public boolean annuler(int idReservation) throws Exception {
        return reservationDAO.annulerReservation(idReservation);
    }

    public List<Reservation> reservations3DerniersMois(int idClient) throws Exception {
        return reservationDAO.getReservationsDerniers3Mois(idClient);
    }

    // ✅ MÉTHODE À AJOUTER : vérifie si un client a réservé un hébergement
    public boolean aReserveHebergement(int idClient, int idHebergement) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE idClient = ? AND idHebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.setInt(2, idHebergement);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
