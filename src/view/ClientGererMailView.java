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
    private final BookingController controller;

    public ClientGererMailView(Client client, Connection connection, BookingController controller) {
        this.client = client;
        this.connection = connection;
        this.controller = controller;

        setTitle("📨 Messagerie Client");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(900, 80));
        JLabel titre = new JLabel("Messagerie Client", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // CONTENU
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        contenu.setBackground(Color.WHITE);

        // Mails reçus
        JPanel recuPanel = createRoundedPanel(new Color(227, 227, 227));
        recuPanel.setLayout(new BoxLayout(recuPanel, BoxLayout.Y_AXIS));
        recuPanel.add(new JLabel("📥 Vos 10 derniers messages reçus :"));
        recuPanel.add(Box.createVerticalStrut(10));

        try {
            MailDAO dao = new MailDAO(connection);
            List<Mail> mails = dao.getMailsRecusParClient(client.getIdClient(), 10);

            if (mails.isEmpty()) {
                recuPanel.add(new JLabel("Aucun message reçu."));
            } else {
                for (Mail m : mails) {
                    String adminNom = dao.getAdminNomPrenom(m.getIdAdministrateur());
                    JLabel mail = new JLabel("• [" + m.getDateEnvoi() + "] " + m.getObjet() +
                            " – " + m.getContenu() + " (de " + adminNom + ")");
                    recuPanel.add(mail);
                    recuPanel.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception e) {
            recuPanel.add(new JLabel("⚠️ Erreur de chargement des mails."));
        }

        contenu.add(recuPanel);
        contenu.add(Box.createVerticalStrut(30));

        // Formulaire d'envoi
        JPanel envoiPanel = createRoundedPanel(new Color(183, 176, 176));
        envoiPanel.setLayout(new BoxLayout(envoiPanel, BoxLayout.Y_AXIS));
        envoiPanel.add(new JLabel("✉️ Envoyer un message à un administrateur :"));
        envoiPanel.add(Box.createVerticalStrut(10));

        envoiPanel.add(new JLabel("Prénom de l’admin :"));
        JTextField prenomField = new JTextField();
        envoiPanel.add(prenomField);
        envoiPanel.add(Box.createVerticalStrut(5));

        envoiPanel.add(new JLabel("Nom de l’admin :"));
        JTextField nomField = new JTextField();
        envoiPanel.add(nomField);
        envoiPanel.add(Box.createVerticalStrut(5));

        envoiPanel.add(new JLabel("Message :"));
        JTextArea messageArea = new JTextArea(5, 40);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        envoiPanel.add(new JScrollPane(messageArea));
        envoiPanel.add(Box.createVerticalStrut(10));

        JButton envoyerBtn = new JButton("Envoyer");
        envoyerBtn.setBackground(new Color(89, 141, 144));
        envoyerBtn.setForeground(Color.WHITE);
        envoyerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        envoiPanel.add(envoyerBtn);

        envoyerBtn.addActionListener(e -> {
            String prenom = prenomField.getText().trim();
            String nom = nomField.getText().trim();
            String contenuMsg = messageArea.getText().trim();

            if (prenom.isEmpty() || nom.isEmpty() || contenuMsg.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }

            try {
                MailDAO dao = new MailDAO(connection);
                int idAdmin = dao.getIdAdminParPrenomNom(prenom, nom);
                dao.envoyerMailClientVersAdmin(client.getIdClient(), idAdmin, "Message Client", contenuMsg);
                JOptionPane.showMessageDialog(this, "✅ Message envoyé à " + prenom + " " + nom + ".");
                dispose();
                new ClientGererMailView(client, connection, controller);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        contenu.add(envoiPanel);
        contenu.add(Box.createVerticalStrut(30));

        // Bouton retour
        JButton retourBtn = new JButton("Retour");
        retourBtn.setBackground(new Color(122, 194, 199));
        retourBtn.setForeground(Color.WHITE);
        retourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retourBtn.addActionListener(e -> {
            dispose();
            new MonCompteView(client, connection, controller);
        });

        contenu.add(retourBtn);

        add(new JScrollPane(contenu), BorderLayout.CENTER);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createRoundedPanel(Color color) {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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
