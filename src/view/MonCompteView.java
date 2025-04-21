package view;

import model.Client;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class MonCompteView extends JFrame {

    public MonCompteView(Client client) {
        setTitle("👤 Mon Compte");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(450, 60));
        JLabel titre = new JLabel("Mon compte", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === INFOS CLIENT + RÉSERVATION ===
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(Color.WHITE);

        infoPanel.add(new JLabel("👤 Nom : " + client.getNom()));
        infoPanel.add(new JLabel("🧾 Prénom : " + client.getPrenom()));
        infoPanel.add(new JLabel("📧 Email : " + client.getEmail()));
        infoPanel.add(new JLabel("🏠 Adresse : " + client.getAdresse()));
        infoPanel.add(new JLabel("📞 Téléphone : " + client.getTelephone()));

        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(new JLabel("📅 Dernière réservation confirmée :"));

        // → Ces données peuvent venir d’une classe Réservation ou d’un DAO
        String dateResa = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"));
        String lieuResa = "Chalet Montagne";
        double prixResa = 1200.0;

        infoPanel.add(new JLabel("🗓 Date : " + dateResa));
        infoPanel.add(new JLabel("📍 Lieu : " + lieuResa));
        infoPanel.add(new JLabel("💶 Montant payé : " + prixResa + " €"));

        add(infoPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
