package view;

import controller.AuthController;
import dao.MailDAO;
import model.Mail;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AdminGererMailView extends JFrame {

    private final Connection connection;

    public AdminGererMailView(Connection connection) {
        this.connection = connection;

        setTitle("Gestion des Mails");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JLabel titre = new JLabel("Envoyer un mail", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);
        JPanel header = new JPanel();
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(800, 80));
        header.add(titre);

        // === PANNEAU CENTRAL ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Bloc destinataire
        JPanel destinatairePanel = createRoundedPanel(new Color(183, 176, 176));
        destinatairePanel.setLayout(new BoxLayout(destinatairePanel, BoxLayout.Y_AXIS));
        JLabel lblDest = new JLabel("Destinataire : (anciens / nouveaux / ID client)");
        JTextField destinataireField = new JTextField();
        destinatairePanel.add(lblDest);
        destinatairePanel.add(Box.createVerticalStrut(5));
        destinatairePanel.add(destinataireField);
        mainPanel.add(destinatairePanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Bloc message
        JPanel messagePanel = createRoundedPanel(new Color(227, 227, 227));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JLabel lblMsg = new JLabel("Tapez votre texte :");
        JTextArea contenuArea = new JTextArea(10, 40);
        contenuArea.setLineWrap(true);
        contenuArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(contenuArea);
        messagePanel.add(lblMsg);
        messagePanel.add(Box.createVerticalStrut(5));
        messagePanel.add(scroll);
        mainPanel.add(messagePanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Boutons envoyer et retour
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        JButton btnEnvoyer = createActionButton("Envoyer", new Color(89, 141, 144));
        JButton btnRetour = createActionButton("Retour", new Color(89, 141, 144));

        btnEnvoyer.addActionListener(e -> {
            String dest = destinataireField.getText().trim();
            String message = contenuArea.getText().trim();

            if (dest.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir les champs.");
                return;
            }

            try {
                MailDAO dao = new MailDAO(connection);
                int mailsEnvoyes = 0;

                if (dest.equalsIgnoreCase("anciens")) {
                    List<Integer> ids = dao.getClientsAvecReservation();
                    for (int id : ids) {
                        dao.envoyerMail(new Mail(id, "Message Admin", message));
                        mailsEnvoyes++;
                    }
                } else if (dest.equalsIgnoreCase("nouveaux")) {
                    List<Integer> ids = dao.getClientsSansReservation();
                    for (int id : ids) {
                        dao.envoyerMail(new Mail(id, "Message Admin", message));
                        mailsEnvoyes++;
                    }
                } else {
                    int idClient = Integer.parseInt(dest);
                    dao.envoyerMail(new Mail(idClient, "Message Admin", message));
                    mailsEnvoyes = 1;
                }

                JOptionPane.showMessageDialog(this, "Mail envoyé à " + mailsEnvoyes + " client(s).");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        btnRetour.addActionListener(e -> {
            dispose();
            new AccueilAdminView(null, connection); // à remplacer par admin si disponible
        });

        btnPanel.add(btnEnvoyer);
        btnPanel.add(btnRetour);
        mainPanel.add(btnPanel);

        // === AJOUT ===
        add(header, BorderLayout.NORTH);
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
        panel.setMaximumSize(new Dimension(700, 150));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }
}