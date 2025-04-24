// view/ReportingView.java
package view;

import dao.ReportingDAO;
import model.Administrateur;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;

public class ReportingView extends JFrame {
    private final Administrateur admin;
    private final Connection connection;
    private final ReportingDAO reportingDAO;
    private final DefaultPieDataset pieDataset = new DefaultPieDataset();
    private final JComboBox<String> monthSelector;
    private ChartPanel pieChartPanel; // ← ajout ici

    public ReportingView(Administrateur admin, Connection connection) {
        this.admin = admin;
        this.connection = connection;
        this.reportingDAO = new ReportingDAO(connection);

        setTitle("📊 Statistiques – Reporting");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // === HEADER ===
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(122, 194, 199));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        header.setPreferredSize(new Dimension(1200, 70));
        JLabel titre = new JLabel("Statistiques de Réservation", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setForeground(Color.WHITE);
        header.add(titre, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // === PANE CENTRAL ===
        ChartPanel barChartPanel = createChartPanel(createStackedBarChart());

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        monthSelector = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            monthSelector.addItem(String.format("%02d", m));
        }
        monthSelector.setSelectedItem(String.format("%02d", LocalDate.now().getMonthValue()));
        monthSelector.addActionListener(e -> updatePieChart());

        JFreeChart pieChart = createPieChart((String) monthSelector.getSelectedItem());
        pieChartPanel = new ChartPanel(pieChart);
        rightPanel.add(pieChartPanel, BorderLayout.CENTER);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        topRight.setBackground(Color.WHITE);
        topRight.add(new JLabel("Mois :"));
        topRight.add(monthSelector);
        rightPanel.add(topRight, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0));
        center.add(barChartPanel);
        center.add(rightPanel);
        add(center, BorderLayout.CENTER);

        // === FOOTER ===
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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
            Map<String, Map<String, Integer>> data = reportingDAO.getReservationCountByCategoryAndMonth(year);
            data.forEach((categorie, countsByMonth) -> {
                countsByMonth.forEach((mois, cnt) ->
                        dataset.addValue(cnt, categorie, mois));
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement barres empilées :\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Réservations par catégorie et mois – " + year,
                "Mois", "Nombre",
                dataset, PlotOrientation.VERTICAL,
                true, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getDomainAxis().setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        return chart;
    }

    private JFreeChart createPieChart(String mois) {
        pieDataset.clear();
        try {
            Map<String, Map<String, Integer>> all = reportingDAO.getReservationCountByCategoryAndMonth(LocalDate.now().getYear());
            for (var entry : all.entrySet()) {
                String categorie = entry.getKey();
                Integer cnt = entry.getValue().getOrDefault(mois, 0);
                pieDataset.setValue(categorie, cnt);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement camembert :\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        JFreeChart pie = ChartFactory.createPieChart("Répartition – mois " + mois, pieDataset, true, true, false);
        PiePlot plot = (PiePlot) pie.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        return pie;
    }

    private void updatePieChart() {
        String mois = (String) monthSelector.getSelectedItem();
        pieDataset.clear();
        try {
            Map<String, Map<String, Integer>> all = reportingDAO.getReservationCountByCategoryAndMonth(LocalDate.now().getYear());
            for (var entry : all.entrySet()) {
                String categorie = entry.getKey();
                Integer cnt = entry.getValue().getOrDefault(mois, 0);
                pieDataset.setValue(categorie, cnt);
            }
            JFreeChart updatedChart = ChartFactory.createPieChart(
                    "Répartition – mois " + mois,
                    pieDataset,
                    true, true, false
            );
            updatedChart.getPlot().setBackgroundPaint(Color.WHITE);
            pieChartPanel.setChart(updatedChart); // ← mise à jour dynamique
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur mise à jour camembert :\n" + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
