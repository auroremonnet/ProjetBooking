package view;

import controller.BookingController;
import controller.ReservationController;
import model.Client;
import model.Hebergement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class FicheHebergement extends JFrame {

    private final Connection connection;
    private final BookingController controller;
    private final Client client;
    private final Hebergement hebergement;
    private final LocalDate dateArrivee;
    private final LocalDate dateDepart;
    private final int nbParents;
    private final int nbEnfants;
    private final int nbLits;

    public FicheHebergement(Connection connection,
                            Hebergement h,
                            Client client,
                            LocalDate dateArrivee,
                            LocalDate dateDepart,
                            int nbParents,
                            int nbEnfants,
                            int nbLits,
                            BookingController controller) {
        this.connection = connection;
        this.client = client;
        this.hebergement = h;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nbParents = nbParents;
        this.nbEnfants = nbEnfants;
        this.nbLits = nbLits;
        this.controller = controller;

        setTitle("🛏 Réservation – " + h.getNom());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildHeader();
        buildContentWithScroll();

        setVisible(true);
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(800, 60));

        JLabel titre = new JLabel("Réservation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);

        // Création du menu popup
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemAccueil = new JMenuItem("🏠 Accueil");
        JMenuItem itemMonCompte = new JMenuItem("👤 Mon compte");

        // Actions du menu
        itemAccueil.addActionListener(e -> {
            dispose();
            new MainView(controller, client, connection);
        });
        itemMonCompte.addActionListener(e -> new MonCompteView(client, connection, controller));

        // Ajout des items au menu
        menu.add(itemAccueil);
        menu.add(itemMonCompte);

        // Bouton pour afficher le menu
        JButton btnMenu = new JButton(" Menu");
        btnMenu.setFocusPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        btnMenu.addActionListener(e -> menu.show(btnMenu, 0, btnMenu.getHeight()));

        header.add(btnMenu, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContentWithScroll() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        center.setBackground(Color.WHITE);
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Chargement de l'image
        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        URL url = getClass().getClassLoader().getResource("images/" + hebergement.getPhotos());
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            File f = new File("resources/images/" + hebergement.getPhotos());
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            } else {
                imgLabel.setText("Image indisponible");
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                System.err.println("❌ Image introuvable pour : " + hebergement.getPhotos());
            }
        }
        center.add(imgLabel);
        center.add(Box.createVerticalStrut(15));

        // Nom du logement
        JLabel nom = new JLabel(hebergement.getNom());
        nom.setFont(new Font("Arial", Font.BOLD, 20));
        nom.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(nom);
        center.add(Box.createVerticalStrut(10));

        // Panneau d'informations
        JPanel infosPanel = createRoundedPanel();
        infosPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infosPanel.add(new JLabel("📍 Adresse : " + hebergement.getAdresse()));
        infosPanel.add(new JLabel("💶 Prix par nuit : " + hebergement.getPrix() + " €"));
        infosPanel.add(new JLabel("🏠 Capacité : " + hebergement.getCapaciteMax() + " pers – " + hebergement.getNombreLits() + " lits"));
        center.add(infosPanel);
        center.add(Box.createVerticalStrut(20));

        // Description complémentaire
        if (hebergement.getComplementDescription() != null && !hebergement.getComplementDescription().isEmpty()) {
            JPanel descPanel = createRoundedPanel();
            descPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel desc = new JLabel("<html><p style='width:600px'>" + hebergement.getComplementDescription() + "</p></html>");
            desc.setFont(new Font("Arial", Font.ITALIC, 14));
            descPanel.add(desc);
            center.add(descPanel);
            center.add(Box.createVerticalStrut(20));
        }

        // Récapitulatif de la sélection
        JPanel recap = createRoundedPanel();
        recap.setMaximumSize(new Dimension(500, 120));
        recap.setAlignmentX(Component.CENTER_ALIGNMENT);
        recap.add(new JLabel("📋 Vos données sélectionnées"));
        recap.add(new JLabel("🗓 Séjour : " + dateArrivee + " → " + dateDepart));
        recap.add(new JLabel("👨‍👩‍👧‍👦 Voyageurs : " + (nbParents + nbEnfants) + " pers (" + nbParents + " parents, " + nbEnfants + " enfants)"));
        recap.add(new JLabel("↪ Lits souhaités : " + nbLits));
        center.add(recap);
        center.add(Box.createVerticalStrut(20));

        // Calcul du prix total
        long nbJours = ChronoUnit.DAYS.between(dateArrivee, dateDepart);
        double total = nbJours * hebergement.getPrix();
        JPanel paiementPanel = createRoundedPanel();
        paiementPanel.setMaximumSize(new Dimension(500, 80));
        paiementPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        paiementPanel.add(new JLabel("Prix total pour " + nbJours + " nuit(s) : " + total + " €"));
        center.add(paiementPanel);
        center.add(Box.createVerticalStrut(30));

        // Bouton de confirmation
        JButton btnValider = new JButton("Payer et confirmer");
        btnValider.setBackground(Color.decode("#598d90"));
        btnValider.setForeground(Color.WHITE);
        btnValider.setFont(new Font("Arial", Font.BOLD, 16));
        btnValider.setFocusPainted(false);
        btnValider.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValider.setPreferredSize(new Dimension(250, 50));
        btnValider.addActionListener(e -> doReservationAndPayment(total));
        center.add(btnValider);

        // Wrapping horizontal + JScrollPane
        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.X_AXIS));
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(Box.createHorizontalGlue());
        centerWrapper.add(center);
        centerWrapper.add(Box.createHorizontalGlue());

        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void doReservationAndPayment(double montant) {
        try {
            ReservationController rc = new ReservationController(connection);
            Reservation r = new Reservation(
                    0,
                    Date.valueOf(dateArrivee),
                    Date.valueOf(dateDepart),
                    nbParents,
                    nbEnfants,
                    nbLits,
                    client.getIdClient(),
                    hebergement.getIdHebergement(),
                    "Confirmée",
                    new Timestamp(System.currentTimeMillis())
            );
            boolean ok = rc.reserver(r);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "❌ Échec de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idResa = rc.historiqueParClient(client.getIdClient())
                    .stream()
                    .max(Comparator.comparing(Reservation::getDateReservation))
                    .get()
                    .getIdReservation();
            dispose();
            new PaiementView(
                    connection,
                    controller,
                    client,
                    idResa,
                    montant,
                    hebergement,
                    dateArrivee,
                    dateDepart,
                    nbParents,
                    nbEnfants,
                    nbLits
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
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