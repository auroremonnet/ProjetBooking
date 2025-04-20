package view;

import controller.AdminController;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AdminDashboardView extends JFrame {
    private final AdminController controller;

    private JTextField nomField, adresseField, localisationField, prixField,
            categorieField, photoField, optionsField, capaciteField, litsField, idSuppressionField;

    public AdminDashboardView(Connection conn) {
        this.controller = new AdminController(conn);

        setTitle("🛠️ Admin - Gestion des hébergements");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chargerHebergements();
        add(buildFormPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void chargerHebergements() {
        try {
            List<Hebergement> liste = controller.listerHebergements();

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.WHITE);

            for (Hebergement h : liste) {
                JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));
                ligne.setBackground(Color.WHITE);
                ligne.setPreferredSize(new Dimension(950, 100));

                // 📷 Image
                String imagePath = "resources/images/" + h.getPhotos();
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaled));

                // 🧾 Texte sur une seule ligne
                String info = String.format(
                        "<html><b>ID:</b> %d | <b>%s</b> | %s — %.2f €<br>Catégorie: %s | Options: %s | Capacité: %d | Lits: %d</html>",
                        h.getIdHebergement(), h.getNom(), h.getAdresse(), h.getPrix(),
                        h.getCategorie(), h.getOptions(), h.getCapaciteMax(), h.getNombreLits()
                );

                JLabel label = new JLabel(info);
                label.setPreferredSize(new Dimension(800, 80));

                ligne.add(imageLabel);
                ligne.add(label);
                contentPanel.add(ligne);
            }

            JScrollPane scrollPane = new JScrollPane(contentPanel);

            getContentPane().removeAll();
            add(scrollPane, BorderLayout.CENTER);
            add(buildFormPanel(), BorderLayout.SOUTH);

            revalidate();
            repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(6, 4, 5, 5));
        formPanel.setBackground(new Color(245, 245, 245));

        nomField = new JTextField();
        adresseField = new JTextField();
        localisationField = new JTextField();
        prixField = new JTextField();
        categorieField = new JTextField();
        photoField = new JTextField();
        optionsField = new JTextField();
        capaciteField = new JTextField();
        litsField = new JTextField();
        idSuppressionField = new JTextField();

        formPanel.add(new JLabel("Nom"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Adresse"));
        formPanel.add(adresseField);
        formPanel.add(new JLabel("Localisation"));
        formPanel.add(localisationField);
        formPanel.add(new JLabel("Prix (€)"));
        formPanel.add(prixField);
        formPanel.add(new JLabel("Catégorie"));
        formPanel.add(categorieField);
        formPanel.add(new JLabel("Photo (nom.jpg)"));
        formPanel.add(photoField);
        formPanel.add(new JLabel("Options"));
        formPanel.add(optionsField);
        formPanel.add(new JLabel("Capacité max"));
        formPanel.add(capaciteField);
        formPanel.add(new JLabel("Nombre de lits"));
        formPanel.add(litsField);
        formPanel.add(new JLabel("ID à supprimer"));
        formPanel.add(idSuppressionField);

        JButton ajouterBtn = new JButton("➕ Ajouter");
        ajouterBtn.addActionListener(e -> ajouterHebergement());
        JButton supprimerBtn = new JButton("❌ Supprimer");
        supprimerBtn.addActionListener(e -> supprimerHebergement());
        JButton refreshBtn = new JButton("🔄 Rafraîchir");
        refreshBtn.addActionListener(e -> chargerHebergements());

        formPanel.add(ajouterBtn);
        formPanel.add(supprimerBtn);
        formPanel.add(refreshBtn);

        return formPanel;
    }

    private void ajouterHebergement() {
        try {
            String nom = nomField.getText();
            String adresse = adresseField.getText();
            String localisation = localisationField.getText();
            double prix = Double.parseDouble(prixField.getText());
            String categorie = categorieField.getText();
            String photo = photoField.getText();
            String options = optionsField.getText();
            int capaciteMax = Integer.parseInt(capaciteField.getText());
            int nombreLits = Integer.parseInt(litsField.getText());

            Hebergement h = new Hebergement(nom, adresse, localisation, "Description automatique",
                    prix, categorie, photo, options, capaciteMax, nombreLits);

            boolean ok = controller.ajouterHebergement(h);
            if (ok) {
                chargerHebergements();
                JOptionPane.showMessageDialog(this, "✅ Hébergement ajouté.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Échec de l'ajout.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerHebergement() {
        try {
            int id = Integer.parseInt(idSuppressionField.getText());
            boolean ok = controller.supprimerHebergement(id);
            if (ok) {
                chargerHebergements();
                JOptionPane.showMessageDialog(this, "✅ Hébergement supprimé.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ ID introuvable.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
}
