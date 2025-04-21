// src/dao/MoyenPaiementDAO.java
package dao;

import model.MoyenPaiement;

import java.sql.*;

/**
 * DAO pour gérer les moyens de paiement :
 *  - trouver ou créer un moyen existant,
 *  - valider le CVV,
 *  - débiter le solde.
 */
public class MoyenPaiementDAO {
    private final Connection conn;

    public MoyenPaiementDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Cherche un moyen de paiement existant pour ce client avec ces 4 derniers chiffres et cette date.
     * Si rien n'est trouvé, insère un nouvel enregistrement avec le solde initial en base.
     * @return l'idMoyenPaiement
     */
    public int trouverOuCreer(MoyenPaiement m) throws SQLException {
        // 1) on essaie de retrouver un enregistrement identique
        String select = """
            SELECT idMoyenPaiement
              FROM moyenpaiement
             WHERE idClient = ?
               AND numeroLast4 = ?
               AND expMois = ?
               AND expAnnee = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setInt   (1, m.getIdClient());
            ps.setString(2, m.getNumeroLast4());
            ps.setInt   (3, m.getExpMois());
            ps.setInt   (4, m.getExpAnnee());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idMoyenPaiement");
                }
            }
        }

        // 2) sinon on insère un nouveau moyen avec le solde initial fourni
        String insert = """
            INSERT INTO moyenpaiement
              (idClient, typeCarte, numeroLast4, expMois, expAnnee, cvv, solde)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, m.getIdClient());
            ps.setString(2, m.getTypeCarte());
            ps.setString(3, m.getNumeroLast4());
            ps.setInt   (4, m.getExpMois());
            ps.setInt   (5, m.getExpAnnee());
            ps.setString(6, m.getCvv());
            ps.setDouble(7, m.getSolde());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("Création du moyen de paiement échouée, pas de clé générée.");
            }
        }
    }

    /**
     * Vérifie que le CVV fourni correspond à celui enregistré en base.
     */
    public boolean validerCvv(int idMoyen, String cvv) throws SQLException {
        String sql = "SELECT cvv FROM moyenpaiement WHERE idMoyenPaiement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMoyen);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && cvv.equals(rs.getString("cvv"));
            }
        }
    }

    /**
     * Tente de débiter le solde du moyen.
     * @return true si le solde était suffisant et a été mis à jour, false sinon.
     */
    public boolean debiter(int idMoyen, double montant) throws SQLException {
        String sel = "SELECT solde FROM moyenpaiement WHERE idMoyenPaiement = ? FOR UPDATE";
        try (PreparedStatement ps1 = conn.prepareStatement(sel)) {
            ps1.setInt(1, idMoyen);
            try (ResultSet rs = ps1.executeQuery()) {
                if (!rs.next()) return false;
                double solde = rs.getDouble("solde");
                if (solde < montant) return false;
                String upd = "UPDATE moyenpaiement SET solde = ? WHERE idMoyenPaiement = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(upd)) {
                    ps2.setDouble(1, solde - montant);
                    ps2.setInt(2, idMoyen);
                    return ps2.executeUpdate() == 1;
                }
            }
        }
    }
}
