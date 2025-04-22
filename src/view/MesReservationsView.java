package view;

import controller.BookingController;
import controller.ReservationController;
import dao.AvisDAO;
import model.Avis;
import model.Client;
import model.Hebergement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class MesReservationsView extends JFrame {
    private final Client client;
    private final Connection connection;
    private final BookingController controller;

    public MesReservationsView(Client client, Connection connection, BookingController controller) {
        this.client = client;
        this.connection = connection;
        this.controller = controller;

        setTitle("Mes Réservations");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(1000, 70));
        JLabel titre = new JLabel("Historique de Réservations", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);

        try {
            ReservationController rc = new ReservationController(connection);
            BookingController bc = new BookingController(connection);
            AvisDAO avisDAO = new AvisDAO(connection);
            List<Reservation> reservations = rc.historiqueParClient(client.getIdClient());

            if (reservations.isEmpty()) {
                JLabel noRes = new JLabel("Aucune réservation trouvée.");
                noRes.setFont(new Font("Arial", Font.BOLD, 18));
                noRes.setAlignmentX(Component.CENTER_ALIGNMENT);
                mainPanel.add(noRes);
            } else {
                for (Reservation r : reservations) {
                    Hebergement h = bc.getHebergementParId(r.getIdHebergement());
                    JPanel resPanel = new JPanel();
                    resPanel.setLayout(new BoxLayout(resPanel, BoxLayout.Y_AXIS));
                    resPanel.setBackground(new Color(240, 240, 240));
                    resPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                    resPanel.setMaximumSize(new Dimension(800, 400));

                    JLabel title = new JLabel("🏨 " + h.getNom());
                    title.setFont(new Font("Arial", Font.BOLD, 18));
                    resPanel.add(title);
                    resPanel.add(new JLabel("Du " + r.getDateArrivee() + " au " + r.getDateDepart()));
                    resPanel.add(new JLabel("Adulte(s) : " + r.getNombreAdultes() + ", Enfant(s) : " + r.getNombreEnfants()));
                    resPanel.add(new JLabel("Statut : " + r.getStatut()));

                    // 🔴 Annuler uniquement si la date d’arrivée est dans le futur
                    if (r.getStatut().equals("Confirmée") && r.getDateArrivee().toLocalDate().isAfter(LocalDate.now())) {
                        JButton annulerBtn = new JButton("❌ Annuler");
                        annulerBtn.setBackground(new Color(220, 80, 80));
                        annulerBtn.setForeground(Color.WHITE);
                        annulerBtn.addActionListener(e -> {
                            try {
                                rc.annuler(r.getIdReservation());
                                JOptionPane.showMessageDialog(this, "Réservation annulée !");
                                dispose();
                                new MesReservationsView(client, connection, controller);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                            }
                        });
                        resPanel.add(Box.createVerticalStrut(10));
                        resPanel.add(annulerBtn);
                    }

                    // ✅ Afficher avis si existant
                    Avis avisExistant = avisDAO.getAvisByClientEtHebergement(client.getIdClient(), h.getIdHebergement());
                    if (avisExistant != null) {
                        resPanel.add(Box.createVerticalStrut(10));
                        resPanel.add(new JLabel("📝 Votre avis :"));
                        resPanel.add(new JLabel("Note : " + avisExistant.getNote() + "/5"));
                        resPanel.add(new JLabel("Commentaire : " + avisExistant.getCommentaire()));
                    }
                    // ✍️ Ajouter un avis si séjour terminé et pas encore noté
                    else if (r.getDateDepart().toLocalDate().isBefore(LocalDate.now())) {
                        resPanel.add(Box.createVerticalStrut(10));
                        resPanel.add(new JLabel("✍️ Laissez un avis :"));

                        JSpinner note = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
                        JTextArea comment = new JTextArea(3, 25);
                        comment.setLineWrap(true);
                        JScrollPane sc = new JScrollPane(comment);
                        JButton envoyer = new JButton("Envoyer");

                        envoyer.setBackground(new Color(89, 141, 144));
                        envoyer.setForeground(Color.WHITE);
                        envoyer.addActionListener(e -> {
                            try {
                                Avis avis = new Avis(0, client.getIdClient(), h.getIdHebergement(),
                                        (Integer) note.getValue(), comment.getText().trim(),
                                        new Timestamp(System.currentTimeMillis()));
                                boolean ok = avisDAO.ajouterAvis(avis);
                                if (ok) {
                                    JOptionPane.showMessageDialog(this, "Avis envoyé !");
                                    dispose();
                                    new MesReservationsView(client, connection, controller);
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                            }
                        });

                        resPanel.add(new JLabel("Note :"));
                        resPanel.add(note);
                        resPanel.add(new JLabel("Commentaire :"));
                        resPanel.add(sc);
                        resPanel.add(envoyer);
                    }

                    resPanel.add(Box.createVerticalStrut(20));
                    mainPanel.add(resPanel);
                    mainPanel.add(Box.createVerticalStrut(20));
                }
            }

        } catch (Exception e) {
            JLabel error = new JLabel("Erreur lors du chargement des réservations.");
            error.setFont(new Font("Arial", Font.BOLD, 18));
            error.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(error);
        }

        // ✅ Bouton retour
        JButton retourBtn = new JButton("⬅️ Retour");
        retourBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        retourBtn.setBackground(new Color(122, 194, 199));
        retourBtn.setForeground(Color.BLACK);
        retourBtn.setFocusPainted(false);
        retourBtn.addActionListener(e -> {
            dispose();
            new MonCompteView(client, connection, controller);
        });

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.add(retourBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
