package dao;

import model.Reservation;
import java.sql.SQLException;
import java.util.List;

public interface ReservationDAO {
    // Récupère une réservation par son id
    Reservation findById(int id) throws SQLException;

    // Récupère toutes les réservations
    List<Reservation> findAll() throws SQLException;

    // Insère une nouvelle réservation
    boolean insert(Reservation reservation) throws SQLException;

    // Met à jour une réservation existante
    boolean update(Reservation reservation) throws SQLException;

    // Supprime une réservation par son id
    boolean delete(int id) throws SQLException;
}
