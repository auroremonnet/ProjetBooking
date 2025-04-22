package view;

import controller.BookingController;
import dao.MailDAO;
import model.Client;
import model.Mail;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ClientGererMailView extends JFrame {

    private final Connection connection;
    private final Client client;
    private final BookingController controller; // ✅ déplacé ici

    public ClientGererMailView(Client client, Connection connection, BookingController controller) {
        this.client = client;
        this.connection = connection;
        this.controller = controller;

        setTitle("Mes Mails");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JLabel titre = new JLabel("Messagerie Client", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);
        JPanel header = new JPanel();
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(900, 100));
        header.add(titre);
        add(header, BorderLayout.NORTH);

        // === PANEL PRINCIPAL ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(Color.WHITE);

        // === Derniers mails ===
        JPanel mailsPanel = createRoundedPanel(new Color(227, 227, 227));
        mailsPanel.setLayout(new BoxLayout(mailsPanel, BoxLayout.Y_AXIS));
        JLabel labelDerniers = new JLabel("📥 Vos 10 derniers mails reçus :");
        labelDerniers.setFont(new Font("Arial", Font.BOLD, 16));
        mailsPanel.add(labelDerniers);
        mailsPanel.add(Box.createVerticalStrut(10));

        try {
            MailDAO dao = new MailDAO(connection);
            List<Mail> mails = dao.getDerniersMailsPourClient(client.getIdClient(), 10);
            if (mails.isEmpty()) {
                mailsPanel.add(new JLabel("Aucun mail pour l’instant."));
            } else {
                for (Mail m : mails) {
                    JLabel mailLabel = new JLabel("• " + m.getObjet() + " – " + m.getMessage());
                    mailsPanel.add(mailLabel);
                    mailsPanel.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception e) {
            mailsPanel.add(new JLabel("Erreur de chargement des mails."));
        }

        mainPanel.add(mailsPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // === Envoyer un message à un admin ===
        JPanel envoiPanel = createRoundedPanel(new Color(183, 176, 176));
        envoiPanel.setLayout(new BoxLayout(envoiPanel, BoxLayout.Y_AXIS));

        JLabel labelEnvoyer = new JLabel("📨 Envoyer un message à un administrateur");
        labelEnvoyer.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField prenomField = new JTextField();
        JTextField nomField = new JTextField();
        JTextArea messageArea = new JTextArea(5, 40);
        JScrollPane messageScroll = new JScrollPane(messageArea);

        envoiPanel.add(labelEnvoyer);
        envoiPanel.add(Box.createVerticalStrut(10));
        envoiPanel.add(new JLabel("Prénom de l’admin :"));
        envoiPanel.add(prenomField);
        envoiPanel.add(Box.createVerticalStrut(5));
        envoiPanel.add(new JLabel("Nom de l’admin :"));
        envoiPanel.add(nomField);
        envoiPanel.add(Box.createVerticalStrut(5));
        envoiPanel.add(new JLabel("Message :"));
        envoiPanel.add(messageScroll);

        JButton envoyerBtn = new JButton("Envoyer");
        envoyerBtn.setBackground(new Color(89, 141, 144));
        envoyerBtn.setForeground(Color.WHITE);
        envoyerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        envoyerBtn.addActionListener(e -> {
            String prenom = prenomField.getText().trim();
            String nom = nomField.getText().trim();
            String msg = messageArea.getText().trim();
            if (prenom.isEmpty() || nom.isEmpty() || msg.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }
            try {
                MailDAO dao = new MailDAO(connection);
                int idAdmin = dao.getIdAdminParPrenomNom(prenom, nom);
                dao.envoyerMailClientVersAdmin(client.getIdClient(), idAdmin, "Message Client", msg);
                JOptionPane.showMessageDialog(this, "Message envoyé à l’admin " + prenom + " " + nom + ".");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        envoiPanel.add(Box.createVerticalStrut(10));
        envoiPanel.add(envoyerBtn);

        mainPanel.add(envoiPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // === Bouton Retour ===
        JButton retourBtn = new JButton("Retour");
        retourBtn.setBackground(new Color(122, 194, 199));
        retourBtn.setForeground(Color.BLACK);
        retourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourBtn.addActionListener(e -> {
            dispose();
            new MonCompteView(client, connection, controller);
        });

        mainPanel.add(retourBtn);
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
        panel.setMaximumSize(new Dimension(800, 400));
        return panel;
    }
}