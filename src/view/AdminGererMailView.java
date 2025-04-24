package view;

import dao.MailDAO;
import model.Administrateur;
import model.Mail;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AdminGererMailView extends JFrame {

    private final Administrateur admin;
    private final Connection connection;
    private final JPanel envoiBox;
    private final JPanel receptionBox;

    public AdminGererMailView(Administrateur admin, Connection connection) {
        this.admin = admin;
        this.connection = connection;

        setTitle("Gestion des Mails – Admin");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(1000, 80));
        JLabel titre = new JLabel("Gestion des Mails", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Color.WHITE);
        contenu.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        contenu.add(buildEnvoiForm());
        contenu.add(Box.createVerticalStrut(30));

        envoiBox = createRoundedPanel(new Color(89, 141, 144));
        envoiBox.setLayout(new BoxLayout(envoiBox, BoxLayout.Y_AXIS));
        contenu.add(envoiBox);
        contenu.add(Box.createVerticalStrut(30));

        receptionBox = createRoundedPanel(new Color(122, 194, 199));
        receptionBox.setLayout(new BoxLayout(receptionBox, BoxLayout.Y_AXIS));
        contenu.add(receptionBox);

        add(new JScrollPane(contenu), BorderLayout.CENTER);

        JButton retour = new JButton("Retour");
        retour.setBackground(new Color(122, 194, 199));
        retour.setForeground(Color.WHITE);
        retour.addActionListener(e -> {
            dispose();
            new AccueilAdminView(admin, connection);
        });

        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.add(retour);
        add(footer, BorderLayout.SOUTH);

        reloadEnvoi();
        reloadReception();

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel buildEnvoiForm() {
        JPanel form = createRoundedPanel(new Color(200, 200, 200));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.add(new JLabel("ID du client destinataire (ou tapez 'anciens' pour tous les anciens clients) :"));
        JTextField idField = new JTextField();
        form.add(idField);

        form.add(Box.createVerticalStrut(10));
        form.add(new JLabel("Message à envoyer :"));
        JTextArea messageArea = new JTextArea(5, 40);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        form.add(new JScrollPane(messageArea));
        form.add(Box.createVerticalStrut(10));

        JButton sendBtn = new JButton("Envoyer");
        sendBtn.setBackground(new Color(89, 141, 144));
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(sendBtn);

        sendBtn.addActionListener(e -> {
            String idText = idField.getText().trim();
            String msg = messageArea.getText().trim();
            if (idText.isEmpty() || msg.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Champs vides.");
                return;
            }

            try {
                MailDAO dao = new MailDAO(connection);
                if (idText.equalsIgnoreCase("anciens")) {
                    List<Integer> ids = dao.getClientsAnciens();
                    for (int idClient : ids) {
                        dao.envoyerMailAdminVersClient(admin.getIdAdministrateur(), idClient, "Message Admin", msg);
                    }
                    JOptionPane.showMessageDialog(this, "Message envoyé à tous les clients anciens.");
                } else {
                    int idClient = Integer.parseInt(idText);
                    dao.envoyerMailAdminVersClient(admin.getIdAdministrateur(), idClient, "Message Admin", msg);
                    JOptionPane.showMessageDialog(this, "Message envoyé au client #" + idClient);
                }
                reloadEnvoi();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        return form;
    }

    private void reloadEnvoi() {
        envoiBox.removeAll();
        envoiBox.add(new JLabel("📤 10 derniers mails envoyés :"));
        envoiBox.add(Box.createVerticalStrut(10));
        try {
            MailDAO dao = new MailDAO(connection);
            List<Mail> list = dao.getMailsEnvoyesParAdmin(admin.getIdAdministrateur(), 10);
            if (list.isEmpty()) {
                envoiBox.add(new JLabel("(Aucun message envoyé)"));
            } else {
                for (Mail m : list) {
                    String client = dao.getClientNomPrenom(m.getIdClient());
                    envoiBox.add(new JLabel("→ " + client + " : " + m.getContenu()));
                    envoiBox.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception ex) {
            envoiBox.add(new JLabel("Erreur : " + ex.getMessage()));
        }
        envoiBox.revalidate();
        envoiBox.repaint();
    }

    private void reloadReception() {
        receptionBox.removeAll();
        receptionBox.add(new JLabel("📥 10 derniers mails reçus :"));
        receptionBox.add(Box.createVerticalStrut(10));
        try {
            MailDAO dao = new MailDAO(connection);
            List<Mail> list = dao.getMailsRecusParAdmin(admin.getIdAdministrateur(), 10);
            if (list.isEmpty()) {
                receptionBox.add(new JLabel("(Aucun message reçu)"));
            } else {
                for (Mail m : list) {
                    String client = dao.getClientNomPrenom(m.getIdClient());
                    receptionBox.add(new JLabel("← " + client + " : " + m.getContenu()));
                    receptionBox.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception ex) {
            receptionBox.add(new JLabel("Erreur : " + ex.getMessage()));
        }
        receptionBox.revalidate();
        receptionBox.repaint();
    }

    private JPanel createRoundedPanel(Color bg) {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }
}
