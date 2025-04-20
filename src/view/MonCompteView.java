package view;

import model.Client;

import javax.swing.*;
import java.awt.*;

public class MonCompteView extends JFrame {

    public MonCompteView(Client client) {
        setTitle("👤 Mon Compte");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(400, 60));
        JLabel titre = new JLabel("Mon compte", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(Color.WHITE);

        infoPanel.add(new JLabel("👤 Nom : " + client.getNom()));
        infoPanel.add(new JLabel("🧾 Prénom : " + client.getPrenom()));
        infoPanel.add(new JLabel("📧 Email : " + client.getEmail()));
        infoPanel.add(new JLabel("🏠 Adresse : " + client.getAdresse()));
        infoPanel.add(new JLabel("📞 Téléphone : " + client.getTelephone()));

        add(infoPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}