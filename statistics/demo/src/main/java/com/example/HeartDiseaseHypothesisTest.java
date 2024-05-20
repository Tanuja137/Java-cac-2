package com.example;
import org.apache.commons.math3.stat.inference.TTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeartDiseaseHypothesisTest {

    public static void main(String[] args) {
        String csvFile = "heart_disease_uci.csv"; // Path to your dataset file
        String line;
        String csvSplitBy = ",";
        List<Double> ageWithDisease = new ArrayList<>();
        List<Double> ageWithoutDisease = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Assuming the first line is the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                double age = Double.parseDouble(data[0]); // Assuming 'age' is the first column
                int target = Integer.parseInt(data[data.length - 1]); // Assuming 'target' is the last column

                if (target == 1) {
                    ageWithDisease.add(age);
                } else {
                    ageWithoutDisease.add(age);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert List<Double> to double[]
        double[] agesWithDiseaseArray = ageWithDisease.stream().mapToDouble(Double::doubleValue).toArray();
        double[] agesWithoutDiseaseArray = ageWithoutDisease.stream().mapToDouble(Double::doubleValue).toArray();

        // Perform t-test
        TTest tTest = new TTest();
        double pValue = tTest.tTest(agesWithDiseaseArray, agesWithoutDiseaseArray);

        System.out.println("P-value: " + pValue);
        if (pValue < 0.05) {
            System.out.println("There is a significant difference in the mean ages between patients with and without heart disease.");
        } else {
            System.out.println("There is no significant difference in the mean ages between patients with and without heart disease.");
        }
    }
}
