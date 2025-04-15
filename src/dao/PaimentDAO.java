import java.sql.*;

public class PaiementDAO {
    private Connection conn;

    public PaiementDAO(Connection conn) {
        this.conn = conn;
    }

    // Ajoute un nouveau paiement
    public boolean addPaiement(Paiement paiement) {
        String sql = "INSERT INTO Paiement (idReservation, montant, modePaiement, datePaiement) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, paiement.getIdReservation());
            ps.setBigDecimal(2, paiement.getMontant());
            ps.setString(3, paiement.getModePaiement());
            ps.setTimestamp(4, Timestamp.valueOf(paiement.getDatePaiement()));
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création du paiement a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                if (generatedKeys.next()){
                    paiement.setIdPaiement(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Récupère un paiement via l'id de la réservation (supposé ici 1:1)
    public Paiement getPaiementByReservationId(int idReservation) {
        String sql = "SELECT * FROM Paiement WHERE idReservation = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Paiement(
                        rs.getInt("idPaiement"),
                        rs.getInt("idReservation"),
                        rs.getBigDecimal("montant"),
                        rs.getString("modePaiement"),
                        rs.getTimestamp("datePaiement").toLocalDateTime()
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}

