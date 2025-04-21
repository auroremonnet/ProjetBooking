// src/dao/PaiementDAO.java
package dao;

import model.Paiement;
import java.sql.*;

/**
 * DAO pour valider (CVV + solde) puis insérer le paiement dans Paiement.
 */
public class PaiementDAO {
    private final Connection conn;
    public PaiementDAO(Connection conn) { this.conn = conn; }

    public boolean enregistrerPaiement(Paiement pmt) throws SQLException {
        MoyenPaiementDAO mdao = new MoyenPaiementDAO(conn);
        try {
            conn.setAutoCommit(false);

            Integer idMoyen = pmt.getIdMoyenPaiement();

            // 1) Validation CVV
            if (idMoyen != null && !mdao.validerCvv(idMoyen, pmt.getCvv())) {
                conn.rollback();
                return false;
            }
            // 2) Débit solde
            if (idMoyen != null && !mdao.debiter(idMoyen, pmt.getMontant())) {
                conn.rollback();
                return false;
            }
            // 3) Insertion du paiement
            String sql = """
                INSERT INTO Paiement
                  (idReservation, montant, modePaiement, datePaiement, idMoyenPaiement, cvv)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, pmt.getIdReservation());
                ps.setDouble(2, pmt.getMontant());
                ps.setString(3, pmt.getModePaiement());
                ps.setTimestamp(4, pmt.getDatePaiement());
                if (idMoyen != null) {
                    ps.setInt(5, idMoyen);
                    ps.setString(6, pmt.getCvv());
                } else {
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.VARCHAR);
                }
                ps.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
