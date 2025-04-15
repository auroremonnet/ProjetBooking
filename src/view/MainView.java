package view;

import controller.BookingController;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {

    private JTextArea output;
    private JTextField champLieu, champCategorie, champPrix;

    public MainView(BookingController controller) {
        setTitle("🌍 Booking 2025 - Recherche d'hébergements");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 10));
        getContentPane().setBackground(Color.decode("#f4f6f8")); // fond clair

        // 🟦 Titre
        JLabel titre = new JLabel("🏠  Hébergements disponibles", JLabel.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(40, 40, 40));
        add(titre, BorderLayout.NORTH);

        // 📝 Résultats
        output = new JTextArea();
        output.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        output.setMargin(new Insets(15, 15, 15, 15));
        output.setBackground(Color.WHITE);
        output.setForeground(Color.DARK_GRAY);
        output.setEditable(false);
        output.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(new JScrollPane(output), BorderLayout.CENTER);

        // 🔍 Filtres
        JPanel filtrePanel = new JPanel(new GridLayout(2, 4, 10, 5));
        filtrePanel.setBackground(Color.decode("#e9ecef"));

        champLieu = new JTextField();
        champCategorie = new JTextField();
        champPrix = new JTextField();

        filtrePanel.add(createLabel("📍 Lieu :"));
        filtrePanel.add(champLieu);
        filtrePanel.add(createLabel("🏷️ Catégorie :"));
        filtrePanel.add(champCategorie);
        filtrePanel.add(createLabel("💶 Prix max (€) :"));
        filtrePanel.add(champPrix);

        JButton btnChercher = new JButton("🔎 Chercher");
        styliseBouton(btnChercher, new Color(52, 152, 219));

        JButton btnAfficherTout = new JButton("📋 Tout afficher");
        styliseBouton(btnAfficherTout, new Color(39, 174, 96));

        btnChercher.addActionListener(e -> rechercher(controller));
        btnAfficherTout.addActionListener(e -> afficherHebergements(controller));

        filtrePanel.add(btnChercher);
        filtrePanel.add(btnAfficherTout);
        add(filtrePanel, BorderLayout.SOUTH);

        afficherHebergements(controller);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }

    private void styliseBouton(JButton bouton, Color couleur) {
        bouton.setBackground(couleur);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bouton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        bouton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void afficherHebergements(BookingController controller) {
        output.setText("");
        try {
            List<Hebergement> hebergements = controller.listerTous();
            if (hebergements.isEmpty()) {
                output.setText("⚠️ Aucun hébergement trouvé.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Hebergement h : hebergements) {
                    sb.append("• ")
                            .append(h.getNom())
                            .append(" | ").append(h.getAdresse())
                            .append(" | ").append(h.getPrix()).append(" €\n\n");
                }
                output.setText(sb.toString());
            }
        } catch (Exception e) {
            output.setText("❌ Erreur : " + e.getMessage());
        }
    }

    private void rechercher(BookingController controller) {
        output.setText("");
        try {
            String lieu = champLieu.getText().trim();
            String categorie = champCategorie.getText().trim();
            double prixMax = champPrix.getText().isEmpty()
                    ? Double.MAX_VALUE
                    : Double.parseDouble(champPrix.getText());

            List<Hebergement> resultats = controller.chercher(lieu, categorie, prixMax);

            if (resultats.isEmpty()) {
                output.setText("🔍 Aucun résultat pour votre recherche.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Hebergement h : resultats) {
                    sb.append("• ").append(h.getNom())
                            .append(" | ").append(h.getAdresse())
                            .append(" | ").append(h.getPrix()).append(" €\n\n");
                }
                output.setText(sb.toString());
            }

        } catch (NumberFormatException nfe) {
            output.setText("⚠️ Prix invalide !");
        } catch (Exception e) {
            output.setText("❌ Erreur de recherche : " + e.getMessage());
        }
    }
}
