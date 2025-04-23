package view;

import controller.AuthController;
import controller.BookingController;
import model.Administrateur;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AuthView extends JFrame {
    private AuthController authController;
    private final Connection connection;

    private Client clientConnecte; // Client connecté

    // Composants UI
    private JTextField loginEmailField, regNameField, regSurnameField, regEmailField, regAddressField, regPhoneField, regAdminKeyField;
    private JPasswordField loginPasswordField, regPasswordField;
    private JRadioButton rbClientLogin, rbAdminLogin, rbClientRegister, rbAdminRegister;
    private JLabel adminKeyLabel;
    private ButtonGroup bgLoginType, bgRegisterType;

    public AuthView(AuthController authController, Connection connection) {
        this.authController = authController;
        this.connection = connection;
        setTitle("Authentification");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initUI();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initUI() {
        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#7ac2c7"));
        header.setPreferredSize(new Dimension(700, 60));
        JLabel titre = new JLabel("Authentification", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 34));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // CONTENU CENTRAL
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // --- PANNEAU CONNEXION ---
        JPanel loginPanel = createRoundedPanel();
        JLabel loginTitle = new JLabel("Connexion");
        loginTitle.setFont(new Font("Arial", Font.BOLD, 16));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(loginTitle);
        loginPanel.add(Box.createVerticalStrut(10));

        rbClientLogin = new JRadioButton("Client");
        rbAdminLogin = new JRadioButton("Administrateur");
        rbClientLogin.setSelected(true);
        bgLoginType = new ButtonGroup();
        bgLoginType.add(rbClientLogin);
        bgLoginType.add(rbAdminLogin);
        JPanel typeLoginPanel = new JPanel(); typeLoginPanel.add(rbClientLogin); typeLoginPanel.add(rbAdminLogin);
        loginPanel.add(typeLoginPanel);

        loginEmailField = new JTextField(20);
        loginPasswordField = new JPasswordField(20);
        loginPanel.add(new JLabel("Email :"));
        loginPanel.add(loginEmailField);
        loginPanel.add(Box.createVerticalStrut(5));
        loginPanel.add(new JLabel("Mot de passe :"));
        loginPanel.add(loginPasswordField);
        loginPanel.add(Box.createVerticalStrut(10));

        JButton loginButton = new JButton("Se connecter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(Color.decode("#e3e3e3"));
        loginButton.addActionListener(e -> doLogin());
        loginPanel.add(loginButton);

        // --- PANNEAU INSCRIPTION ---
        JPanel registerPanel = createRoundedPanel();
        JLabel regTitle = new JLabel("Créer un compte");
        regTitle.setFont(new Font("Arial", Font.BOLD, 16));
        regTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerPanel.add(regTitle);
        registerPanel.add(Box.createVerticalStrut(10));

        rbClientRegister = new JRadioButton("Client");
        rbAdminRegister = new JRadioButton("Administrateur");
        rbClientRegister.setSelected(true);
        bgRegisterType = new ButtonGroup();
        bgRegisterType.add(rbClientRegister);
        bgRegisterType.add(rbAdminRegister);
        JPanel typeRegPanel = new JPanel(); typeRegPanel.add(rbClientRegister); typeRegPanel.add(rbAdminRegister);
        registerPanel.add(typeRegPanel);

        rbAdminRegister.addActionListener(e -> toggleAdminKey(true));
        rbClientRegister.addActionListener(e -> toggleAdminKey(false));

        regNameField = new JTextField(20);
        regSurnameField = new JTextField(20);
        regEmailField = new JTextField(20);
        regPasswordField = new JPasswordField(20);
        regAddressField = new JTextField(20);
        regPhoneField = new JTextField(20);
        regAdminKeyField = new JTextField(10);
        adminKeyLabel = new JLabel("Clé Admin :");
        adminKeyLabel.setVisible(false);
        regAdminKeyField.setVisible(false);

        registerPanel.add(new JLabel("Nom :")); registerPanel.add(regNameField);
        registerPanel.add(new JLabel("Prénom :")); registerPanel.add(regSurnameField);
        registerPanel.add(new JLabel("Email :")); registerPanel.add(regEmailField);
        registerPanel.add(new JLabel("Mot de passe :")); registerPanel.add(regPasswordField);
        registerPanel.add(new JLabel("Adresse :")); registerPanel.add(regAddressField);
        registerPanel.add(new JLabel("Téléphone :")); registerPanel.add(regPhoneField);
        registerPanel.add(adminKeyLabel); registerPanel.add(regAdminKeyField);

        JButton registerButton = new JButton("Créer le compte");
        registerButton.setBackground(Color.decode("#e3e3e3"));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> doRegister());
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(registerButton);

        // AJOUT
        centerPanel.add(loginPanel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(registerPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#7ac2c7"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(500, 400));
        return panel;
    }

    private void toggleAdminKey(boolean show) {
        adminKeyLabel.setVisible(show);
        regAdminKeyField.setVisible(show);
        revalidate(); repaint();
    }

    private void doLogin() {
        String email = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        try {
            if (rbClientLogin.isSelected()) {
                clientConnecte = authController.loginClient(email, password);
                if (clientConnecte != null) {
                    JOptionPane.showMessageDialog(this, "Connexion réussie !");
                    dispose();
                    BookingController controller = new BookingController(connection);
                    new MainView(controller, clientConnecte, connection);

                } else {
                    JOptionPane.showMessageDialog(this, "Identifiants incorrects pour client.");
                }
            } else {
                Administrateur admin = authController.loginAdmin(email, password);
                if (admin != null) {
                    JOptionPane.showMessageDialog(this, "Connexion administrateur réussie !");
                    dispose();
                    new AccueilAdminView(admin, connection);
                } else {
                    JOptionPane.showMessageDialog(this, "Identifiants incorrects pour administrateur.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void doRegister() {
        String nom = regNameField.getText().trim();
        String prenom = regSurnameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword());
        String adresse = regAddressField.getText().trim();
        String tel = regPhoneField.getText().trim();

        try {
            if (rbClientRegister.isSelected()) {
                Client c = new Client(0, nom, prenom, email, password, "nouveau", adresse, tel);
                if (authController.registerClient(c)) {
                    JOptionPane.showMessageDialog(this, "Compte client créé !");
                }
            } else {
                String key = regAdminKeyField.getText().trim();
                if (!"86".equals(key)) {
                    JOptionPane.showMessageDialog(this, "Clé admin incorrecte.");
                    return;
                }
                Administrateur a = new Administrateur(nom, prenom, email, password);
                if (authController.registerAdmin(a)) {
                    JOptionPane.showMessageDialog(this, "Compte admin créé !");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    public Client getClientConnecte() {
        return clientConnecte;
    }
}