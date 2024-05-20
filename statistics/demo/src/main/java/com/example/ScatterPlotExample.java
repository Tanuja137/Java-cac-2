package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScatterPlotExample extends JFrame {

    public ScatterPlotExample() {
        super("Scatter Plot Example");

        // Create datasets
        XYDataset ageCholesterolDataset = createAgeCholesterolDataset();
        XYDataset cholesterolHeartRateDataset = createCholesterolHeartRateDataset();
        XYDataset ageHeartRateDataset = createAgeHeartRateDataset();

        // Create charts
        JFreeChart ageCholesterolChart = ChartFactory.createScatterPlot(
                "Age vs Cholesterol",     // Chart title
                "Age",                    // X-axis label
                "Cholesterol",            // Y-axis label
                ageCholesterolDataset,    // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true,                     // Include legend
                true,                     // Include tooltips
                false                     // Include URLs
        );

        JFreeChart cholesterolHeartRateChart = ChartFactory.createScatterPlot(
                "Cholesterol vs Heart Rate", // Chart title
                "Cholesterol",               // X-axis label
                "Heart Rate",                // Y-axis label
                cholesterolHeartRateDataset,// Dataset
                PlotOrientation.VERTICAL,    // Plot orientation
                true,                        // Include legend
                true,                        // Include tooltips
                false                        // Include URLs
        );

        JFreeChart ageHeartRateChart = ChartFactory.createScatterPlot(
                "Age vs Heart Rate",       // Chart title
                "Age",                     // X-axis label
                "Heart Rate",              // Y-axis label
                ageHeartRateDataset,       // Dataset
                PlotOrientation.VERTICAL,  // Plot orientation
                true,                      // Include legend
                true,                      // Include tooltips
                false                      // Include URLs
        );

        // Create Panels
        ChartPanel ageCholesterolPanel = new ChartPanel(ageCholesterolChart);
        ChartPanel cholesterolHeartRatePanel = new ChartPanel(cholesterolHeartRateChart);
        ChartPanel ageHeartRatePanel = new ChartPanel(ageHeartRateChart);

        // Set layout
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Add Panels to Frame
        getContentPane().add(ageCholesterolPanel);
        getContentPane().add(cholesterolHeartRatePanel);
        getContentPane().add(ageHeartRatePanel);
    }

    private XYDataset createAgeCholesterolDataset() {
        XYSeries series = new XYSeries("Age vs Cholesterol");

        // Read data from CSV and add to the series
        try (BufferedReader br = new BufferedReader(new FileReader("heart_disease_uci.csv"))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip header line
                }

                String[] values = line.split(",");
                int age = Integer.parseInt(values[1]); // Assuming age is in the second column (index 1)
                int cholesterol = Integer.parseInt(values[6]); // Assuming cholesterol is in the seventh column (index 6)
                series.add(age, cholesterol);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private XYDataset createCholesterolHeartRateDataset() {
        XYSeries series = new XYSeries("Cholesterol vs Heart Rate");

        // Read data from CSV and add to the series
        try (BufferedReader br = new BufferedReader(new FileReader("heart_disease_uci.csv"))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip header line
                }

                String[] values = line.split(",");
                int cholesterol = Integer.parseInt(values[6]); // Assuming cholesterol is in the seventh column (index 6)
                int heartRate = Integer.parseInt(values[9]); // Assuming heart rate is in the tenth column (index 9)
                series.add(cholesterol, heartRate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private XYDataset createAgeHeartRateDataset() {
        XYSeries series = new XYSeries("Age vs Heart Rate");

        // Read data from CSV and add to the series
        try (BufferedReader br = new BufferedReader(new FileReader("heart_disease_uci.csv"))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip header line
                }

                String[] values = line.split(",");
                int age = Integer.parseInt(values[1]); // Assuming age is in the second column (index 1)
                int heartRate = Integer.parseInt(values[9]); // Assuming heart rate is in the tenth column (index 9)
                series.add(age, heartRate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample();
            example.setSize(800, 800);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
