package view;

import controller.AuthController;
import model.Client;
import model.Administrateur;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class AuthDialog extends JDialog {
    private final AuthController authController;
    private boolean succeeded = false;

    // Composants communs
    private JTabbedPane tabbedPane;

    // Champs de connexion
    private JRadioButton rbClientLogin, rbAdminLogin;
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;

    // Champs d'inscription
    private JRadioButton rbClientReg, rbAdminReg;
    private JTextField regNameField, regSurnameField, regEmailField,
            regAddressField, regPhoneField, regAdminKeyField;
    private JPasswordField regPasswordField;

    public AuthDialog(Frame parent, AuthController authController) {
        super(parent, "Authentification", true);
        this.authController = authController;
        initComponents();
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // -- Onglet Connexion --
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(5,5,5,5);
        c1.anchor = GridBagConstraints.WEST;

        // Type de compte
        rbClientLogin = new JRadioButton("Client", true);
        rbAdminLogin  = new JRadioButton("Administrateur");
        ButtonGroup bgLogin = new ButtonGroup();
        bgLogin.add(rbClientLogin);
        bgLogin.add(rbAdminLogin);

        JPanel pLoginType = new JPanel();
        pLoginType.add(rbClientLogin);
        pLoginType.add(rbAdminLogin);

        c1.gridx=0; c1.gridy=0;
        loginPanel.add(new JLabel("Type de compte :"), c1);
        c1.gridx=1;
        loginPanel.add(pLoginType, c1);

        // Email
        c1.gridy=1; c1.gridx=0;
        loginPanel.add(new JLabel("Email :"), c1);
        loginEmailField = new JTextField(20);
        c1.gridx=1;
        loginPanel.add(loginEmailField, c1);

        // Mot de passe
        c1.gridy=2; c1.gridx=0;
        loginPanel.add(new JLabel("Mot de passe :"), c1);
        loginPasswordField = new JPasswordField(20);
        c1.gridx=1;
        loginPanel.add(loginPasswordField, c1);

        // Boutons
        JButton btnLogin = new JButton("Se connecter");
        btnLogin.addActionListener(e -> doLogin());
        JButton btnCancel = new JButton("Quitter");
        btnCancel.addActionListener(e -> {
            succeeded = false;
            dispose();
        });
        JPanel pLoginBtn = new JPanel();
        pLoginBtn.add(btnLogin);
        pLoginBtn.add(btnCancel);

        c1.gridy=3; c1.gridx=0; c1.gridwidth=2; c1.anchor=GridBagConstraints.CENTER;
        loginPanel.add(pLoginBtn, c1);

        // -- Onglet Inscription --
        JPanel regPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5,5,5,5);
        c2.anchor = GridBagConstraints.WEST;
        c2.gridwidth = 1;

        // Type de compte
        rbClientReg = new JRadioButton("Client", true);
        rbAdminReg  = new JRadioButton("Administrateur");
        ButtonGroup bgReg = new ButtonGroup();
        bgReg.add(rbClientReg);
        bgReg.add(rbAdminReg);

        JPanel pRegType = new JPanel();
        pRegType.add(rbClientReg);
        pRegType.add(rbAdminReg);

        c2.gridx=0; c2.gridy=0;
        regPanel.add(new JLabel("Type de compte :"), c2);
        c2.gridx=1;
        regPanel.add(pRegType, c2);

        // Nom / Prénom
        c2.gridy=1; c2.gridx=0; regPanel.add(new JLabel("Nom :"), c2);
        regNameField = new JTextField(20);
        c2.gridx=1; regPanel.add(regNameField, c2);

        c2.gridy=2; c2.gridx=0; regPanel.add(new JLabel("Prénom :"), c2);
        regSurnameField = new JTextField(20);
        c2.gridx=1; regPanel.add(regSurnameField, c2);

        // Email / Mdp
        c2.gridy=3; c2.gridx=0; regPanel.add(new JLabel("Email :"), c2);
        regEmailField = new JTextField(20);
        c2.gridx=1; regPanel.add(regEmailField, c2);

        c2.gridy=4; c2.gridx=0; regPanel.add(new JLabel("Mot de passe :"), c2);
        regPasswordField = new JPasswordField(20);
        c2.gridx=1; regPanel.add(regPasswordField, c2);

        // Adresse / Téléphone
        c2.gridy=5; c2.gridx=0; regPanel.add(new JLabel("Adresse :"), c2);
        regAddressField = new JTextField(20);
        c2.gridx=1; regPanel.add(regAddressField, c2);

        c2.gridy=6; c2.gridx=0; regPanel.add(new JLabel("Téléphone :"), c2);
        regPhoneField = new JTextField(20);
        c2.gridx=1; regPanel.add(regPhoneField, c2);

        // Clé admin (visible si rbAdminReg)
        c2.gridy=7; c2.gridx=0;
        JLabel lblKey = new JLabel("Clé Admin :");
        regAdminKeyField = new JTextField(10);
        lblKey.setVisible(false);
        regAdminKeyField.setVisible(false);
        regPanel.add(lblKey, c2);
        c2.gridx=1; regPanel.add(regAdminKeyField, c2);

        rbAdminReg.addActionListener(e -> {
            lblKey.setVisible(true);
            regAdminKeyField.setVisible(true);
            AuthDialog.this.pack();
        });
        rbClientReg.addActionListener(e -> {
            lblKey.setVisible(false);
            regAdminKeyField.setVisible(false);
            AuthDialog.this.pack();
        });

        // Bouton Inscription
        JButton btnRegister = new JButton("Créer un compte");
        btnRegister.addActionListener(e -> doRegister());
        c2.gridy=8; c2.gridx=0; c2.gridwidth=2; c2.anchor=GridBagConstraints.CENTER;
        regPanel.add(btnRegister, c2);

        // Ajout des onglets
        tabbedPane.add("Connexion", loginPanel);
        tabbedPane.add("Créer un compte", regPanel);

        getContentPane().add(tabbedPane);
    }

    private void doLogin() {
        String email = loginEmailField.getText().trim();
        String pwd   = new String(loginPasswordField.getPassword());
        try {
            if (rbClientLogin.isSelected()) {
                Client c = authController.loginClient(email, pwd);
                if (c == null) throw new RuntimeException("Identifiants client invalides");
            } else {
                Administrateur a = authController.loginAdmin(email, pwd);
                if (a == null) throw new RuntimeException("Identifiants admin invalides");
            }
            succeeded = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void doRegister() {
        String name     = regNameField.getText().trim();
        String surname  = regSurnameField.getText().trim();
        String email    = regEmailField.getText().trim();
        String pwd      = new String(regPasswordField.getPassword());
        String address  = regAddressField.getText().trim();
        String phone    = regPhoneField.getText().trim();
        try {
            if (rbClientReg.isSelected()) {
                Client c = new Client(0, name, surname, email, pwd, "nouveau", address, phone);
                boolean ok = authController.registerClient(c);
                JOptionPane.showMessageDialog(
                        this,
                        ok ? "Compte client créé !" : "Erreur création compte client",
                        ok ? "Succès" : "Erreur",
                        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
            } else {
                String key = regAdminKeyField.getText().trim();
                if (!"86".equals(key))
                    throw new RuntimeException("Clé Admin incorrecte");
                Administrateur a = new Administrateur(name, surname, email, pwd);
                boolean ok = authController.registerAdmin(a);
                JOptionPane.showMessageDialog(
                        this,
                        ok ? "Compte admin créé !" : "Erreur création compte admin",
                        ok ? "Succès" : "Erreur",
                        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /** @return true si l’utilisateur s’est authentifié correctement */
    public boolean isSucceeded() {
        return succeeded;
    }
}
