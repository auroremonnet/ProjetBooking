// view/AdminGererMailView.java
package view;

import dao.MailDAO;
import model.Administrateur;
import model.Mail;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.util.List;

public class AdminGererMailView extends JFrame {
    private final Administrateur admin;
    private final Connection connection;
    private final JPanel envoiBox;
    private final JPanel receptionBox;

    public AdminGererMailView(Administrateur admin, Connection connection) {
        this.admin      = admin;
        this.connection = connection;

        setTitle("Gestion des Mails");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(900, 80));
        JLabel titre = new JLabel("Gestion des Mails", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // CONTENU PRINCIPAL
        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        contenu.setBackground(Color.WHITE);

        // 1) Formulaire d'envoi
        contenu.add(buildEnvoiForm());
        contenu.add(Box.createVerticalStrut(30));

        // 2) Boîte d'envoi
        envoiBox = createRoundedPanel(new Color(89, 141, 144));
        envoiBox.setLayout(new BoxLayout(envoiBox, BoxLayout.Y_AXIS));
        contenu.add(envoiBox);
        contenu.add(Box.createVerticalStrut(30));

        // 3) Boîte de réception
        receptionBox = createRoundedPanel(new Color(122, 194, 199));
        receptionBox.setLayout(new BoxLayout(receptionBox, BoxLayout.Y_AXIS));
        contenu.add(receptionBox);

        add(new JScrollPane(contenu,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER);

        // FOOTER
        JButton retour = new JButton("Retour");
        retour.setBackground(new Color(122, 194, 199));
        retour.setForeground(Color.WHITE);
        retour.addActionListener(e -> {
            dispose();
            new AccueilAdminView(admin, connection);
        });
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        footer.add(retour);
        add(footer, BorderLayout.SOUTH);

        // Chargement initial
        reloadEnvoi();
        reloadReception();

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel buildEnvoiForm() {
        JPanel form = createRoundedPanel(new Color(183, 176, 176));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.add(new JLabel("Destinataire : (anciens / nouveaux / ID client)"));
        JTextField destField = new JTextField();
        form.add(Box.createVerticalStrut(5));
        form.add(destField);
        form.add(Box.createVerticalStrut(15));

        form.add(new JLabel("Message :"));
        JTextArea area = new JTextArea(6, 40);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        form.add(Box.createVerticalStrut(5));
        form.add(new JScrollPane(area));
        form.add(Box.createVerticalStrut(15));

        JButton send = new JButton("Envoyer");
        send.setBackground(new Color(89, 141, 144));
        send.setForeground(Color.WHITE);
        send.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(send);

        send.addActionListener(e -> {
            String d     = destField.getText().trim();
            String texte = area.getText().trim();
            if (d.isEmpty() || texte.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
                return;
            }
            try {
                MailDAO dao = new MailDAO(connection);
                int count = 0;
                if ("anciens".equalsIgnoreCase(d)) {
                    for (int id : dao.getClientsAvecReservation()) {
                        dao.envoyerMail(new Mail(
                                id,
                                admin.getIdAdministrateur(),
                                "Message Admin",
                                texte
                        ));
                        count++;
                    }
                } else if ("nouveaux".equalsIgnoreCase(d)) {
                    for (int id : dao.getClientsSansReservation()) {
                        dao.envoyerMail(new Mail(
                                id,
                                admin.getIdAdministrateur(),
                                "Message Admin",
                                texte
                        ));
                        count++;
                    }
                } else {
                    int idC = Integer.parseInt(d);
                    dao.envoyerMail(new Mail(
                            idC,
                            admin.getIdAdministrateur(),
                            "Message Admin",
                            texte
                    ));
                    count = 1;
                }
                JOptionPane.showMessageDialog(this, "Mail envoyé à " + count + " client(s).");
                reloadEnvoi();
                reloadReception();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Erreur d’envoi : " + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        return form;
    }

    // Dans AdminGererMailView.java

    /**
     * Recharge la boîte d’envoi : seuls les mails émis par l’admin courant.
     */
    private void reloadEnvoi() {
        envoiBox.removeAll();
        envoiBox.add(new JLabel("Boîte d’envoi – 10 derniers mails envoyés"));
        envoiBox.add(Box.createVerticalStrut(10));

        try {
            MailDAO dao = new MailDAO(connection);
            // UNIQUEMENT les mails où l’admin est expéditeur
            List<Mail> list = dao.getDerniersMailsEnvoyesParAdmin(
                    admin.getIdAdministrateur(), 10
            );

            if (list.isEmpty()) {
                envoiBox.add(new JLabel("(vide)"));
            } else {
                for (Mail m : list) {
                    // destinataire = client
                    String clientNom = dao.getClientNomPrenom(m.getIdClient());
                    envoiBox.add(new JLabel(
                            "→ " + clientNom
                                    + "  [" + m.getObjet() + "]  " + m.getContenu()
                    ));
                    envoiBox.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception ex) {
            envoiBox.add(new JLabel("Erreur de chargement : " + ex.getMessage()));
        }

        envoiBox.revalidate();
        envoiBox.repaint();
    }

    /**
     * Recharge la boîte de réception : seuls les mails émis par les clients.
     */
    private void reloadReception() {
        receptionBox.removeAll();
        receptionBox.add(new JLabel("Boîte de réception – 10 derniers mails reçus"));
        receptionBox.add(Box.createVerticalStrut(10));

        try {
            MailDAO dao = new MailDAO(connection);
            // UNIQUEMENT les mails où l’admin est destinataire
            List<Mail> list = dao.getDerniersMailsRecusPourAdmin(
                    admin.getIdAdministrateur(), 10
            );

            if (list.isEmpty()) {
                receptionBox.add(new JLabel("(vide)"));
            } else {
                for (Mail m : list) {
                    // expéditeur = client
                    String clientNom = dao.getClientNomPrenom(m.getIdClient());
                    receptionBox.add(new JLabel(
                            "← " + clientNom
                                    + "  [" + m.getObjet() + "]  " + m.getContenu()
                    ));
                    receptionBox.add(Box.createVerticalStrut(5));
                }
            }
        } catch (Exception ex) {
            receptionBox.add(new JLabel("Erreur de chargement : " + ex.getMessage()));
        }

        receptionBox.revalidate();
        receptionBox.repaint();
    }

    private JPanel createRoundedPanel(Color bg) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        return p;
    }
}
