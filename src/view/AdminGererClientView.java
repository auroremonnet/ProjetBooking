package view;

import controller.AdminController;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AdminGererClientView extends JFrame {
    private final AdminController controller;
    private final Connection connection;
    private JScrollPane scrollPane;

    private JTextField nomField, prenomField, emailField, mdpField,
            typeClientField, adresseField, telField, idSuppressionField;

    public AdminGererClientView(Connection conn) {
        this.connection = conn;
        this.controller = new AdminController(conn);

        setTitle("Admin - Gestion des clients");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(122, 194, 199));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(1000, 70));
        JLabel titre = new JLabel("Gérer la clientèle", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        chargerClients();
        add(buildFormPanel(), BorderLayout.SOUTH);

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void chargerClients() {
        if (scrollPane != null) {
            getContentPane().remove(scrollPane);
        }
        try {
            List<Client> clients = controller.listerClients();
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.WHITE);

            for (Client c : clients) {
                JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(227, 227, 227));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    }
                };
                ligne.setOpaque(false);
                ligne.setPreferredSize(new Dimension(950, 80));
                ligne.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                String info = String.format("<html><b>ID:</b> %d | %s %s | %s<br>Email: %s | Tél: %s | Type: %s</html>",
                        c.getIdClient(), c.getPrenom(), c.getNom(), c.getAdresse(),
                        c.getEmail(), c.getTelephone(), c.getTypeClient());

                JLabel label = new JLabel(info);
                label.setPreferredSize(new Dimension(850, 60));
                label.setForeground(Color.BLACK);

                ligne.add(label);
                contentPanel.add(ligne);
                contentPanel.add(Box.createVerticalStrut(10));
            }

            scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);

            revalidate();
            repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(new Color(89, 141, 144));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        fieldsPanel.setOpaque(false);

        nomField = new JTextField(); prenomField = new JTextField(); emailField = new JTextField();
        mdpField = new JTextField(); typeClientField = new JTextField();
        adresseField = new JTextField(); telField = new JTextField(); idSuppressionField = new JTextField();

        fieldsPanel.add(createLabel("Nom")); fieldsPanel.add(nomField);
        fieldsPanel.add(createLabel("Prénom")); fieldsPanel.add(prenomField);
        fieldsPanel.add(createLabel("Email")); fieldsPanel.add(emailField);
        fieldsPanel.add(createLabel("Mot de passe")); fieldsPanel.add(mdpField);
        fieldsPanel.add(createLabel("Type client")); fieldsPanel.add(typeClientField);
        fieldsPanel.add(createLabel("Adresse")); fieldsPanel.add(adresseField);
        fieldsPanel.add(createLabel("Téléphone")); fieldsPanel.add(telField);
        fieldsPanel.add(createLabel("ID à supprimer")); fieldsPanel.add(idSuppressionField);

        formPanel.add(Box.createVerticalStrut(15), BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JButton ajouterBtn = createStyledButton("Ajouter");
        ajouterBtn.addActionListener(e -> ajouterClient());
        JButton supprimerBtn = createStyledButton("Supprimer");
        supprimerBtn.addActionListener(e -> supprimerClient());
        JButton retourBtn = createStyledButton("⬅ Retour");
        retourBtn.addActionListener(e -> {
            dispose();
            new AccueilAdminView(null, connection);
        });

        buttonPanel.add(retourBtn);
        buttonPanel.add(ajouterBtn);
        buttonPanel.add(supprimerBtn);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(new Color(183, 176, 176));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }

    private void ajouterClient() {
        try {
            Client c = new Client(0, nomField.getText(), prenomField.getText(), emailField.getText(),
                    mdpField.getText(), typeClientField.getText(), adresseField.getText(), telField.getText());

            boolean ok = controller.ajouterClient(c);
            if (ok) {
                chargerClients();
                nomField.setText("");
                prenomField.setText("");
                emailField.setText("");
                mdpField.setText("");
                typeClientField.setText("");
                adresseField.setText("");
                telField.setText("");
                idSuppressionField.setText("");

                JOptionPane.showMessageDialog(this, "✅ Client ajouté.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Echec de l'ajout.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerClient() {
        try {
            int id = Integer.parseInt(idSuppressionField.getText());
            boolean ok = controller.supprimerClient(id);
            if (ok) {
                chargerClients();
                JOptionPane.showMessageDialog(this, "✅ Client supprimé.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ ID introuvable.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
}