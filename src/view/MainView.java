package view;

import controller.BookingController;
import model.Hebergement;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainView extends JFrame {

    private JPanel logementPanel;
    private JPanel filtresPanel;

    private List<JCheckBox> cbCategories = new ArrayList<>();
    private List<JCheckBox> cbLocalisations = new ArrayList<>();
    private List<JCheckBox> cbCaracteristiques = new ArrayList<>();
    private List<JCheckBox> cbProfitances = new ArrayList<>();

    private JTextField champLieu = new JTextField();
    private JDatePickerImpl dateArriveePicker;
    private JDatePickerImpl dateDepartPicker;

    public MainView(BookingController controller) {
        setTitle("Booking 2025 – Hébergements");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(1200, 50));

        JLabel titre = new JLabel("Hébergements", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(titre, BorderLayout.CENTER);

        JButton btnHome = new JButton("Accueil");
        btnHome.setFocusPainted(false);
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFont(new Font("Arial", Font.PLAIN, 14));
        btnHome.addActionListener(e -> {
            dispose();
            new MainView(controller);
        });
        header.add(btnHome, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // === Menu filtres à gauche ===
        filtresPanel = new JPanel();
        filtresPanel.setLayout(new BoxLayout(filtresPanel, BoxLayout.Y_AXIS));
        filtresPanel.setBackground(Color.decode("#437a7e"));

        JLabel lblLieu = new JLabel("📍 Lieu :");
        lblLieu.setForeground(Color.WHITE);
        lblLieu.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        filtresPanel.add(lblLieu);
        filtresPanel.add(champLieu);
        filtresPanel.add(Box.createVerticalStrut(10));

        addRepliableSection("📂 Catégories :", cbCategories, "Hôtel", "Appartement", "Chalet", "Villa", "Loft");
        addRepliableSection("📍 Localisation :", cbLocalisations, "Mer", "Montagne", "Campagne", "Ville");
        addRepliableSection("⚙️ Caractéristiques :", cbCaracteristiques, "Wifi", "Climatisation", "Coffre-fort", "Fumeur", "Non fumeur", "Ménage", "Petit-déjeuner");
        addRepliableSection("🍃 Profitance :", cbProfitances, "Plage", "Activité pas loin", "Environnement naturel");

        // === Dates ===
        JLabel lblDates = new JLabel("📅 Dates de séjour :");
        lblDates.setForeground(Color.WHITE);
        lblDates.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        filtresPanel.add(lblDates);

        dateArriveePicker = creerDatePicker();
        dateDepartPicker = creerDatePicker();
        filtresPanel.add(dateArriveePicker);
        filtresPanel.add(Box.createVerticalStrut(5));
        filtresPanel.add(dateDepartPicker);
        filtresPanel.add(Box.createVerticalStrut(10));

        JButton btnChercher = new JButton("🔍 Rechercher");
        JButton btnToutAfficher = new JButton("📋 Tout afficher");

        btnChercher.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnToutAfficher.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panneau bas
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new BoxLayout(panneauBoutons, BoxLayout.Y_AXIS));
        panneauBoutons.setBackground(Color.decode("#437a7e"));
        panneauBoutons.setPreferredSize(new Dimension(290, 80));
        panneauBoutons.add(btnChercher);
        panneauBoutons.add(Box.createVerticalStrut(10));
        panneauBoutons.add(btnToutAfficher);

        // Scroll vertical sur le panneau bleu
        JScrollPane scrollFiltres = new JScrollPane(filtresPanel);
        scrollFiltres.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollFiltres.setPreferredSize(new Dimension(290, getHeight()));
        scrollFiltres.setBorder(null);

        JPanel blocGauche = new JPanel(new BorderLayout());
        blocGauche.setPreferredSize(new Dimension(290, getHeight()));
        blocGauche.add(scrollFiltres, BorderLayout.CENTER);
        blocGauche.add(panneauBoutons, BorderLayout.SOUTH);

        add(blocGauche, BorderLayout.WEST);

        // === Zone centrale ===
        logementPanel = new JPanel();
        logementPanel.setBackground(Color.decode("#f4f4f4"));
        logementPanel.setLayout(new BoxLayout(logementPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(logementPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // === Actions
        btnChercher.addActionListener(e -> rechercher(controller));
        btnToutAfficher.addActionListener(e -> {
            champLieu.setText("");
            resetCheckboxes(cbCategories);
            resetCheckboxes(cbLocalisations);
            resetCheckboxes(cbCaracteristiques);
            resetCheckboxes(cbProfitances);
            dateArriveePicker.getModel().setValue(null);
            dateDepartPicker.getModel().setValue(null);
            afficherTousLesHebergements(controller);
        });

        afficherTousLesHebergements(controller);
        setVisible(true);
    }

    private void addRepliableSection(String titre, List<JCheckBox> checkboxes, String... labels) {
        JButton toggle = new JButton(titre);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setForeground(Color.WHITE);
        toggle.setBorderPainted(false);
        toggle.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.decode("#437a7e"));
        sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String label : labels) {
            JCheckBox cb = new JCheckBox(label);
            cb.setBackground(Color.decode("#437a7e"));
            cb.setForeground(Color.WHITE);
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            sectionPanel.add(cb);
            checkboxes.add(cb);
        }

        toggle.addActionListener(e -> sectionPanel.setVisible(!sectionPanel.isVisible()));

        filtresPanel.add(toggle);
        filtresPanel.add(sectionPanel);
        filtresPanel.add(Box.createVerticalStrut(10));
    }

    private void rechercher(BookingController controller) {
        logementPanel.removeAll();
        try {
            String lieu = champLieu.getText().trim();
            String categorie = getSelected(cbCategories);
            List<String> selectedOptions = new ArrayList<>();
            selectedOptions.addAll(getListSelected(cbCaracteristiques));
            selectedOptions.addAll(getListSelected(cbProfitances));
            selectedOptions.addAll(getListSelected(cbLocalisations));
            String options = String.join(",", selectedOptions);

            List<Hebergement> resultats = controller.rechercherAvancee(lieu, categorie, options);
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

    private JPanel creerCarteHebergement(Hebergement h) {
        JPanel carte = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };

        carte.setOpaque(false);
        carte.setBackground(Color.decode("#e3e3e3"));
        carte.setLayout(new BorderLayout(15, 0));
        carte.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        carte.setMaximumSize(new Dimension(1100, 170));
        carte.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);

        JLabel nom = new JLabel(h.getNom());
        nom.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel adresse = new JLabel(h.getAdresse());
        JLabel prix = new JLabel("Prix : " + h.getPrix() + " €");
        JLabel desc = new JLabel(h.getDescription());

        JButton btnReserver = new JButton("Réserver");
        btnReserver.addActionListener(e -> new FicheHebergement(h));

        infos.add(nom);
        infos.add(adresse);
        infos.add(prix);
        infos.add(desc);
        infos.add(Box.createVerticalStrut(10));
        infos.add(btnReserver);

        carte.add(imageLabel, BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);

        return carte;
    }

    private void resetCheckboxes(List<JCheckBox> checkboxes) {
        for (JCheckBox cb : checkboxes) {
            cb.setSelected(false);
        }
    }

    private String getSelected(List<JCheckBox> checkboxes) {
        List<String> selected = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selected.add(cb.getText());
            }
        }
        return String.join(",", selected);
    }

    private List<String> getListSelected(List<JCheckBox> checkboxes) {
        List<String> selected = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                selected.add(cb.getText());
            }
        }
        return selected;
    }

    private JDatePickerImpl creerDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Aujourd’hui");
        p.put("text.month", "Mois");
        p.put("text.year", "Année");
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(panel, new DateComponentFormatter());
    }
}
