package view;

import controller.BookingController;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MainView extends JFrame {

    private JTextArea output;
    private JTextField champLieu, champCategorie, champPrix;

    public MainView(BookingController controller) {
        setTitle("Booking 2025 - Liste des hébergements");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("🏠 Hébergements disponibles", JLabel.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        add(titre, BorderLayout.NORTH);

        output = new JTextArea();
        output.setEditable(false);
        add(new JScrollPane(output), BorderLayout.CENTER);

        // 🔍 Zone de recherche
        JPanel filtrePanel = new JPanel(new GridLayout(2, 4, 10, 5));
        champLieu = new JTextField();
        champCategorie = new JTextField();
        champPrix = new JTextField();

        filtrePanel.add(new JLabel("Lieu :"));
        filtrePanel.add(champLieu);
        filtrePanel.add(new JLabel("Catégorie :"));
        filtrePanel.add(champCategorie);
        filtrePanel.add(new JLabel("Prix max (€) :"));
        filtrePanel.add(champPrix);

        JButton btnChercher = new JButton("🔍 Chercher");
        btnChercher.addActionListener(e -> rechercher(controller));
        filtrePanel.add(btnChercher);

        JButton btnAfficherTout = new JButton("📋 Tout afficher");
        btnAfficherTout.addActionListener(e -> afficherHebergements(controller));
        filtrePanel.add(btnAfficherTout);

        add(filtrePanel, BorderLayout.SOUTH);

        // Première charge
        afficherHebergements(controller);

        setVisible(true);
    }

    private void afficherHebergements(BookingController controller) {
        output.setText("");
        try {
            List<Hebergement> hebergements = controller.listerTous();
            if (hebergements.isEmpty()) {
                output.setText("Aucun hébergement trouvé.");
            } else {
                for (Hebergement h : hebergements) {
                    output.append("- " + h.getNom() + " | " + h.getAdresse() + " | " + h.getPrix() + " €\n");
                }
            }
        } catch (Exception e) {
            output.setText("Erreur lors du chargement : " + e.getMessage());
        }
    }

    private void rechercher(BookingController controller) {
        output.setText("");
        try {
            String lieu = champLieu.getText().trim();
            String categorie = champCategorie.getText().trim();
            double prixMax = champPrix.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(champPrix.getText());

            List<Hebergement> resultats = controller.chercher(lieu, categorie, prixMax);

            if (resultats.isEmpty()) {
                output.setText("Aucun hébergement trouvé pour votre recherche.");
            } else {
                for (Hebergement h : resultats) {
                    output.append("- " + h.getNom() + " | " + h.getAdresse() + " | " + h.getPrix() + " €\n");
                }
            }

        } catch (NumberFormatException nfe) {
            output.setText("⚠️ Prix invalide !");
        } catch (Exception e) {
            output.setText("Erreur lors de la recherche : " + e.getMessage());
        }
    }
}
