package controller;

import dao.ReservationDAO;
import model.Reservation;

import java.sql.Connection;
import java.util.List;

public class ReservationController {
    private final ReservationDAO reservationDAO;

    public ReservationController(Connection conn) {
        this.reservationDAO = new ReservationDAO(conn);
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
}
