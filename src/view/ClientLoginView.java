package view;

import javax.swing.*;
import java.awt.*;

public class ClientLoginView extends JFrame {
    // Composants de l'interface
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Constructeur qui initialise la vue
    public ClientLoginView() {
        setTitle("Connexion Client");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre
        initComponents();
    }

    // Initialise et positionne les composants à l'intérieur de la fenêtre
    private void initComponents() {
        // Création des labels et des champs
        JLabel emailLabel = new JLabel("Email :");
        emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Se connecter");

        // Utilisation d'un layout flexible (GridBagLayout)
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Marges autour des composants
        gbc.anchor = GridBagConstraints.WEST;

        // Positionnement du label email
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        // Positionnement du champ email
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Positionnement du label mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        // Positionnement du champ mot de passe
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Positionnement du bouton de connexion
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // Ajout du panneau dans la fenêtre
        add(panel);
    }

    // Méthode main pour lancer et tester la vue
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientLoginView loginView = new ClientLoginView();
            loginView.setVisible(true);
        });
    }
}
