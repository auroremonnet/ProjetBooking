package view;

import controller.BookingController;
import model.Client;
import model.Hebergement;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Date;

public class MainView extends JFrame {

    private final Client client;
    private final BookingController controller;
    private final Connection connection;

    private final JPanel logementPanel;
    private final JPanel filtresPanel;
    private final List<JCheckBox> cbCategories      = new ArrayList<>();
    private final List<JCheckBox> cbLocalisations   = new ArrayList<>();
    private final List<JCheckBox> cbCaracteristiques= new ArrayList<>();
    private final List<JCheckBox> cbProfitances     = new ArrayList<>();

    private final JTextField champLieu               = new JTextField();
    private final JSpinner spinnerParents   = new JSpinner(new SpinnerNumberModel(2, 0, 20, 1));
    private final JSpinner spinnerEnfants   = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
    private final JSpinner spinnerLits      = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    private final JDatePickerImpl dateArriveePicker;
    private final JDatePickerImpl dateDepartPicker;

    public MainView(BookingController controller, Client client, Connection connection) {
        this.controller = controller;
        this.client     = client;
        this.connection = connection;

        setTitle("Booking 2025 – Hébergements");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(1200, 60));
        JLabel titre = new JLabel("Hébergements", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);

        // menu déroulant
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemAccueil   = new JMenuItem("🏠 Accueil");
        JMenuItem itemMonCompte = new JMenuItem("👤 Mon compte");
        itemAccueil.addActionListener(e -> {
            dispose();
            new MainView(controller, client, connection);
        });
        itemMonCompte.addActionListener(e -> new MonCompteView(client));
        menu.add(itemAccueil);
        menu.add(itemMonCompte);

        JButton menuButton = new JButton("☰ Menu");
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFont(new Font("Arial", Font.PLAIN, 14));
        menuButton.addActionListener(e -> menu.show(menuButton, 0, menuButton.getHeight()));
        header.add(menuButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // date pickers
        dateArriveePicker = creerDatePicker();
        dateDepartPicker  = creerDatePicker();

        // === PANNEAU FILTRES ===
        filtresPanel = new JPanel();
        filtresPanel.setLayout(new BoxLayout(filtresPanel, BoxLayout.Y_AXIS));
        filtresPanel.setBackground(Color.decode("#437a7e"));

        // lieu
        JLabel lblLieu = new JLabel("📍 Lieu :");
        lblLieu.setForeground(Color.WHITE);
        lblLieu.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        filtresPanel.add(lblLieu);
        filtresPanel.add(champLieu);
        filtresPanel.add(Box.createVerticalStrut(10));

        // sections repliables
        addRepliableSection("📂 Catégories :",      cbCategories,      "Hôtel","Appartement","Chalet","Villa","Loft");
        addRepliableSection("📍 Localisation :",    cbLocalisations,   "Mer","Montagne","Campagne","Ville");
        addRepliableSection("⚙️ Caractéristiques :", cbCaracteristiques, "Wifi","Climatisation","Coffre-fort","Fumeur","Non fumeur","Ménage","Petit-déjeuner");
        addRepliableSection("🍃 Profitance :",      cbProfitances,     "Plage","Activité pas loin","Environnement naturel");

        // dates
        JLabel lblDates = new JLabel("📅 Dates de séjour :");
        lblDates.setForeground(Color.WHITE);
        lblDates.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        filtresPanel.add(lblDates);
        filtresPanel.add(dateArriveePicker);
        filtresPanel.add(Box.createVerticalStrut(5));
        filtresPanel.add(dateDepartPicker);
        filtresPanel.add(Box.createVerticalStrut(10));

        // voyageurs
        JLabel lblVoy  = new JLabel("👨‍👩‍👧‍👦 Voyageurs :");
        lblVoy.setForeground(Color.WHITE);
        lblVoy.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        filtresPanel.add(lblVoy);
        JPanel pnlVoy = new JPanel(new GridLayout(3,2,5,5));
        pnlVoy.setOpaque(false);
        pnlVoy.add(new JLabel("Parents :")); pnlVoy.add(spinnerParents);
        pnlVoy.add(new JLabel("Enfants :")); pnlVoy.add(spinnerEnfants);
        pnlVoy.add(new JLabel("Lits :"));    pnlVoy.add(spinnerLits);
        filtresPanel.add(pnlVoy);

        // boutons
        JButton btnChercher     = new JButton("🔍 Rechercher");
        JButton btnToutAfficher = new JButton("📋 Tout afficher");
        JPanel pnlBtns = new JPanel();
        pnlBtns.setLayout(new BoxLayout(pnlBtns, BoxLayout.Y_AXIS));
        pnlBtns.setBackground(Color.decode("#437a7e"));
        pnlBtns.add(btnChercher);
        pnlBtns.add(Box.createVerticalStrut(10));
        pnlBtns.add(btnToutAfficher);

        // scroll filtres
        JScrollPane scFiltres = new JScrollPane(filtresPanel);
        scFiltres.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scFiltres.setPreferredSize(new Dimension(290, getHeight()));
        scFiltres.setBorder(null);
        JPanel blocGauche = new JPanel(new BorderLayout());
        blocGauche.setPreferredSize(new Dimension(290, getHeight()));
        blocGauche.add(scFiltres, BorderLayout.CENTER);
        blocGauche.add(pnlBtns,   BorderLayout.SOUTH);
        add(blocGauche, BorderLayout.WEST);

        // zone d’affichage
        logementPanel = new JPanel();
        logementPanel.setBackground(Color.decode("#f4f4f4"));
        logementPanel.setLayout(new BoxLayout(logementPanel, BoxLayout.Y_AXIS));
        JScrollPane scLog = new JScrollPane(logementPanel);
        scLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scLog.setBorder(BorderFactory.createEmptyBorder());
        add(scLog, BorderLayout.CENTER);

        // actions
        btnChercher.addActionListener(e -> rechercher());
        btnToutAfficher.addActionListener(e -> {
            champLieu.setText("");
            resetCheckboxes(cbCategories);
            resetCheckboxes(cbLocalisations);
            resetCheckboxes(cbCaracteristiques);
            resetCheckboxes(cbProfitances);
            dateArriveePicker.getModel().setValue(null);
            dateDepartPicker.getModel().setValue(null);
            afficherTous();
        });

        // affichage initial
        afficherTous();
        setVisible(true);
    }

