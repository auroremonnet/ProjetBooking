// view/ReportingView.java
package view;

import dao.ReportingDAO;
import model.Administrateur;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.sql.Connection;
import java.util.Map;

public class ReportingView extends JFrame {
    private final Administrateur admin;
    private final Connection connection;
    private final ReportingDAO reportingDAO;

    public ReportingView(Administrateur admin, Connection connection) {
        this.admin      = admin;
        this.connection = connection;
        this.reportingDAO = new ReportingDAO(connection);

        setTitle("📊 Statistiques – Reporting");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // on utilise BorderLayout pour header / centre / footer
        setLayout(new BorderLayout(10, 10));

        // === HEADER identique à AccueilAdminView ===
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.
                        KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(122, 194, 199));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        header.setPreferredSize(new Dimension(1000, 70));
        JLabel titre = new JLabel("Statistiques de Réservation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === CHART PANEL au centre ===
        ChartPanel chartPanel = createChartPanel(createStackedBarChart());
        add(chartPanel, BorderLayout.CENTER);

        // === FOOTER avec bouton Retour ===
        JButton retour = new JButton("← Retour");
        retour.setBackground(new Color(122, 194, 199));
        retour.setForeground(Color.WHITE);
        retour.setFont(new Font("Arial", Font.BOLD, 14));
        retour.setFocusPainted(false);
        retour.addActionListener(e -> {
            dispose();
            new AccueilAdminView(admin, connection);
        });
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footer.add(retour);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return panel;
    }

    private JFreeChart createStackedBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int year = LocalDate.now().getYear();

        try {
            Map<String, Map<String,Integer>> data =
                    reportingDAO.getReservationCountByCategoryAndMonth(year);
            for (var catEntry : data.entrySet()) {
                String categorie = catEntry.getKey();
                for (var monthEntry : catEntry.getValue().entrySet()) {
                    dataset.addValue(
                            monthEntry.getValue(),
                            categorie,
                            monthEntry.getKey()
                    );
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erreur chargement données empilées:\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE
            );
        }

        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Réservations par catégorie et mois – " + year,
                "Mois",
                "Nombre de réservations",
                dataset,
                PlotOrientation.VERTICAL,
                true,  // légende
                true,  // tooltips
                false  // URLs
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getDomainAxis().setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        return chart;
    }
}
