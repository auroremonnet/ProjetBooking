// view/ReportingView.java
package view;

import dao.ReportingDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;

public class ReportingView extends JFrame {
    private final ReportingDAO reportingDAO;

    public ReportingView(Connection connection) {
        super("📊 Statistiques – Reporting");
        this.reportingDAO = new ReportingDAO(connection);

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Titre en haut
        JLabel title = new JLabel("Réservations par catégorie et par mois", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Chart empilé au centre
        ChartPanel chartPanel = createChartPanel(createStackedBarChart());
        add(chartPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new Dimension(950, 550));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return panel;
    }

    /**
     * Barres empilées : pour chaque mois, proportion de réservations
     * par catégorie d'hébergement.
     */
    private JFreeChart createStackedBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int year = LocalDate.now().getYear();

        try {
            // Retourne Map<Catégorie, Map<Mois (format "01"→"12"), Count>>
            Map<String, Map<String,Integer>> data =
                    reportingDAO.getReservationCountByCategoryAndMonth(year);

            // Remplissage du dataset empilé
            for (var entryCat : data.entrySet()) {
                String categorie = entryCat.getKey();
                Map<String,Integer> countsByMonth = entryCat.getValue();
                for (var entryMonth : countsByMonth.entrySet()) {
                    String mois = entryMonth.getKey();
                    Integer cnt = entryMonth.getValue();
                    dataset.addValue(cnt, categorie, mois);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur chargement données empilées:\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Création du chart empilé
        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Réservations par catégorie et mois – " + year,
                "Mois",
                "Nombre de réservations",
                dataset,
                PlotOrientation.VERTICAL,
                true,   // légende
                true,   // tooltips
                false   // URLs
        );

        // Personnalisation du plot
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getDomainAxis().setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );

        return chart;
    }
}
