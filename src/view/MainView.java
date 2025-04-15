package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainView extends JFrame {

    // Constructeur de la vue principale
    public MainView() {
        setTitle("Main View - Booking 2025");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centre la fenêtre

        // Appel à la méthode d'initialisation des composants de l'interface
        initComponents();
    }

    // Méthode qui ajoute les composants à la fenêtre
    private void initComponents() {
        JPanel panel = new JPanel();
        JLabel welcomeLabel = new JLabel("Bienvenue sur Booking 2025 !");
        panel.add(welcomeLabel);
        add(panel);
    }

    // Méthode main pour lancer l'application et afficher la vue principale
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
