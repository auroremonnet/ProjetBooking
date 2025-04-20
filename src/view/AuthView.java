package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import controller.AuthController;
import model.Administrateur;
import model.Client;
import util.DBConnection; // Assurez-vous que la classe DBConnection se trouve bien dans le package util

public class AuthView extends JFrame {
    private AuthController authController;
    private JTabbedPane tabbedPane;

    // Composants pour la connexion
    private JRadioButton rbClientLogin, rbAdminLogin;
    private ButtonGroup bgLoginType;
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;

    // Composants pour l'inscription
    private JRadioButton rbClientRegister, rbAdminRegister;
    private ButtonGroup bgRegisterType;
    private JTextField regNameField, regSurnameField, regEmailField, regAddressField, regPhoneField;
    private JPasswordField regPasswordField;
    private JTextField regAdminKeyField;
    private JLabel adminKeyLabel;
    private JButton registerButton;

    public AuthView(AuthController authController) {
        this.authController = authController;
        setTitle("Authentification");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // ------------------------
        // Onglet Connexion
        // ------------------------
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // [Modifié] Ajout d'un heading précis
        JLabel headingLabel = new JLabel("Se connecter en tant que :");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; // occupe 2 colonnes
        loginPanel.add(headingLabel, gbc);
        gbc.gridwidth = 1; // réinitialisation pour les autres composants

        // Sélection du type de compte
        JLabel loginTypeLabel = new JLabel("Type de compte :");
        rbClientLogin = new JRadioButton("Client");
        rbAdminLogin = new JRadioButton("Administrateur");
        rbClientLogin.setSelected(true);
        bgLoginType = new ButtonGroup();
        bgLoginType.add(rbClientLogin);
        bgLoginType.add(rbAdminLogin);
        JPanel loginTypePanel = new JPanel();
        loginTypePanel.add(rbClientLogin);
        loginTypePanel.add(rbAdminLogin);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(loginTypeLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(loginTypePanel, gbc);

        // Champ Email
        JLabel emailLabel = new JLabel("Email :");
        loginEmailField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(loginEmailField, gbc);

        // Champ Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe :");
        loginPasswordField = new JPasswordField(20);
        gbc.gridx = 0; gbc.gridy = 3;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(loginPasswordField, gbc);

        // Bouton Connexion
        loginButton = new JButton("Se connecter");
        loginButton.addActionListener(e -> doLogin());
        gbc.gridx = 1; gbc.gridy = 4;
        loginPanel.add(loginButton, gbc);

        // ------------------------
        // Onglet Inscription
        // ------------------------
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;

        // Sélection du type de compte pour l'inscription
        JLabel regTypeLabel = new JLabel("Type de compte :");
        rbClientRegister = new JRadioButton("Client");
        rbAdminRegister = new JRadioButton("Admin");
        rbClientRegister.setSelected(true);
        bgRegisterType = new ButtonGroup();
        bgRegisterType.add(rbClientRegister);
        bgRegisterType.add(rbAdminRegister);
        JPanel regTypePanel = new JPanel();
        regTypePanel.add(rbClientRegister);
        regTypePanel.add(rbAdminRegister);

        // Affichage conditionnel du champ "Clé Admin"
        rbAdminRegister.addActionListener(e -> toggleAdminKey(true));
        rbClientRegister.addActionListener(e -> toggleAdminKey(false));

        // Champs d'inscription
        JLabel nameLabel = new JLabel("Nom :");
        regNameField = new JTextField(20);
        JLabel surnameLabel = new JLabel("Prénom :");
        regSurnameField = new JTextField(20);
        JLabel regEmailLabel = new JLabel("Email :");
        regEmailField = new JTextField(20);
        JLabel regPasswordLabel = new JLabel("Mot de passe :");
        regPasswordField = new JPasswordField(20);
        JLabel addressLabel = new JLabel("Adresse :");
        regAddressField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Téléphone :");
        regPhoneField = new JTextField(20);
        adminKeyLabel = new JLabel("Clé Admin :");
        regAdminKeyField = new JTextField(10);
        // Masquer par défaut le champ et le label Clé Admin
        adminKeyLabel.setVisible(false);
        regAdminKeyField.setVisible(false);

        registerButton = new JButton("Créer le compte");
        registerButton.addActionListener(e -> doRegister());

        gbc2.gridx = 0; gbc2.gridy = 0;
        registerPanel.add(regTypeLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regTypePanel, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 1;
        registerPanel.add(nameLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regNameField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 2;
        registerPanel.add(surnameLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regSurnameField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 3;
        registerPanel.add(regEmailLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regEmailField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 4;
        registerPanel.add(regPasswordLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regPasswordField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 5;
        registerPanel.add(addressLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regAddressField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 6;
        registerPanel.add(phoneLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regPhoneField, gbc2);

        gbc2.gridx = 0; gbc2.gridy = 7;
        registerPanel.add(adminKeyLabel, gbc2);
        gbc2.gridx = 1;
        registerPanel.add(regAdminKeyField, gbc2);

        gbc2.gridx = 1; gbc2.gridy = 8;
        registerPanel.add(registerButton, gbc2);

        tabbedPane.addTab("Connexion", loginPanel);
        tabbedPane.addTab("Créer un compte", registerPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    // Méthode pour afficher/masquer le champ de la clé admin lors du changement de type de compte
    private void toggleAdminKey(boolean show) {
        adminKeyLabel.setVisible(show);
        regAdminKeyField.setVisible(show);
        revalidate();
        repaint();
    }

    // Méthode appelée lors de la connexion
    private void doLogin() {
        String email = loginEmailField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        try {
            if (rbClientLogin.isSelected()) {
                Client client = authController.loginClient(email, password);
                if (client != null) {
                    JOptionPane.showMessageDialog(this, "Connexion réussie en tant que client !");
                    // TODO : rediriger vers la vue principale client
                } else {
                    JOptionPane.showMessageDialog(this, "Identifiants incorrects pour client.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Administrateur admin = authController.loginAdmin(email, password);
                if (admin != null) {
                    JOptionPane.showMessageDialog(this, "Connexion réussie en tant qu'administrateur !");
                    // TODO : rediriger vers la vue administrateur
                } else {
                    JOptionPane.showMessageDialog(this, "Identifiants incorrects pour administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode appelée lors de la création d'un compte
    private void doRegister() {
        String name = regNameField.getText().trim();
        String surname = regSurnameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword());
        String address = regAddressField.getText().trim();
        String phone = regPhoneField.getText().trim();
        try {
            if (rbClientRegister.isSelected()) {
                // Création d'un compte client avec type "nouveau"
                Client client = new Client(0, name, surname, email, password, "nouveau", address, phone);
                boolean success = authController.registerClient(client);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Compte client créé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte client.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Vérification de la clé admin avant création
                String adminKey = regAdminKeyField.getText().trim();
                if (!"86".equals(adminKey)) {
                    JOptionPane.showMessageDialog(this, "Clé Admin incorrecte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Administrateur admin = new Administrateur(name, surname, email, password);
                boolean success = authController.registerAdmin(admin);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Compte administrateur créé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            // Obtention d'une connexion réelle à la base de données via DBConnection
            Connection conn = DBConnection.getConnection();
            AuthController authController = new AuthController(conn);
            SwingUtilities.invokeLater(() -> {
                AuthView view = new AuthView(authController);
                view.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
