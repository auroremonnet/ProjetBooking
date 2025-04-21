package view;

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
                            int nbLits) {
        this.connection    = connection;
        this.client        = client;
        this.hebergement   = h;
        this.dateArrivee   = dateArrivee;
        this.dateDepart    = dateDepart;
        this.nbParents     = nbParents;
        this.nbEnfants     = nbEnfants;
        this.nbLits        = nbLits;

        setTitle("🛏️ Réservation – " + h.getNom());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        buildHeader();
        buildContent();

        setVisible(true);
    }

    private void buildHeader() {
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
    }

    private void buildContent() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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

        // --- Informations hébergement ---
        JPanel infosPanel = createRoundedPanel();
        infosPanel.add(new JLabel("📍 Adresse : " + hebergement.getAdresse()));
        infosPanel.add(new JLabel("💶 Prix par nuit : " + hebergement.getPrix() + " €"));
        infosPanel.add(new JLabel("🏠 Capacité : " + hebergement.getCapaciteMax() +
                " pers – " + hebergement.getNombreLits() + " lits"));
        center.add(infosPanel);
        center.add(Box.createVerticalStrut(20));

        // --- Détails de la réservation ---
        JPanel recap = createRoundedPanel();
        recap.setMaximumSize(new Dimension(500, 120));
        recap.add(new JLabel("📋 Vos données sélectionnées"));
        recap.add(new JLabel("🗓️ Séjour : " + dateArrivee + " → " + dateDepart));
        recap.add(new JLabel("👨‍👩‍👧‍👦 Voyageurs : " +
                (nbParents + nbEnfants) + " pers (" +
                nbParents + " parents, " + nbEnfants + " enfants)"));
        recap.add(new JLabel("↪️ Lits souhaités : " + nbLits));
        center.add(recap);
        center.add(Box.createVerticalStrut(20));

        // --- Calcul prix total ---
        long nbJours = ChronoUnit.DAYS.between(dateArrivee, dateDepart);
        double total = nbJours * hebergement.getPrix();
        JPanel paiementPanel = createRoundedPanel();
        paiementPanel.setMaximumSize(new Dimension(500, 80));
        paiementPanel.add(new JLabel("💵 Prix total pour " + nbJours + " nuit(s) : " + total + " €"));
        center.add(paiementPanel);
        center.add(Box.createVerticalStrut(20));

        // --- Bouton de confirmation ---
        JButton btnValider = new JButton("Payer et confirmer");
        btnValider.setBackground(Color.decode("#e3e3e3"));
        btnValider.setFocusPainted(false);
        btnValider.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValider.addActionListener(e -> doReservationAndPayment(total));
        center.add(btnValider);

        add(center, BorderLayout.CENTER);
    }

    private void doReservationAndPayment(double montant) {
        try {
            // 1) créer la réservation en base
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

            // 2) récupérer l'ID de la réservation créée
            int idResa = rc.historiqueParClient(client.getIdClient())
                    .stream()
                    .max(Comparator.comparing(Reservation::getDateReservation))
                    .get()
                    .getIdReservation();

            // 3) lancer la vue de paiement
            dispose();
            new PaiementView(
                    connection,
                    client.getIdClient(),  // ← ici on passe l’ID du client
                    idResa,                // l’ID de la réservation qu’on vient de créer
                    montant                // le montant calculé
            );

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
                Graphics2D g2 = (Graphics2D)g;
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
