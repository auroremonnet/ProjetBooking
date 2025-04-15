import java.sql.*;

public class ReservationDAO {
    private Connection conn;

    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }

    // Ajoute une nouvelle réservation
    public boolean addReservation(Reservation reservation) {
        String sql = "INSERT INTO Reservation (dateArrivee, dateDepart, nombreAdultes, nombreEnfants, nombreChambres, idClient, idHebergement, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(reservation.getDateArrivee()));
            ps.setDate(2, Date.valueOf(reservation.getDateDepart()));
            ps.setInt(3, reservation.getNombreAdultes());
            ps.setInt(4, reservation.getNombreEnfants());
            ps.setInt(5, reservation.getNombreChambres());
            ps.setInt(6, reservation.getIdClient());
            ps.setInt(7, reservation.getIdHebergement());
            ps.setString(8, reservation.getStatut());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0){
                throw new SQLException("La création de la réservation a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                if (generatedKeys.next()){
                    reservation.setIdReservation(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Récupère une réservation par son identifiant
    public Reservation getReservationById(int idReservation) {
        String sql = "SELECT * FROM Reservation WHERE idReservation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Reservation(
                        rs.getInt("idReservation"),
                        rs.getDate("dateArrivee").toLocalDate(),
                        rs.getDate("dateDepart").toLocalDate(),
                        rs.getInt("nombreAdultes"),
                        rs.getInt("nombreEnfants"),
                        rs.getInt("nombreChambres"),
                        rs.getInt("idClient"),
                        rs.getInt("idHebergement"),
                        rs.getString("statut"),
                        rs.getTimestamp("dateReservation").toLocalDateTime()
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Met à jour une réservation
    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE Reservation SET dateArrivee = ?, dateDepart = ?, nombreAdultes = ?, nombreEnfants = ?, nombreChambres = ?, idClient = ?, idHebergement = ?, statut = ? WHERE idReservation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reservation.getDateArrivee()));
            ps.setDate(2, Date.valueOf(reservation.getDateDepart()));
            ps.setInt(3, reservation.getNombreAdultes());
            ps.setInt(4, reservation.getNombreEnfants());
            ps.setInt(5, reservation.getNombreChambres());
            ps.setInt(6, reservation.getIdClient());
            ps.setInt(7, reservation.getIdHebergement());
            ps.setString(8, reservation.getStatut());
            ps.setInt(9, reservation.getIdReservation());
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Supprime une réservation
    public boolean deleteReservation(int idReservation) {
        String sql = "DELETE FROM Reservation WHERE idReservation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
