package view;

import controller.AdminController;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AdminDashboardView extends JFrame {
    private final AdminController controller;
    private JTextArea output;
    private JTextField nomField, adresseField, localisationField, prixField, categorieField, photoField, optionsField, idSuppressionField;

    public AdminDashboardView(Connection conn) {
        this.controller = new AdminController(conn);

        setTitle("🛠️ Admin - Gestion des hébergements");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setMargin(new Insets(10, 10, 10, 10));
        add(new JScrollPane(output), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        nomField = new JTextField();
        adresseField = new JTextField();
        localisationField = new JTextField();
        prixField = new JTextField();
        categorieField = new JTextField();
        photoField = new JTextField();
        optionsField = new JTextField();
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
        formPanel.add(new JLabel("Photo"));
        formPanel.add(photoField);
        formPanel.add(new JLabel("Options"));
        formPanel.add(optionsField);
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

        add(formPanel, BorderLayout.SOUTH);

        chargerHebergements();
        setVisible(true);
    }

    private void chargerHebergements() {
        try {
            List<Hebergement> liste = controller.listerHebergements();

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);

            for (Hebergement h : liste) {
                JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT));
                card.setPreferredSize(new Dimension(850, 100));

                // Texte
                String info = String.format("<html><b>ID:</b> %d<br><b>%s</b><br>%s — %.2f €<br>Catégorie: %s<br>Options: %s</html>",
                        h.getIdHebergement(), h.getNom(), h.getAdresse(), h.getPrix(), h.getCategorie(), h.getOptions());
                JLabel label = new JLabel(info);
                label.setPreferredSize(new Dimension(500, 80));

                // Image
                String imagePath = "resources/images/" + h.getPhotos();
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaled));

                card.add(imageLabel);
                card.add(label);
                panel.add(card);
            }

            JScrollPane scrollPane = new JScrollPane(panel);
            setContentPane(scrollPane);
            revalidate();
            repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
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

            Hebergement h = new Hebergement(nom, adresse, localisation, "Aucune description", prix, categorie, photo, options);
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
