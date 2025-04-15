import javax.swing.SwingUtilities;
import view.ClientLoginView; // Assure-toi que ce package est correct

public class Main {
    public static void main(String[] args) {
        // Exécute l'interface graphique sur le thread de l'Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crée et affiche la vue de connexion initiale
                ClientLoginView loginView = new ClientLoginView();
                loginView.setVisible(true);
            }
        });
    }
}
