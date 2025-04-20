package view;

import controller.BookingController;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainView extends JFrame {

    private JPanel logementPanel;
    private JPanel filtresPanel;

    private List<JCheckBox> cbCategories = new ArrayList<>();
    private List<JCheckBox> cbLocalisations = new ArrayList<>();
    private List<JCheckBox> cbCaracteristiques = new ArrayList<>();
    private List<JCheckBox> cbProfitances = new ArrayList<>();

    public MainView(BookingController controller) {
        setTitle("Booking 2025 – Hébergements");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🧡 Filtres à gauche
        filtresPanel = new JPanel();
        filtresPanel.setLayout(new BoxLayout(filtresPanel, BoxLayout.Y_AXIS));
        filtresPanel.setBackground(Color.decode("#437a7e"));
        filtresPanel.setPreferredSize(new Dimension(250, getHeight()));

        addSection("📂 Catégories :", cbCategories, "Hôtel", "Maison", "Appartement", "Chalet", "Villa");
        addSection("📍 Localisation :", cbLocalisations, "Mer", "Montagne", "Campagne", "Ville");
        addSection("⚙️ Caractéristiques :", cbCaracteristiques, "Wifi", "Climatisation", "Coffre-fort", "Fumeur", "Non fumeur", "Ménage", "Petit-déjeuner");
        addSection("🍃 Profitance :", cbProfitances, "Plage", "Activité pas loin", "Environnement naturel");

        JButton btnChercher = new JButton("🔍 Rechercher");
        JButton btnToutAfficher = new JButton("📋 Tout afficher");
        btnChercher.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnToutAfficher.setAlignmentX(Component.CENTER_ALIGNMENT);

        filtresPanel.add(Box.createVerticalStrut(20));
        filtresPanel.add(btnChercher);
        filtresPanel.add(Box.createVerticalStrut(10));
        filtresPanel.add(btnToutAfficher);

        add(filtresPanel, BorderLayout.WEST);

        // 💜 Zone centrale
        logementPanel = new JPanel();
        logementPanel.setBackground(Color.decode("#f4f4f4"));
        logementPanel.setLayout(new BoxLayout(logementPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(logementPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        btnChercher.addActionListener(e -> rechercher(controller));
        btnToutAfficher.addActionListener(e -> afficherTousLesHebergements(controller));

        afficherTousLesHebergements(controller);
        setVisible(true); // à ne pas oublier
    }

    private void addSection(String titre, List<JCheckBox> checkboxes, String... labels) {
        JLabel sectionLabel = new JLabel(titre);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        sectionLabel.setForeground(Color.WHITE);
        filtresPanel.add(sectionLabel);

        for (String label : labels) {
            JCheckBox cb = new JCheckBox(label);
            cb.setBackground(Color.decode("#437a7e"));
            cb.setForeground(Color.WHITE);
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            filtresPanel.add(cb);
            checkboxes.add(cb);
        }
        filtresPanel.add(Box.createVerticalStrut(10));
    }

    private void afficherTousLesHebergements(BookingController controller) {
        logementPanel.removeAll();
        try {
            List<Hebergement> hebergements = controller.listerTous();
            for (Hebergement h : hebergements) {
                logementPanel.add(creerCarteHebergement(h));
                logementPanel.add(Box.createVerticalStrut(15));
            }
        } catch (Exception e) {
            logementPanel.add(new JLabel("Erreur : " + e.getMessage()));
        }
        logementPanel.revalidate();
        logementPanel.repaint();
    }

    private void rechercher(BookingController controller) {
        logementPanel.removeAll();
        try {
            String categorie = getSelected(cbCategories);
            String localisation = getSelected(cbLocalisations);
            String options = getSelected(cbCaracteristiques) + "," + getSelected(cbProfitances);

            List<Hebergement> resultats = controller.rechercherAvancee(localisation, categorie, options);
            if (resultats.isEmpty()) {
                logementPanel.add(new JLabel("Aucun hébergement trouvé."));
            } else {
                for (Hebergement h : resultats) {
                    logementPanel.add(creerCarteHebergement(h));
                    logementPanel.add(Box.createVerticalStrut(15));
                }
            }
        } catch (Exception e) {
            logementPanel.add(new JLabel("Erreur lors de la recherche : " + e.getMessage()));
        }
        logementPanel.revalidate();
        logementPanel.repaint();
    }

    private String getSelected(List<JCheckBox> checkboxes) {
        List<String> selected = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) selected.add(cb.getText());
        }
        return String.join(",", selected);
    }

    private JPanel creerCarteHebergement(Hebergement h) {
        JPanel carte = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // bords arrondis
            }
        };

        carte.setOpaque(false);
        carte.setBackground(Color.decode("#e3e3e3"));
        carte.setLayout(new BorderLayout(15, 0));
        carte.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        carte.setMaximumSize(new Dimension(1100, 150));
        carte.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Image à gauche
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 100));
        try {
            ImageIcon icon = new ImageIcon("images/" + h.getPhotos());
            Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Aucune image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // Infos hébergement
        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);

        JLabel nom = new JLabel(h.getNom());
        nom.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel adresse = new JLabel(h.getAdresse());
        JLabel prix = new JLabel("Prix : " + h.getPrix() + " €");
        JLabel desc = new JLabel(h.getDescription());

        infos.add(nom);
        infos.add(adresse);
        infos.add(prix);
        infos.add(desc);

        carte.add(imageLabel, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);

        return carte;
    }
}