    private void addRepliableSection(String titre, List<JCheckBox> liste, String... labels) {
        JButton toggle = new JButton(titre);
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setForeground(Color.WHITE);
        toggle.setBorderPainted(false);
        toggle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#437a7e"));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String l : labels) {
            JCheckBox cb = new JCheckBox(l);
            cb.setBackground(Color.decode("#437a7e"));
            cb.setForeground(Color.WHITE);
            cb.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(cb);
            liste.add(cb);
        }

        toggle.addActionListener(e -> panel.setVisible(!panel.isVisible()));
        filtresPanel.add(toggle);
        filtresPanel.add(panel);
        filtresPanel.add(Box.createVerticalStrut(10));
    }

    private void rechercher() {
        logementPanel.removeAll();
        try {
            String lieu = champLieu.getText().trim();
            String cate = getSelected(cbCategories);
            List<String> opts = new ArrayList<>();
            opts.addAll(getListSelected(cbCaracteristiques));
            opts.addAll(getListSelected(cbProfitances));
            opts.addAll(getListSelected(cbLocalisations));
            String options = String.join(",", opts);

            int voyageurs = (Integer) spinnerParents.getValue() + (Integer) spinnerEnfants.getValue();
            int lits       = (Integer) spinnerLits.getValue();

            List<Hebergement> result = controller.rechercherAvancee(lieu, cate, options);
            for (Hebergement h : result) {
                if (h.getCapaciteMax() >= voyageurs && h.getNombreLits() >= lits) {
                    logementPanel.add(creerCarte(h));
                    logementPanel.add(Box.createVerticalStrut(15));
                }
            }
            if (logementPanel.getComponentCount() == 0) {
                logementPanel.add(new JLabel("Aucun hébergement trouvé."));
            }
        } catch (Exception ex) {
            logementPanel.add(new JLabel("Erreur : " + ex.getMessage()));
        }
        logementPanel.revalidate();
        logementPanel.repaint();
    }

    private void afficherTous() {
        logementPanel.removeAll();
        try {
            for (Hebergement h : controller.listerTous()) {
                logementPanel.add(creerCarte(h));
                logementPanel.add(Box.createVerticalStrut(15));
            }
        } catch (Exception ex) {
            logementPanel.add(new JLabel("Erreur : " + ex.getMessage()));
        }
        logementPanel.revalidate();
        logementPanel.repaint();
    }

    private JPanel creerCarte(Hebergement h) {
        JPanel carte = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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

        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(150, 100));
        try {
            ImageIcon ic = new ImageIcon("images/" + h.getPhotos());
            Image im = ic.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(im));
        } catch (Exception ex) {
            img.setText("Aucune image");
            img.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setOpaque(false);
        infos.add(new JLabel(h.getNom()));
        infos.add(new JLabel(h.getAdresse()));
        infos.add(new JLabel("Prix : " + h.getPrix() + " €"));
        infos.add(new JLabel(h.getDescription()));
        infos.add(new JLabel("Capacité : " + h.getCapaciteMax() + " pers – " + h.getNombreLits() + " lits"));
        JButton btnRes = new JButton("Réserver");
        btnRes.addActionListener(e -> {
            Date d1 = (Date) dateArriveePicker.getModel().getValue();
            Date d2 = (Date) dateDepartPicker.getModel().getValue();
            if (d1 != null && d2 != null) {
                LocalDate a = d1.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                LocalDate b = d2.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                int np = (Integer) spinnerParents.getValue();
                int ne = (Integer) spinnerEnfants.getValue();
                int nl = (Integer) spinnerLits.getValue();
                new FicheHebergement(connection, h, client, a, b, np, ne, nl, controller);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner les dates.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        infos.add(Box.createVerticalStrut(10));
        infos.add(btnRes);

        carte.add(img,   BorderLayout.WEST);
        carte.add(infos, BorderLayout.CENTER);
        return carte;
    }

    private String getSelected(List<JCheckBox> list) {
        List<String> sel = new ArrayList<>();
        for (JCheckBox cb : list)
            if (cb.isSelected()) sel.add(cb.getText());
        return String.join(",", sel);
    }

    private List<String> getListSelected(List<JCheckBox> list) {
        List<String> sel = new ArrayList<>();
        for (JCheckBox cb : list)
            if (cb.isSelected()) sel.add(cb.getText());
        return sel;
    }

    private void resetCheckboxes(List<JCheckBox> list) {
        for (JCheckBox cb : list) cb.setSelected(false);
    }

    private JDatePickerImpl creerDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today","Aujourd’hui");
        p.put("text.month","Mois");
        p.put("text.year","Année");
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(panel, new DateComponentFormatter());
    }
}
