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
        this.conn = conn;  // Pour les accès directs
    }

    public boolean reserver(Reservation reservation) throws Exception {
        boolean ok = reservationDAO.createReservation(reservation);
        if (ok) {
            updateClientToAncien(reservation.getIdClient());
        }
        return ok;
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

    // ✅ Mise à jour automatique du type client
    private void updateClientToAncien(int idClient) {
        String sql = "UPDATE Client SET typeClient = 'ancien' WHERE idClient = ? AND typeClient = 'nouveau'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Logiquement, on ne bloque pas la réservation même si cette mise à jour échoue
        }
    }
}
