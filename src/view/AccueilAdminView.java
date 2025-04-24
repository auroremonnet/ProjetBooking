package view;

import controller.AdminController;
import controller.AuthController;
import model.Administrateur;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AccueilAdminView extends JFrame {
    private final Connection connection;
    private final Administrateur admin;

    public AccueilAdminView(Administrateur admin, Connection connection) {
        this.connection = connection;
        this.admin = admin;

        setTitle("Accueil Administrateur");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(900, 70));

        JLabel titre = new JLabel("Tableau de bord administrateur", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 34));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(Color.WHITE);

        JPanel profilBox = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(227, 227, 227));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        profilBox.setLayout(new BoxLayout(profilBox, BoxLayout.Y_AXIS));
        profilBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        profilBox.setMaximumSize(new Dimension(300, 200));
        profilBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        String photoPath = "/resources/images/" + admin.getPhoto();
        java.net.URL imageUrl = getClass().getResource(photoPath);

        JLabel photo;
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            photo = new JLabel(new ImageIcon(scaled));
        } else {
            System.err.println("❌ Image introuvable : " + photoPath);
            photo = new JLabel("📷 Image manquante");
        }
        photo.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilBox.add(photo);

        JLabel nomPrenom = new JLabel(admin.getPrenom() + " " + admin.getNom());
        nomPrenom.setFont(new Font("Arial", Font.BOLD, 20));
        nomPrenom.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomPrenom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        profilBox.add(nomPrenom);

        panel.add(profilBox);
        panel.add(Box.createVerticalStrut(20));

        JPanel infoPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(89, 141, 144));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        infoPanel.setLayout(new GridLayout(2, 1));
        infoPanel.setMaximumSize(new Dimension(800, 80));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        infoPanel.add(new JLabel("Email : " + admin.getEmail()));
        infoPanel.add(new JLabel("Mot de passe : " + admin.getMotDePasse()));
        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(30));

        JButton btn1 = createRoundedButton("Ajouter / Supprimer des propriétés",
                e -> new AdminGererProprieteView(connection));
        JButton btn2 = createRoundedButton("Gérer la clientèle",
                e -> new AdminGererClientView(connection));
        JButton btn3 = createRoundedButton("Ajouter des réductions",
                e -> {
                    AdminController controller = new AdminController(connection);
                    new AdminGererReductionView(controller).setVisible(true);
                });
        JButton logoutBtn = createRoundedButton("Déconnexion",
                e -> {
                    dispose();
                    new AuthView(new AuthController(connection), connection);
                });
        JButton btnMail = createRoundedButton("Envoyer un mail",
                e -> new AdminGererMailView(admin, connection));

        panel.add(btnMail);
        panel.add(Box.createVerticalStrut(15));
        panel.add(createRoundedButton("Statistiques",
                e -> new ReportingView(admin, connection)));
        panel.add(Box.createVerticalStrut(15));
        panel.add(btn1);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btn2);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btn3);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnMail);
        panel.add(Box.createVerticalStrut(30));
        panel.add(logoutBtn);
        panel.add(logoutBtn);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JButton createRoundedButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(122, 194, 199));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setMaximumSize(new Dimension(500, 50));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(122, 194, 199), 5, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        button.addActionListener(action);
        return button;
    }
}