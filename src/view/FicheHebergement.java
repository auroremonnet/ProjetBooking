package view;


import model.Hebergement;

import javax.swing.*;
import java.awt.*;

public class FicheHebergement extends JFrame {

    public FicheHebergement(Hebergement h) {
        setTitle("Détail de l'hébergement");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 200));
        try {
            ImageIcon icon = new ImageIcon("images/" + h.getPhotos());
            Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image indisponible");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        mainPanel.add(imageLabel, BorderLayout.WEST);

        // Infos texte
        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));

        JLabel lblNom = new JLabel("🏨 " + h.getNom());
        lblNom.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel lblAdresse = new JLabel("📍 Adresse : " + h.getAdresse());
        JLabel lblLocalisation = new JLabel("🌍 Localisation : " + h.getLocalisation());
        JLabel lblCategorie = new JLabel("🏷️ Catégorie : " + h.getCategorie());
        JLabel lblPrix = new JLabel("💶 Prix : " + h.getPrix() + " €");
        JLabel lblDescription = new JLabel("<html><b>Description :</b><br/>" + h.getDescription() + "</html>");
        JLabel lblOptions = new JLabel("<html><b>Options :</b> " + h.getOptions() + "</html>");

        infosPanel.add(lblNom);
        infosPanel.add(Box.createVerticalStrut(5));
        infosPanel.add(lblAdresse);
        infosPanel.add(lblLocalisation);
        infosPanel.add(lblCategorie);
        infosPanel.add(lblPrix);
        infosPanel.add(Box.createVerticalStrut(10));
        infosPanel.add(lblDescription);
        infosPanel.add(Box.createVerticalStrut(5));
        infosPanel.add(lblOptions);

        mainPanel.add(infosPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> dispose());
        add(btnFermer, BorderLayout.SOUTH);

        setVisible(true);
    }
}
