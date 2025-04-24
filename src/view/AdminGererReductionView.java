package view;

import controller.AdminController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminGererReductionView extends JFrame {
    private final AdminController controller;
    private JSpinner spinnerTaux;
    private JButton btnEnregistrer;

    public AdminGererReductionView(AdminController controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        setTitle("Gérer les Réductions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header comme AccueilAdminView
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(600, 70));
        JLabel titre = new JLabel("Réduction", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 34));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Contenu centré avec bord arrondi
        JPanel content = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(89, 141, 144));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        content.setLayout(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        content.setBackground(new Color(0, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel label = new JLabel("Taux de réduction pour les anciens clients (%):");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        content.add(label, gbc);

        // Spinner
        spinnerTaux = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        gbc.gridy++;
        content.add(spinnerTaux, gbc);

        // Bouton enregistrer
        btnEnregistrer = new JButton("Enregistrer");
        gbc.gridy++;
        content.add(btnEnregistrer, gbc);

        // Charger la valeur actuelle depuis la base
        try {
            double taux = controller.getTauxReductionAncien();
            spinnerTaux.setValue(taux);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du taux", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Action bouton
        btnEnregistrer.addActionListener((ActionEvent e) -> {
            double nouveauTaux = ((Number) spinnerTaux.getValue()).doubleValue();
            boolean success = controller.modifierTauxReductionAncien(nouveauTaux);
            if (success) {
                JOptionPane.showMessageDialog(this, "Taux mis à jour avec succès : " + nouveauTaux + "%", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Échec de la mise à jour.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(content, BorderLayout.CENTER);

        // Bouton retour
        JButton btnRetour = new JButton("Retour");
        btnRetour.setBackground(new Color(122, 194, 199));
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setFont(new Font("Arial", Font.BOLD, 16));
        btnRetour.setFocusPainted(false);
        btnRetour.setPreferredSize(new Dimension(120, 40));
        btnRetour.addActionListener(e -> {
            dispose();
            new AccueilAdminView(controller.getAdministrateur(), controller.getConnection());
        });

        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footer.add(btnRetour);
        add(footer, BorderLayout.SOUTH);
    }
}