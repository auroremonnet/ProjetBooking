package view;

import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FicheHebergement extends JFrame {

    public FicheHebergement(Hebergement h, LocalDate dateArrivee, LocalDate dateDepart, int nbParents, int nbEnfants, int nbLits) {
        setTitle("🛏️ Réservation – " + h.getNom());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(800, 60));

        JLabel titre = new JLabel("Réservation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);

        JButton btnAccueil = new JButton("Accueil");
        btnAccueil.setFocusPainted(false);
        btnAccueil.setContentAreaFilled(false);
        btnAccueil.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAccueil.addActionListener(e -> dispose());
        header.add(btnAccueil, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // === CONTENU CENTRAL ===
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        // === IMAGE ===
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("images/" + h.getPhotos());
            Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image indisponible");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalStrut(15));

        // === NOM ===
        JLabel nomLabel = new JLabel(h.getNom());
        nomLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(nomLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        // === INFOS HEBERGEMENT ===
        JLabel lblAdresse = new JLabel("📍 Adresse : " + h.getAdresse());
        JLabel lblPrix = new JLabel("💶 Prix : " + h.getPrix() + " €");
        JLabel lblCapacite = new JLabel("🏠 Capacité max : " + h.getCapaciteMax() + " pers – " + h.getNombreLits() + " lits");

        JPanel infosPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        infosPanel.setOpaque(false);
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.add(lblAdresse);
        infosPanel.add(lblPrix);
        infosPanel.add(lblCapacite);
        infosPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(infosPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // === VOS DONNÉES SELECTIONNÉES ===
        JPanel cadreInfos = createRoundedPanel();
        cadreInfos.setMaximumSize(new Dimension(500, 120));
        JLabel titreInfos = new JLabel("📋 Vos données sélectionnées");
        titreInfos.setFont(new Font("Arial", Font.BOLD, 14));
        cadreInfos.add(titreInfos);
        cadreInfos.add(new JLabel("🗓️ Séjour : " + dateArrivee + " → " + dateDepart));
        cadreInfos.add(new JLabel("👨‍👩‍👧‍👦 Voyageurs : " + (nbParents + nbEnfants) + " personnes (dont " + nbParents + " parents, " + nbEnfants + " enfants)"));
        cadreInfos.add(new JLabel("↪️ Nombre de lits souhaités : " + nbLits));

        centerPanel.add(cadreInfos);
        centerPanel.add(Box.createVerticalStrut(20));

        // === PAIEMENT (Affichage uniquement) ===
        long nbJours = ChronoUnit.DAYS.between(dateArrivee, dateDepart);
        int prixTotal = (int) (nbJours * h.getPrix());

        JPanel cadrePaiement = createRoundedPanel();
        cadrePaiement.setMaximumSize(new Dimension(500, 100));
        JLabel titrePaiement = new JLabel("✅ Validé et procéder au paiement");
        titrePaiement.setFont(new Font("Arial", Font.BOLD, 14));
        cadrePaiement.add(titrePaiement);
        cadrePaiement.add(new JLabel("💵 Prix total pour " + nbJours + " nuit(s) : " + prixTotal + " €"));

        centerPanel.add(cadrePaiement);
        centerPanel.add(Box.createVerticalStrut(15));

        // === BOUTON VALIDER (Message uniquement) ===
        JButton btnValider = new JButton("Valider");
        btnValider.setBackground(Color.decode("#e3e3e3"));
        btnValider.setFocusPainted(false);
        btnValider.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValider.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "✅ Réservation validée ! (Paiement non encore implémenté)");
            dispose();
        });

        centerPanel.add(btnValider);
        centerPanel.add(Box.createVerticalStrut(10));

        add(centerPanel, BorderLayout.CENTER);
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
