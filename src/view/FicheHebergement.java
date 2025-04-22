package view;

import controller.BookingController;
import controller.ReservationController;
import model.Client;
import model.Hebergement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

/**
 * Affiche le détail d'un hébergement, permet de réserver et de lancer le paiement.
 */
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

        setTitle("🛏️ Réservation – " + h.getNom());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        buildHeader();
        buildContent();

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(800, 60));

        JLabel titre = new JLabel("Réservation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(titre, BorderLayout.CENTER);

        // Menu déroulant
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemAccueil = new JMenuItem("🏠 Accueil");
        JMenuItem itemMonCompte = new JMenuItem("👤 Mon compte");

        itemAccueil.addActionListener(e -> {
            dispose();
            new MainView(controller, client, connection);
        });

        itemMonCompte.addActionListener(e -> new MonCompteView(client, connection, controller));

        JButton btnMenu = new JButton("☰ Menu");
        btnMenu.setFocusPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        btnMenu.addActionListener(e -> menu.show(btnMenu, 0, btnMenu.getHeight()));

        header.add(btnMenu, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));
        center.setBackground(Color.WHITE);

        // --- Image ---
        JLabel imgLabel = new JLabel();
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("images/" + hebergement.getPhotos());
            Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            imgLabel.setText("Image indisponible");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        center.add(imgLabel);
        center.add(Box.createVerticalStrut(15));

        // --- Nom ---
        JLabel nom = new JLabel(hebergement.getNom());
        nom.setFont(new Font("Arial", Font.BOLD, 20));
        nom.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(nom);
        center.add(Box.createVerticalStrut(10));

        // --- Infos hébergement ---
        JPanel infosPanel = createRoundedPanel();
        infosPanel.add(new JLabel("📍 Adresse : " + hebergement.getAdresse()));
        infosPanel.add(new JLabel("💶 Prix par nuit : " + hebergement.getPrix() + " €"));
        infosPanel.add(new JLabel("🏠 Capacité : " + hebergement.getCapaciteMax() + " pers – " + hebergement.getNombreLits() + " lits"));
        center.add(infosPanel);
        center.add(Box.createVerticalStrut(20));

        // --- Détails sélection utilisateur ---
        JPanel recap = createRoundedPanel();
        recap.setMaximumSize(new Dimension(500, 120));
        recap.add(new JLabel("📋 Vos données sélectionnées"));
        recap.add(new JLabel("🗓️ Séjour : " + dateArrivee + " → " + dateDepart));
        recap.add(new JLabel("👨‍👩‍👧‍👦 Voyageurs : " + (nbParents + nbEnfants) + " pers (" + nbParents + " parents, " + nbEnfants + " enfants)"));
        recap.add(new JLabel("↪️ Lits souhaités : " + nbLits));
        center.add(recap);
        center.add(Box.createVerticalStrut(20));

        // --- Prix total ---
        long nbJours = ChronoUnit.DAYS.between(dateArrivee, dateDepart);
        double total = nbJours * hebergement.getPrix();
        JPanel paiementPanel = createRoundedPanel();
        paiementPanel.setMaximumSize(new Dimension(500, 80));
        paiementPanel.add(new JLabel("💵 Prix total pour " + nbJours + " nuit(s) : " + total + " €"));
        center.add(paiementPanel);
        center.add(Box.createVerticalStrut(30));

        // --- Bouton Payer ---
        JButton btnValider = new JButton("Payer et confirmer");
        btnValider.setBackground(Color.decode("#598d90"));
        btnValider.setForeground(Color.WHITE);
        btnValider.setFont(new Font("Arial", Font.BOLD, 16));
        btnValider.setFocusPainted(false);
        btnValider.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValider.setPreferredSize(new Dimension(250, 50));
        btnValider.addActionListener(e -> doReservationAndPayment(total));
        center.add(btnValider);

        add(center, BorderLayout.CENTER);
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
            new PaiementView(connection, controller, client, idResa, montant);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel() {
            @Override
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
