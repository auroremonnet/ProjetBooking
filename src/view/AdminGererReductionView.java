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
        setSize(400, 180);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Taux de réduction pour les anciens clients (%):"), gbc);

        // Spinner
        spinnerTaux = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        gbc.gridx = 1;
        panel.add(spinnerTaux, gbc);

        // Bouton
        btnEnregistrer = new JButton("Enregistrer");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnEnregistrer, gbc);

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

        add(panel);
    }
}
