package view;

import model.Hebergement;
import controller.AdminController;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.util.List;

public class AdminGererProprieteView extends JFrame {
    private final AdminController controller;
    private final Connection connection;
    private JScrollPane scrollPane;

    // Champs du formulaire
    private JTextField nomField, adresseField, localisationField, prixField,
            categorieField, photoField, optionsField, capaciteField, litsField, idSuppressionField;

    public AdminGererProprieteView(Connection conn) {
        this.connection = conn;
        this.controller = new AdminController(conn);

        setTitle("Admin – Gestion des hébergements");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(122, 194, 199));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(1000, 70));
        JLabel titre = new JLabel("➕➖ Ajouter / Supprimer des Propriétés", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 30));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === CENTRER LES HÉBERGEMENTS ===
        chargerHebergements();

        // === FORMULAIRE EN BAS ===
        add(buildFormPanel(), BorderLayout.SOUTH);

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void chargerHebergements() {
        // retire l'ancien scroll si besoin
        if (scrollPane != null) {
            remove(scrollPane);
        }

        try {
            List<Hebergement> liste = controller.listerHebergements();

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(Color.WHITE);

            for (Hebergement h : liste) {
                JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(227, 227, 227));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    }
                };
                ligne.setOpaque(false);
                ligne.setPreferredSize(new Dimension(950, 100));
                ligne.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // image
                String imagePath = "resources/images/" + h.getPhotos();
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaled));

                // texte d’info
                String info = String.format(
                        "<html><b>ID:</b> %d  |  <b>%s</b>  |  %s  —  %.2f €<br>"
                                + "Catégorie: %s  |  Options: %s  |  Capacité: %d  |  Lits: %d</html>",
                        h.getIdHebergement(),
                        h.getNom(),
                        h.getAdresse(),
                        h.getPrix(),
                        h.getCategorie(),      // appelle bien getCategorie()
                        h.getOptions(),
                        h.getCapaciteMax(),
                        h.getNombreLits()
                );
                JLabel label = new JLabel(info);
                label.setPreferredSize(new Dimension(800, 80));
                label.setForeground(Color.BLACK);

                ligne.add(imageLabel);
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
            JOptionPane.showMessageDialog(this, "Erreur chargement hébergements :\n" + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(new Color(89, 141, 144));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Champs ===
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 4, 5, 5));
        fieldsPanel.setOpaque(false);

        nomField = new JTextField();
        adresseField = new JTextField();
        localisationField = new JTextField();
        prixField = new JTextField();
        categorieField = new JTextField();
        photoField = new JTextField();
        optionsField = new JTextField();
        capaciteField = new JTextField();
        litsField = new JTextField();
        idSuppressionField = new JTextField();

        fieldsPanel.add(createLabel("Nom"));
        fieldsPanel.add(nomField);
        fieldsPanel.add(createLabel("Adresse"));
        fieldsPanel.add(adresseField);
        fieldsPanel.add(createLabel("Localisation"));
        fieldsPanel.add(localisationField);
        fieldsPanel.add(createLabel("Prix (€)"));
        fieldsPanel.add(prixField);
        fieldsPanel.add(createLabel("Catégorie"));
        fieldsPanel.add(categorieField);
        fieldsPanel.add(createLabel("Photo (nom.jpg)"));
        fieldsPanel.add(photoField);
        fieldsPanel.add(createLabel("Options"));
        fieldsPanel.add(optionsField);
        fieldsPanel.add(createLabel("Capacité max"));
        fieldsPanel.add(capaciteField);
        fieldsPanel.add(createLabel("Nombre de lits"));
        fieldsPanel.add(litsField);
        fieldsPanel.add(createLabel("ID à supprimer"));
        fieldsPanel.add(idSuppressionField);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        // === Boutons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);

        JButton retourBtn = createStyledButton(" Retour");
        retourBtn.addActionListener(e -> {
            dispose();
            new AccueilAdminView(null, connection);
        });

        JButton ajouterBtn = createStyledButton(" Ajouter");
        ajouterBtn.addActionListener(e -> ajouterHebergement());

        JButton supprimerBtn = createStyledButton(" Supprimer");
        supprimerBtn.addActionListener(e -> supprimerHebergement());

        JButton refreshBtn = createStyledButton(" Rafraîchir");
        refreshBtn.addActionListener(e -> chargerHebergements());

        buttonPanel.add(retourBtn);
        buttonPanel.add(ajouterBtn);
        buttonPanel.add(supprimerBtn);
        buttonPanel.add(refreshBtn);

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
            @Override
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

    private void ajouterHebergement() {
        try {
            String nom          = nomField.getText();
            String adresse      = adresseField.getText();
            String localisation = localisationField.getText();
            double prix         = Double.parseDouble(prixField.getText());
            String categorie    = categorieField.getText();
            String photo        = photoField.getText();
            String options      = optionsField.getText();
            int capaciteMax     = Integer.parseInt(capaciteField.getText());
            int nombreLits      = Integer.parseInt(litsField.getText());

            Hebergement h = new Hebergement(
                    nom, adresse, localisation,
                    "Description automatique",
                    prix, categorie, photo, options,
                    capaciteMax, nombreLits
            );

            boolean ok = controller.ajouterHebergement(h);
            if (ok) {
                chargerHebergements();
                JOptionPane.showMessageDialog(this, "✅ Hébergement ajouté.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Échec de l'ajout.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerHebergement() {
        try {
            int id = Integer.parseInt(idSuppressionField.getText());
            boolean ok = controller.supprimerHebergement(id);
            if (ok) {
                chargerHebergements();
                JOptionPane.showMessageDialog(this, "✅ Hébergement supprimé.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ ID introuvable.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
