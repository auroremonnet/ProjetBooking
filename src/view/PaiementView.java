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

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

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

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(700, 60));


        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(0, 70));
        JLabel titre = new JLabel("Il Est L'Heure de Payer", SwingConstants.CENTER);
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

        // Panneau réduction (#b7b0b0)
        JPanel reductionPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#b7b0b0"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        reductionPanel.setOpaque(false);
        reductionPanel.setLayout(new BoxLayout(reductionPanel, BoxLayout.Y_AXIS));
        reductionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reductionPanel.setMaximumSize(new Dimension(500, 80));

        JLabel lblReduction = new JLabel("Réduction " + client.getTypeClient() +
                " : -" + tauxReduction + "%");
        lblReduction.setFont(new Font("Arial", Font.PLAIN, 16));
        lblReduction.setForeground(Color.WHITE);
        lblReduction.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMontantRemise = new JLabel("Après réduction : " +
                String.format("%.2f", montantFinal) + " €");
        lblMontantRemise.setFont(new Font("Arial", Font.BOLD, 18));
        lblMontantRemise.setForeground(Color.WHITE);
        lblMontantRemise.setAlignmentX(Component.CENTER_ALIGNMENT);

        reductionPanel.add(Box.createVerticalStrut(10));
        reductionPanel.add(lblReduction);
        reductionPanel.add(Box.createVerticalStrut(5));
        reductionPanel.add(lblMontantRemise);
        reductionPanel.add(Box.createVerticalStrut(10));

        main.add(reductionPanel);
        main.add(Box.createVerticalStrut(30));

        // Panneau paiement (#e3e3e3)
        JPanel paiementPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#e3e3e3"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        paiementPanel.setOpaque(false);
        paiementPanel.setLayout(new BoxLayout(paiementPanel, BoxLayout.Y_AXIS));
        paiementPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        paiementPanel.setMaximumSize(new Dimension(500, 300));

        tfNum = new JTextField();
        tfExp = new JTextField();
        tfCvv = new JTextField();

        paiementPanel.add(Box.createVerticalStrut(15));
        paiementPanel.add(createLabelAndField("Numéro de carte (4 chiffres) :", tfNum));
        paiementPanel.add(Box.createVerticalStrut(10));
        paiementPanel.add(createLabelAndField("Date d'expiration (MM/AA) :", tfExp));
        paiementPanel.add(Box.createVerticalStrut(10));
        paiementPanel.add(createLabelAndField("CVV (3 chiffres) :", tfCvv));
        paiementPanel.add(Box.createVerticalStrut(20));

        JButton btnPayer = new JButton("Valider le paiement");
        btnPayer.setBackground(Color.decode("#598d90"));
        btnPayer.setForeground(Color.WHITE);
        btnPayer.setFont(new Font("Arial", Font.BOLD, 18));
        btnPayer.setFocusPainted(false);
        btnPayer.setPreferredSize(new Dimension(250, 50));
        btnPayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPayer.addActionListener(e -> doPayment());
        paiementPanel.add(btnPayer);
        paiementPanel.add(Box.createVerticalStrut(15));

        main.add(paiementPanel);
        main.add(Box.createVerticalStrut(20));

        // Bouton Retour
        JButton btnRetour = new JButton("Retour");
        btnRetour.setBackground(Color.decode("#7ac2c7"));
        btnRetour.setForeground(Color.BLACK);
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRetour.setFocusPainted(false);
        btnRetour.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRetour.setPreferredSize(new Dimension(150, 40));
        btnRetour.addActionListener(e -> {
            dispose();
            new FicheHebergement(conn, hebergement, client,
                    dateArrivee, dateDepart,
                    nbParents, nbEnfants, nbLits,
                    controller);
        });

        main.add(btnRetour);
        add(main, BorderLayout.CENTER);
    }

    private JPanel createLabelAndField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
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
                throw new IllegalArgumentException("Numéro de carte : 4 chiffres attendus.");
            if (!exp.matches("(0[1-9]|1[0-2])/\\d{2}"))
                throw new IllegalArgumentException("Expiration invalide (MM/AA).");
            if (!cvv.matches("\\d{3}"))
                throw new IllegalArgumentException("CVV : 3 chiffres attendus.");

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
                JOptionPane.showMessageDialog(this, "❌ Fonds insuffisants ou CVV invalide",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(),
                    "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur technique : " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
