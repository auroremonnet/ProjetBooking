package view;

import controller.BookingController;
import dao.MoyenPaiementDAO;
import dao.PaiementDAO;
import dao.ReductionClientDAO;
import model.Client;
import model.MoyenPaiement;
import model.Paiement;
import model.Hebergement;
import model.ReductionClient;

import java.time.LocalDate;
import java.util.Optional;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Fenêtre de paiement CB (4 chiffres, MM/AA, CVV), avec design harmonisé.
 */
public class PaiementView extends JFrame {
    private final Connection conn;
    private final BookingController controller;
    private final Client client;
    private final int idResa;
    private final double montant;
    private final Hebergement hebergement;
    private final LocalDate dateArrivee;
    private final LocalDate dateDepart;
    private final int nbParents;
    private final int nbEnfants;
    private final int nbLits;

    private JTextField tfNum, tfExp, tfCvv;

    public PaiementView(Connection conn,
                        BookingController controller,
                        Client client,
                        int idResa,
                        double montant,
                        Hebergement hebergement,
                        LocalDate dateArrivee,
                        LocalDate dateDepart,
                        int nbParents,
                        int nbEnfants,
                        int nbLits) {
        this.conn = conn;
        this.controller = controller;
        this.client = client;
        this.idResa = idResa;
        this.montant = montant;

        this.hebergement = hebergement;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nbParents = nbParents;
        this.nbEnfants = nbEnfants;
        this.nbLits = nbLits;

        setTitle("Paiement Carte bancaire");
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
        header.setPreferredSize(new Dimension(600, 60));
        JLabel titre = new JLabel("Paiement", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 34));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Menu déroulant
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemAccueil = new JMenuItem("Accueil");
        JMenuItem itemMonCompte = new JMenuItem("Mon compte");

        itemAccueil.addActionListener(e -> {
            dispose();
            new MainView(controller, client, conn);
        });

        itemMonCompte.addActionListener(e -> new MonCompteView(client, conn, controller));

        menu.add(itemAccueil);
        menu.add(itemMonCompte);

        JButton btnMenu = new JButton(" Menu");
        btnMenu.setFocusPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setFont(new Font("Arial", Font.PLAIN, 14));
        btnMenu.addActionListener(e -> menu.show(btnMenu, 0, btnMenu.getHeight()));
        header.add(btnMenu, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void buildContent() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        main.setBackground(Color.WHITE);

        // Récupérer le taux de réduction
        double tauxReduction = 5.0;
        try {
            ReductionClientDAO redDao = new ReductionClientDAO(conn);
            Optional<ReductionClient> opt = redDao.findByType(client.getTypeClient());
            tauxReduction = opt.map(ReductionClient::getTauxReduction).orElse(5.0);
        } catch (Exception e) {
            System.err.println("Erreur chargement réduction : " + e.getMessage());
        }

        double montantRemise = montant * tauxReduction / 100.0;
        double montantFinal = montant - montantRemise;

        JLabel lblReduction = new JLabel("Réduction " + client.getTypeClient() + " : -" + tauxReduction + "%");
        lblReduction.setFont(new Font("Arial", Font.PLAIN, 16));
        lblReduction.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMontant = new JLabel("À payer après réduction : " + String.format("%.2f", montantFinal) + " €");
        lblMontant.setFont(new Font("Arial", Font.BOLD, 18));
        lblMontant.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(lblReduction);
        main.add(Box.createVerticalStrut(10));
        main.add(lblMontant);
        main.add(Box.createVerticalStrut(25));

        tfNum = new JTextField();
        tfExp = new JTextField();
        tfCvv = new JTextField();

        main.add(createLabelAndField("Numéro de carte (4 chiffres) :", tfNum));
        main.add(Box.createVerticalStrut(10));
        main.add(createLabelAndField("Date d'expiration (MM/AA) :", tfExp));
        main.add(Box.createVerticalStrut(10));
        main.add(createLabelAndField("CVV (3 chiffres) :", tfCvv));
        main.add(Box.createVerticalStrut(30));

        JButton btnPayer = new JButton("Valider le paiement");
        btnPayer.setBackground(Color.decode("#598d90"));
        btnPayer.setForeground(Color.WHITE);
        btnPayer.setFont(new Font("Arial", Font.BOLD, 18));
        btnPayer.setFocusPainted(false);
        btnPayer.setPreferredSize(new Dimension(250, 50));
        btnPayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPayer.addActionListener(e -> doPayment());
        main.add(btnPayer);
        main.add(Box.createVerticalStrut(15));

        JButton btnRetour = new JButton("Retour");
        btnRetour.setBackground(Color.decode("#7ac2c7"));
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRetour.setFocusPainted(false);
        btnRetour.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetour.setPreferredSize(new Dimension(150, 40));

        btnRetour.addActionListener(e -> {
            dispose();
            new FicheHebergement(conn, hebergement, client, dateArrivee, dateDepart, nbParents, nbEnfants, nbLits, controller);
        });

        main.add(btnRetour);
        add(main, BorderLayout.CENTER);
    }

    private JPanel createLabelAndField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(400, 50));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void doPayment() {
        try {
            String num = tfNum.getText().trim();
            String exp = tfExp.getText().trim();
            String cvv = tfCvv.getText().trim();

            if (!num.matches("\\d{4}"))
                throw new IllegalArgumentException("Numéro de carte : 4 chiffres attendus.");
            if (!exp.matches("(0[1-9]|1[0-2])/\\d{2}"))
                throw new IllegalArgumentException("Expiration invalide (MM/AA).");
            if (!cvv.matches("\\d{3}"))
                throw new IllegalArgumentException("CVV : 3 chiffres attendus.");

            String[] parts = exp.split("/");
            int mois = Integer.parseInt(parts[0]);
            int annee = 2000 + Integer.parseInt(parts[1]);

            MoyenPaiementDAO mdao = new MoyenPaiementDAO(conn);
            MoyenPaiement moyen = new MoyenPaiement(
                    0,
                    client.getIdClient(),
                    "Carte bancaire",
                    num,
                    mois,
                    annee,
                    cvv,
                    0.0
            );
            int idM = mdao.trouverOuCreer(moyen);

            Timestamp now = Timestamp.from(Instant.now());
            Paiement pmt = new Paiement(
                    0,
                    idResa,
                    montant,
                    "Carte bancaire",
                    now,
                    idM,
                    cvv
            );

            PaiementDAO pdao = new PaiementDAO(conn);
            boolean ok = pdao.enregistrerPaiement(pmt);

            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Paiement effectué !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Fonds insuffisants ou CVV invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur technique : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
