package view;

import controller.AdminController;
import controller.AuthController;
import model.Administrateur;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccueilAdminView extends JFrame {
    private final Connection connection;
    private final Administrateur admin;

    public AccueilAdminView(Administrateur admin, Connection connection) {
        this.connection = connection;
        this.admin = admin;

        setTitle("Accueil Administrateur");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(122, 194, 199));
        header.setPreferredSize(new Dimension(0, 70));
        JLabel titre = new JLabel("Tableau de bord administrateur", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 34));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Chargement photo admin
        String filename = null;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT photo FROM administrateur WHERE idAdministrateur = ?"
            );
            ps.setInt(1, admin.getIdAdministrateur());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                filename = rs.getString("photo");
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JPanel profilBox = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
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

        JLabel photoLabel;
        if (filename != null) {
            String resourcePath = "images/" + filename;
            URL url = getClass().getClassLoader().getResource(resourcePath);

            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                photoLabel = new JLabel(new ImageIcon(img));
            } else {
                File f = new File("resources/images/" + filename);
                if (f.exists()) {
                    ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    photoLabel = new JLabel(new ImageIcon(img));
                } else {
                    System.err.println("❌ Photo introuvable : " + filename);
                    photoLabel = new JLabel("📷 Photo manquante");
                }
            }
        } else {
            photoLabel = new JLabel("📷 Photo manquante");
        }
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilBox.add(photoLabel);

        JLabel nomPrenom = new JLabel(admin.getPrenom() + " " + admin.getNom(), SwingConstants.CENTER);
        nomPrenom.setFont(new Font("Arial", Font.BOLD, 20));
        nomPrenom.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomPrenom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        profilBox.add(nomPrenom);

        panel.add(profilBox);
        panel.add(Box.createVerticalStrut(20));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(89, 141, 144));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        infoPanel.setMaximumSize(new Dimension(800, 80));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoPanel.add(new JLabel("Email : " + admin.getEmail()));
        infoPanel.add(new JLabel("Mot de passe : " + admin.getMotDePasse()));
        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(30));

        // Boutons
        panel.add(createRoundedButton("Statistiques", e -> new ReportingView(admin, connection)));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createRoundedButton("Ajouter / Supprimer des propriétés", e -> new AdminGererProprieteView(connection)));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createRoundedButton("Gérer la clientèle", e -> new AdminGererClientView(connection)));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createRoundedButton("Ajouter des réductions", e -> {
            AdminController adminController = new AdminController(connection);
            new AdminGererReductionView(adminController).setVisible(true);
        }));
        panel.add(Box.createVerticalStrut(30));
        panel.add(createRoundedButton("Envoyer un mail", e -> new AdminGererMailView(admin, connection)));
        panel.add(Box.createVerticalStrut(30));
        panel.add(createRoundedButton("Déconnexion", e -> {
            dispose();
            new AuthView(new AuthController(connection), connection);
        }));

        // On emballe le centre dans un JScrollPane
        JScrollPane scrollCenter = new JScrollPane(
                panel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

// Pour un défilement plus fluide à la molette
        scrollCenter.getVerticalScrollBar().setUnitIncrement(16);

// (Optionnel) Supprimez la bordure si vous voulez que ça colle aux bords
        scrollCenter.setBorder(null);

// Et on ajoute ce JScrollPane à la fenêtre
        add(scrollCenter, BorderLayout.CENTER);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
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
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.addActionListener(action);
        return button;
    }
}
