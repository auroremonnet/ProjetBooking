package view;

import controller.BookingController;
import model.Hebergement;
import org.jdatepicker.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AccueilView extends JFrame {

    private final JTextField champDestination = new JTextField();
    private final JSpinner spinnerAdultes = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
    private final JSpinner spinnerEnfants = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
    private final JSpinner spinnerChambres = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JPanel hebergementPanel = new JPanel();
    private JDatePickerImpl dateArriveePicker;
    private JDatePickerImpl dateDepartPicker;

    public AccueilView(Connection conn) {
        setTitle("Booking.ece – Accueil");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Titre ===
        JLabel titre = new JLabel("Trouver un lieu de séjour", JLabel.CENTER);
        titre.setFont(new Font("Serif", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        add(titre, BorderLayout.NORTH);

        // === Cadre central gris clair arrondi ===
        JPanel centrePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#e3e3e3"));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        centrePanel.setOpaque(false);
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));
        centrePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centrePanel.setMaximumSize(new Dimension(500, 300));

        centrePanel.add(creerChamp("Où je vais ?", champDestination));

        dateArriveePicker = creerDatePicker();
        centrePanel.add(creerChamp("Date d’arrivée :", dateArriveePicker));

        dateDepartPicker = creerDatePicker();
        centrePanel.add(creerChamp("Date de départ :", dateDepartPicker));

        JPanel spinners = new JPanel(new GridLayout(1, 6, 10, 10));
        spinners.setOpaque(false);
        spinners.add(new JLabel("Adultes:"));
        spinners.add(spinnerAdultes);
        spinners.add(new JLabel("Enfants:"));
        spinners.add(spinnerEnfants);
        spinners.add(new JLabel("Chambres:"));
        spinners.add(spinnerChambres);

        centrePanel.add(Box.createVerticalStrut(10));
        centrePanel.add(spinners);

        JButton btnRechercher = new JButton("🔍 Rechercher");
        btnRechercher.setAlignmentX(Component.CENTER_ALIGNMENT);
        centrePanel.add(Box.createVerticalStrut(10));
        centrePanel.add(btnRechercher);

        JPanel topContainer = new JPanel();
        topContainer.setBackground(Color.decode("#437a7e"));
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.add(Box.createVerticalStrut(20));
        topContainer.add(centrePanel);
        topContainer.add(Box.createVerticalStrut(20));

        add(topContainer, BorderLayout.NORTH);

        // === Zone des hébergements ===
        hebergementPanel.setBackground(Color.WHITE);
        hebergementPanel.setLayout(new GridLayout(0, 4, 10, 10));

        JScrollPane scrollPane = new JScrollPane(hebergementPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(1200, 400));
        add(scrollPane, BorderLayout.CENTER);

        // === Action sur bouton rechercher ===
        btnRechercher.addActionListener((ActionEvent e) -> {
            try {
                String destination = champDestination.getText().trim();
                Date d1 = (Date) dateArriveePicker.getModel().getValue();
                Date d2 = (Date) dateDepartPicker.getModel().getValue();

                LocalDate arrivee = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate depart = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                int adultes = (int) spinnerAdultes.getValue();
                int enfants = (int) spinnerEnfants.getValue();
                int chambres = (int) spinnerChambres.getValue();

                BookingController controller = new BookingController(conn);
                new MainView(controller, destination, arrivee, depart, adultes, enfants, chambres);
                dispose(); // Fermer cette fenêtre

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la recherche.");
            }
        });

        setVisible(true);
    }

    private JPanel creerChamp(String label, JComponent champ) {
        JPanel bloc = new JPanel();
        bloc.setLayout(new BorderLayout(5, 5));
        bloc.setOpaque(false);
        JLabel lbl = new JLabel(label);
        bloc.add(lbl, BorderLayout.WEST);
        bloc.add(champ, BorderLayout.CENTER);
        bloc.setMaximumSize(new Dimension(500, 30));
        return bloc;
    }

    private JDatePickerImpl creerDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Aujourd’hui");
        p.put("text.month", "Mois");
        p.put("text.year", "Année");
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(panel, new DateComponentFormatter());
    }
}
