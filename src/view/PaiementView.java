package view;

import dao.PaiementDAO;
import model.Paiement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;

public class PaiementView extends JFrame {

    public PaiementView(Connection connection, int idReservation, double montant) {
        setTitle("💳 Paiement");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER BLEU ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(500, 60));

        JLabel titre = new JLabel("Paiement", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === CONTENU PRINCIPAL ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === PANEL MONTANT ===
        JPanel recapPanel = createRoundedPanel();
        recapPanel.setMaximumSize(new Dimension(400, 80));
        JLabel lblMontant = new JLabel("💶 Montant total à payer : " + montant + " €");
        lblMontant.setFont(new Font("Arial", Font.BOLD, 16));
        recapPanel.add(lblMontant);
        mainPanel.add(recapPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // === PANEL MODE DE PAIEMENT ===
        JPanel modePanel = createRoundedPanel();
        modePanel.setMaximumSize(new Dimension(400, 100));
        modePanel.add(new JLabel("💳 Choisissez un mode de paiement :"));

        String[] modes = {"Carte bancaire", "PayPal", "Virement", "Espèces"};
        JComboBox<String> cbModes = new JComboBox<>(modes);
        cbModes.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbModes.setMaximumSize(new Dimension(200, 30));
        modePanel.add(cbModes);

        mainPanel.add(modePanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // === BOUTON VALIDER ===
        JButton btnPayer = new JButton("Valider le paiement");
        btnPayer.setBackground(Color.decode("#e3e3e3"));
        btnPayer.setFocusPainted(false);
        btnPayer.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPayer.addActionListener(e -> {
            String mode = (String) cbModes.getSelectedItem();
            Timestamp date = Timestamp.from(Instant.now());

            Paiement paiement = new Paiement(0, idReservation, montant, mode, date);
            try {
                PaiementDAO paiementDAO = new PaiementDAO(connection);
                boolean success = paiementDAO.enregistrerPaiement(paiement);
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Paiement effectué avec succès !");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Paiement échoué.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors du paiement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(btnPayer);
        mainPanel.add(Box.createVerticalStrut(15));

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#7ac2c7"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }
}