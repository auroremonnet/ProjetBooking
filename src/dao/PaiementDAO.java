// src/dao/PaiementDAO.java
package dao;

import model.Paiement;
import model.Client;
import model.ReductionClient;

import java.sql.*;
import java.util.Optional;

/**
 * DAO pour valider (CVV + solde), appliquer la réduction, puis insérer le paiement.
 */
public class PaiementDAO {
    private final Connection conn;

    public PaiementDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean enregistrerPaiement(Paiement pmt) throws SQLException {
        MoyenPaiementDAO mdao = new MoyenPaiementDAO(conn);
        ClientDAO clientDAO = new ClientDAO(conn);
        ReservationDAO reservationDAO = new ReservationDAO(conn);
        ReductionClientDAO reductionDAO = new ReductionClientDAO(conn);

        try {
            conn.setAutoCommit(false);

            Integer idMoyen = pmt.getIdMoyenPaiement();

            int idClient;
            try {
                idClient = reservationDAO.getClientIdFromReservation(pmt.getIdReservation());
            } catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
                return false;
            }

            Client client = clientDAO.findById(idClient);
            String typeClient = client.getTypeClient();
            ReductionClient reduction = reductionDAO.findByType(typeClient).orElse(null);

            double tauxReduction = (reduction != null) ? reduction.getTauxReduction() : 5.0;
            double montantInitial = pmt.getMontant();
            double montantReduit = montantInitial * (1 - tauxReduction / 100.0);

            // Appliquer la réduction au paiement
            pmt.setMontant(montantReduit);
            pmt.setTauxReduction(tauxReduction);

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
            // 3) Insertion du paiement avec taux de réduction
            String sql = """
                INSERT INTO Paiement
                  (idReservation, montant, modePaiement, datePaiement, idMoyenPaiement, cvv, taux_reduction)
                VALUES (?, ?, ?, ?, ?, ?, ?)
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
                ps.setDouble(7, tauxReduction);
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
