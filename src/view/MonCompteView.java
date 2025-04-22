package view;

import controller.AuthController;
import controller.ReservationController;
import model.Client;
import model.Reservation;
import controller.BookingController;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class MonCompteView extends JFrame {
    private final Client client;
    private final Connection connection;
    private final BookingController controller;

    public MonCompteView(Client client, Connection connection, BookingController controller) {
        this.client = client;
        this.connection = connection;
        this.controller = controller;

        setTitle("Accueil Client");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(900, 70));
        JLabel titre = new JLabel("Tableau de bord Client", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 34));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // PANEL CENTRAL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Vos Informations (#e3e3e3)
        JPanel infoPanel = createRoundedPanel(new Color(227, 227, 227));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel infoTitle = new JLabel("Vos Informations");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 20));
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(new JLabel("Nom : " + client.getNom()));
        infoPanel.add(new JLabel("Prénom : " + client.getPrenom()));
        infoPanel.add(new JLabel("Email : " + client.getEmail()));
        infoPanel.add(new JLabel("Mot de passe : " + client.getMotDePasse()));
        infoPanel.add(new JLabel("Adresse : " + client.getAdresse()));
        infoPanel.add(new JLabel("Téléphone : " + client.getTelephone()));
        infoPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Vos Réservations (#598d90)
        JPanel reservationPanel = createRoundedPanel(new Color(89, 141, 144));
        reservationPanel.setLayout(new BoxLayout(reservationPanel, BoxLayout.Y_AXIS));
        JLabel reservationTitle = new JLabel("Vos Réservations Ces 3 Derniers Mois");
        reservationTitle.setFont(new Font("Arial", Font.BOLD, 18));
        reservationTitle.setForeground(Color.WHITE);
        reservationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservationPanel.add(reservationTitle);
        reservationPanel.add(Box.createVerticalStrut(10));
        try {
            ReservationController rc = new ReservationController(connection);
            List<Reservation> dernieres = rc.reservations3DerniersMois(client.getIdClient());
            if (dernieres.isEmpty()) {
                JLabel noRes = new JLabel("(Aucune réservation récente)");
                noRes.setForeground(Color.WHITE);
                noRes.setAlignmentX(Component.CENTER_ALIGNMENT);
                reservationPanel.add(noRes);
            } else {
                for (Reservation r : dernieres) {
                    JLabel resLabel = new JLabel("Réservation #" + r.getIdReservation() + " – Du " + r.getDateArrivee()
                            + " au " + r.getDateDepart()
                            + " – " + r.getNombreAdultes() + " adultes, "
                            + r.getNombreEnfants() + " enfants – Statut : " + r.getStatut());

                    resLabel.setForeground(Color.WHITE);
                    resLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    reservationPanel.add(resLabel);
                    reservationPanel.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception e) {
            JLabel err = new JLabel("Erreur de chargement des réservations.");
            err.setForeground(Color.WHITE);
            err.setAlignmentX(Component.CENTER_ALIGNMENT);
            reservationPanel.add(err);
        }

        mainPanel.add(reservationPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Mes Mails → devient un bouton
        JPanel mailPanel = createRoundedPanel(new Color(122, 194, 199));
        mailPanel.setLayout(new BoxLayout(mailPanel, BoxLayout.Y_AXIS));
        JButton mailBtn = new JButton("📧 Mes Mails");
        mailBtn.setFont(new Font("Arial", Font.BOLD, 18));
        mailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mailBtn.setBackground(new Color(122, 194, 199));
        mailBtn.setForeground(Color.BLACK);
        mailBtn.setFocusPainted(false);
        mailBtn.setBorder(BorderFactory.createLineBorder(new Color(122, 194, 199), 2, true));

        mailBtn.addActionListener(e -> {
            dispose();
            new ClientGererMailView(client, connection, controller);
        });

        mailPanel.add(mailBtn);
        mainPanel.add(mailPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel mesReservationsPanel = createRoundedPanel(new Color(122, 194, 199));
        mesReservationsPanel.setLayout(new BoxLayout(mesReservationsPanel, BoxLayout.Y_AXIS));
        JButton mesReservationsBtn = new JButton("📆 Mes Réservations");
        mesReservationsBtn.setFont(new Font("Arial", Font.BOLD, 18));
        mesReservationsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mesReservationsBtn.setBackground(new Color(122, 194, 199));
        mesReservationsBtn.setForeground(Color.BLACK);
        mesReservationsBtn.setFocusPainted(false);
        mesReservationsBtn.setBorder(BorderFactory.createLineBorder(new Color(122, 194, 199), 2, true));

        mesReservationsBtn.addActionListener(e -> {
            dispose();
            new MesReservationsView(client, connection, controller);
        });

        mesReservationsPanel.add(mesReservationsBtn);
        mainPanel.add(mesReservationsPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Boutons Accueil & Déconnexion côte à côte
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton accueilBtn = new JButton("🏠 Accueil");
        accueilBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        accueilBtn.setBackground(new Color(122, 194, 199));
        accueilBtn.setForeground(Color.BLACK);
        accueilBtn.setFocusPainted(false);
        accueilBtn.setPreferredSize(new Dimension(120, 35));
        accueilBtn.setMaximumSize(new Dimension(120, 35));
        accueilBtn.setBorder(BorderFactory.createLineBorder(new Color(122, 194, 199), 2, true));

        accueilBtn.addActionListener(e -> {
            dispose();
            new MainView(controller, client, connection);
        });

        JButton logoutBtn = new JButton("🔒 Déconnexion");
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutBtn.setBackground(new Color(122, 194, 199));
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPreferredSize(new Dimension(140, 35));
        logoutBtn.setMaximumSize(new Dimension(140, 35));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(122, 194, 199), 2, true));

        logoutBtn.addActionListener(e -> {
            dispose();
            new AuthView(new AuthController(connection), connection);
        });

        buttonPanel.add(accueilBtn);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(logoutBtn);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createRoundedPanel(Color color) {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(800, 200));
        return panel;
    }
}
