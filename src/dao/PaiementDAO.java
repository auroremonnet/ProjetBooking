package dao;

import model.Paiement;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaiementDAO {
    private final Connection connection;

    public PaiementDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean enregistrerPaiement(Paiement paiement) throws Exception {
        String sql = "INSERT INTO Paiement (idReservation, montant, modePaiement, datePaiement) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, paiement.getIdReservation());
            ps.setDouble(2, paiement.getMontant());
            ps.setString(3, paiement.getModePaiement());
            ps.setTimestamp(4, paiement.getDatePaiement());
            return ps.executeUpdate() == 1;
        }
    }
}
