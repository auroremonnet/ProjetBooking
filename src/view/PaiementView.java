// src/view/PaiementView.java
package view;

import dao.MoyenPaiementDAO;
import dao.PaiementDAO;
import model.MoyenPaiement;
import model.Paiement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Fenêtre de paiement CB (4 chiffres, MM/AA, CVV),
 * lit le solde en base et le débite via MoyenPaiementDAO.
 */
public class PaiementView extends JFrame {
    private final Connection conn;
    private final int        idClient, idResa;
    private final double     montant;

    private JTextField tfNum, tfExp, tfCvv;

    public PaiementView(Connection conn, int idClient, int idResa, double montant) {
        this.conn     = conn;
        this.idClient = idClient;
        this.idResa   = idResa;
        this.montant  = montant;
        setTitle("💳 Paiement Carte bancaire");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Montant
        JLabel lbl = new JLabel("💶 À payer : " + montant + " €");
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 16f));
        main.add(lbl);
        main.add(Box.createVerticalStrut(15));

        // Numéro
        main.add(new JLabel("Numéro de carte (4 chiffres) :"));
        tfNum = new JTextField(); main.add(tfNum);
        main.add(Box.createVerticalStrut(5));

        // Expiration
        main.add(new JLabel("Date d'expiration (MM/AA) :"));
        tfExp = new JTextField(); main.add(tfExp);
        main.add(Box.createVerticalStrut(5));

        // CVV
        main.add(new JLabel("CVV (3 chiffres) :"));
        tfCvv = new JTextField(); main.add(tfCvv);
        main.add(Box.createVerticalStrut(15));

        JButton btn = new JButton("Payer");
        btn.addActionListener(e -> doPayment());
        main.add(btn);

        add(main);
    }

    private void doPayment() {
        try {
            String num = tfNum.getText().trim();
            String exp = tfExp.getText().trim();
            String cvv = tfCvv.getText().trim();

            // validations
            if (!num.matches("\\d{4}"))
                throw new IllegalArgumentException("Numéro de carte : 4 chiffres attendus.");
            if (!exp.matches("(0[1-9]|1[0-2])/\\d{2}"))
                throw new IllegalArgumentException("Expiration invalide (MM/AA).");
            if (!cvv.matches("\\d{3}"))
                throw new IllegalArgumentException("CVV : 3 chiffres attendus.");

            // découpe MM/AA
            String[] parts = exp.split("/");
            int mois  = Integer.parseInt(parts[0]);
            int annee = 2000 + Integer.parseInt(parts[1]);

            // 1) créer ou récupérer le moyen en base
            MoyenPaiementDAO mdao = new MoyenPaiementDAO(conn);
            MoyenPaiement moyen = new MoyenPaiement(
                    0,
                    idClient,
                    "Carte bancaire",
                    num,
                    mois,
                    annee,
                    cvv,
                    0.0    // solde initial (sera lu + débité dans debiter())
            );
            int idM = mdao.trouverOuCreer(moyen);

            // 2) enregistrer le paiement (valide CVV + débite le solde)
            Timestamp now = Timestamp.from(Instant.now());
            Paiement pmt = new Paiement(
                    0,
                    idResa,
                    montant,
                    "Carte bancaire",
                    now,
                    idM,
                    cvv
            );

            PaiementDAO pdao = new PaiementDAO(conn);
            boolean ok = pdao.enregistrerPaiement(pmt);

            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Paiement effectué !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "❌ Fonds insuffisants ou CVV invalide",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(
                    this,
                    iae.getMessage(),
                    "Erreur de saisie",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur technique : " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
