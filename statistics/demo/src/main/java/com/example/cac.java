


import java.io.*;
import java.util.*;

public class cac {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        // Path to the dataset file
        String datasetFilePath = "heart_disease_uci.csv";

        // Initialize lists to store data
        List<String> cleanedData = new ArrayList<>();
        List<Integer> ages = new ArrayList<>();
        List<Integer> cholesterols = new ArrayList<>();
        List<Integer> heartRates = new ArrayList<>();
        List<Integer> missingValueIndices = new ArrayList<>();

        // Initialize maps to store counts and sums
        Map<String, Integer> valueCounts = new HashMap<>();
        Map<Integer, Integer> ageCholesterolSum = new HashMap<>();
        Map<Integer, Integer> ageCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(datasetFilePath))) {
            String line;
            boolean headerSkipped = false;
            int index = 1; // Keep track of the row index
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header line
                }

                String[] values = line.split(","); // Split the row into values


                // Check if the value in column 14 is empty
                if (values[13].trim().isEmpty()) {
                    // Increment count for the value in column 14
                    missingValueIndices.add(index);
                    index++;
                    continue; // Skip this row for now, handle it later
                }

                // Add the value to the count map
                String value = values[13].trim();
                valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);

                // Add the row to cleaned data
                cleanedData.add(line);

                // Parse age, cholesterol, and heart rate
                int age = Integer.parseInt(values[1]);
                int cholesterol = Integer.parseInt(values[6]);
                int heartRate = Integer.parseInt(values[9]);

                // Add to respective lists
                ages.add(age);
                cholesterols.add(cholesterol);
                heartRates.add(heartRate);


                // Determine the age group (e.g., group every 10 years)
                int ageGroup = age / 10 * 10;

                // Update the sum of cholesterol and count for the corresponding age group
                ageCholesterolSum.put(ageGroup, ageCholesterolSum.getOrDefault(ageGroup, 0) + cholesterol);
                ageCount.put(ageGroup, ageCount.getOrDefault(ageGroup, 0) + 1);

                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find the mode (most frequent value) for column 14
        String mode = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : valueCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mode = entry.getKey();
            }
        }

        // Replace missing values in column 14 with the mode
        for (int i = 0; i < cleanedData.size(); i++) {
            String[] values = cleanedData.get(i).split(",");
            if (values[13].trim().isEmpty()) {
                values[13] = mode;
                cleanedData.set(i, String.join(",", values));
            }
        }

        // Sort ageCholesterolSum map by keys
        Map<Integer, Integer> sortedAgeCholesterolSum = new TreeMap<>(ageCholesterolSum);

        // Calculate descriptive statistics
        double ageMean = calculateMean(ages);
        double cholesterolMean = calculateMean(cholesterols);
        double heartRateMean = calculateMean(heartRates);

        double ageMedian = calculateMedian(ages);
        double cholesterolMedian = calculateMedian(cholesterols);
        double heartRateMedian = calculateMedian(heartRates);

        double ageStdDev = calculateStandardDeviation(ages);
        double cholesterolStdDev = calculateStandardDeviation(cholesterols);
        double heartRateStdDev = calculateStandardDeviation(heartRates);

        double ageIQR = calculateIQR(ages);
        int ageRange = calculateRange(ages);
        int ageMin = Collections.min(ages);
        int ageMax = Collections.max(ages);
        double ageCoefficientOfVariation = calculateCoefficientOfVariation(ages);
        double ageSkewness = calculateSkewness(ages, ageMean, ageStdDev);

        double cholesterolIQR = calculateIQR(cholesterols);
        int cholesterolRange = calculateRange(cholesterols);
        int cholesterolMin = Collections.min(cholesterols);
        int cholesterolMax = Collections.max(cholesterols);
        double cholesterolCoefficientOfVariation = calculateCoefficientOfVariation(cholesterols);
        double cholesterolSkewness = calculateSkewness(cholesterols, cholesterolMean, cholesterolStdDev);

        double heartRateIQR = calculateIQR(heartRates);
        int heartRateRange = calculateRange(heartRates);
        int heartRateMin = Collections.min(heartRates);
        int heartRateMax = Collections.max(heartRates);
        double heartRateCoefficientOfVariation = calculateCoefficientOfVariation(heartRates);
        double heartRateSkewness = calculateSkewness(heartRates, heartRateMean, heartRateStdDev);

        double[] ageQuartiles = calculateQuartiles(ages);
        double[] cholesterolQuartiles = calculateQuartiles(cholesterols);
        double[] heartRateQuartiles = calculateQuartiles(heartRates);

        double[] agePercentiles = calculatePercentiles(ages);
        double[] cholesterolPercentiles = calculatePercentiles(cholesterols);
        double[] heartRatePercentiles = calculatePercentiles(heartRates);

        double[] ageDeciles = calculateDeciles(ages);
        double[] cholesterolDeciles = calculateDeciles(cholesterols);
        double[] heartRateDeciles = calculateDeciles(heartRates);

        // Calculate correlation coefficients
        double ageCholesterolCorrelation = calculateCorrelation(ages, cholesterols);
        double ageHeartRateCorrelation = calculateCorrelation(ages, heartRates);
        double cholesterolHeartRateCorrelation = calculateCorrelation(cholesterols, heartRates);

        // Print cleaned data
        System.out.println(ANSI_PURPLE + "Cleaned Data:" + ANSI_RESET);
        for (String data : cleanedData) {
            System.out.println(data);
        }

        System.out.println("----------------------------------------------------------------------------------------------------");

        // Print missing values and their row indices
        System.out.println(ANSI_PURPLE+"Missing values:"+ANSI_RESET);
        System.out.println("Missing values in column 14 at row indices: " + missingValueIndices);

        System.out.println("----------------------------------------------------------------------------------------------------");

        
        // Print the counts of null values for each column
        System.out.println(ANSI_PURPLE + "Null Value Counts for Each Column:" + ANSI_RESET);
        for (int i = 0; i < 15; i++) { // Assuming there are 15 columns
            int nullCount = countNullValues(cleanedData, i);
            System.out.println("Column " + (i + 1) + ": " + nullCount);
        }

        System.out.println("----------------------------------------------------------------------------------------------------");

        // Print descriptive statistics
        System.out.println(ANSI_PURPLE + "Descriptive Statistics:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "---------------------" + ANSI_RESET);

        System.out.println(ANSI_CYAN +"-----AGE-----"+ANSI_RESET);
        System.out.println("Mean:" + ageMean);
        System.out.println("Median:" + ageMedian);
        System.out.println("Standard Deviation:" + ageStdDev);
        System.out.println("IQR:" + ageIQR);
        System.out.println("Range:" + ageRange);
        System.out.println("Min:" + ageMin);
        System.out.println("Max:" + ageMax);
        System.out.println("Coefficient of Variation:" + ageCoefficientOfVariation);
        System.out.println("Skewness:" + ageSkewness);
        System.out.println("----------------------------------------------------------------------------------------------------");

        System.out.println(ANSI_CYAN +"-----CHOLESTEROL-----"+ANSI_RESET);
        System.out.println("Mean:" + cholesterolMean);
        System.out.println("Median:" + cholesterolMedian);
        System.out.println("Standard Deviation:" + cholesterolStdDev);
        System.out.println("IQR:" + cholesterolIQR);
        System.out.println("Range:" + cholesterolRange);
        System.out.println("Min:" + cholesterolMin);
        System.out.println("Max:" + cholesterolMax);
        System.out.println("Coefficient of Variation:" + cholesterolCoefficientOfVariation);
        System.out.println("Skewness:" + cholesterolSkewness);
        System.out.println("----------------------------------------------------------------------------------------------------");

        System.out.println(ANSI_CYAN+"-----HEART RATE-----"+ANSI_RESET);
        System.out.println("Mean:" + heartRateMean);
        System.out.println("Median:" + heartRateMedian);
        System.out.println("Standard Deviation:" + heartRateStdDev);
        System.out.println("IQR:" + heartRateIQR);
        System.out.println("Range:" + heartRateRange);
        System.out.println("Min:" + heartRateMin);
        System.out.println("Max:" + heartRateMax);
        System.out.println("Coefficient of Variation:" + heartRateCoefficientOfVariation);
        System.out.println("Skewness:" + heartRateSkewness);
        System.out.println("----------------------------------------------------------------------------------------------------");
        
        // Print quartiles in tabular format with color
        System.out.println(ANSI_GREEN + "Quartiles:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Category" + ANSI_RESET + "      | " + ANSI_YELLOW + "Q1" + ANSI_RESET + "      | " + ANSI_YELLOW+ "Median" + ANSI_RESET + "  | " + ANSI_YELLOW + "Q3" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "------------------------------------" + ANSI_RESET);
        printQuartile("Age", ageQuartiles);
        printQuartile("Cholesterol", cholesterolQuartiles);
        printQuartile("Heart Rate", heartRateQuartiles);

        System.out.println(".........................................................................................................................................................................." );

        System.out.println(ANSI_GREEN +"\nPercentiles:" + ANSI_RESET);
        // Print percentiles for age, cholesterol, and heart rate
        printArray("Age", agePercentiles);
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------" );
        printArray("Cholesterol", cholesterolPercentiles);
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------" );
        printArray("Heart Rate", heartRatePercentiles);


        System.out.println("........................................................................................................................................................................." );

        System.out.println(ANSI_GREEN + "\nDeciles:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Category" + ANSI_RESET + "     | " + ANSI_YELLOW + "10th" + ANSI_RESET + "   | " +
                ANSI_YELLOW + "20th" + ANSI_RESET + "    | " + ANSI_YELLOW + "30th" + ANSI_RESET + "    | " + ANSI_YELLOW + "40th" + ANSI_RESET +
                "   | " + ANSI_YELLOW + "50th" + ANSI_RESET + "   | " + ANSI_YELLOW + "60th" + ANSI_RESET + "   | " + ANSI_YELLOW + "70th" + ANSI_RESET +
                "   | " + ANSI_YELLOW + "80th" + ANSI_RESET + "   | " + ANSI_YELLOW + "90th" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "----------------------------------------------------------------------------------------------------" + ANSI_RESET);
        printDeciles("Age", ageDeciles);
        printDeciles("Cholesterol", cholesterolDeciles);
        printDeciles("Heart Rate", heartRateDeciles);

        System.out.println("....................................................................................................." );
        

        

        // Print average cholesterol level by age group
        System.out.println(ANSI_PURPLE + "\nAverage Cholesterol Level by Age Group:" + ANSI_RESET);

        for (Map.Entry<Integer, Integer> entry : sortedAgeCholesterolSum.entrySet()) {
            int ageGroup = entry.getKey();
            int sumCholesterol = entry.getValue();
            int count = ageCount.get(ageGroup);
            double averageCholesterol = (double) sumCholesterol / count;
            System.out.println("Age Group: " + ageGroup + "-" + (ageGroup + 9) + ", Average Cholesterol: " + averageCholesterol);
        }

        System.out.println(".....................................................................................................");

        // Print correlation matrix
        printCorrelationMatrix(ageCholesterolCorrelation, ageHeartRateCorrelation, cholesterolHeartRateCorrelation);
    }

    // Function to count null values in a specific column
    private static int countNullValues(List<String> data, int columnIndex) {
        int count = 0;
        for (String row : data) {
            String[] values = row.split(",");
            if (values[columnIndex].trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    // Function to calculate mean
    private static double calculateMean(List<Integer> data) {
        int sum = 0;
        for (int value : data) {
            sum += value;
        }
        return (double) sum / data.size();
    }

    // Function to calculate median
    private static double calculateMedian(List<Integer> data) {
        Collections.sort(data);
        int n = data.size();
        if (n % 2 == 0) {
            return (data.get(n / 2 - 1) + data.get(n / 2)) / 2.0;
        } else {
            return data.get(n / 2);
        }
    }

    // Function to calculate standard deviation
    private static double calculateStandardDeviation(List<Integer> data) {
        double mean = calculateMean(data);
        double sum = 0;
        for (int value : data) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / data.size());
    }

    // Function to calculate interquartile range (IQR)
    private static double calculateIQR(List<Integer> data) {
        Collections.sort(data);
        int n = data.size();
        double Q1 = calculateMedian(data.subList(0, n / 2));
        double Q3 = calculateMedian(data.subList((n + 1) / 2, n));
        return Q3 - Q1;
    }

    // Function to calculate range
    private static int calculateRange(List<Integer> data) {
        return Collections.max(data) - Collections.min(data);
    }

    // Function to calculate coefficient of variation
    private static double calculateCoefficientOfVariation(List<Integer> data) {
        return calculateStandardDeviation(data) / calculateMean(data);
    }

    // Function to calculate skewness
    private static double calculateSkewness(List<Integer> data, double mean, double stdDev) {
        double sum = 0;
        for (int value : data) {
            sum += Math.pow((value - mean) / stdDev, 3);
        }
        return sum / data.size();
    }

    // Function to calculate quartiles
    private static double[] calculateQuartiles(List<Integer> data) {
        Collections.sort(data);
        int n = data.size();
        double Q1 = calculateMedian(data.subList(0, n / 2));
        double Q2 = calculateMedian(data);
        double Q3 = calculateMedian(data.subList((n + 1) / 2, n));
        return new double[]{Q1, Q2, Q3};
    }

    // Function to calculate percentiles
    private static double[] calculatePercentiles(List<Integer> data) {
        double[] percentiles = new double[100];
        for (int i = 0; i < 100; i++) {
            percentiles[i] = calculatePercentile(data, i + 1);
        }
        return percentiles;
    }

    private static double calculatePercentile(List<Integer> data, double percentile) {
        Collections.sort(data);
        int n = data.size();
        double rank = (percentile / 100) * (n - 1) + 1;
        int k = (int) Math.floor(rank);
        double d = rank - k;
        if (k == 0) {
            return data.get(0);
        } else if (k == n) {
            return data.get(n - 1);
        } else {
            return data.get(k - 1) + d * (data.get(k) - data.get(k - 1));
        }
    }

    // Function to calculate deciles
    private static double[] calculateDeciles(List<Integer> data) {
        Collections.sort(data);
        int n = data.size();
        double[] deciles = new double[9];
        for (int i = 1; i <= 9; i++) {
            int index = (int) Math.ceil(i * (n + 1) / 10.0) - 1;
            deciles[i - 1] = data.get(index);
        }
        return deciles;
    }

    // Function to calculate correlation coefficient
    private static double calculateCorrelation(List<Integer> x, List<Integer> y) {
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        double sumY2 = 0;
        int n = x.size();
        for (int i = 0; i < n; i++) {
            int xi = x.get(i);
            int yi = y.get(i);
            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumX2 += xi * xi;
            sumY2 += yi * yi;
        }
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        return numerator / denominator;
    }

    private static void printQuartile(String category, double[] quartiles) {
        System.out.printf("%-14s| %-8.2f| %-8.2f| %.2f%n", category, quartiles[0], quartiles[1], quartiles[2]);
    }

    // Function to print array
    private static void printArray(String category, double[] array) {
        System.out.print(ANSI_YELLOW + category + ANSI_RESET + " Percentiles: ");
        for (int i = 0; i < array.length; i++) {
            System.out.printf("%.2f", array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

   // Function to print deciles in tabular format with color
   private static void printDeciles(String category, double[] deciles) {
        System.out.printf("%-14s| ", category);
            for (double decile : deciles) {
                System.out.printf("%-7.2f| ", decile);
            }
        System.out.println();
    }

    // Function to print correlation matrix
    private static void printCorrelationMatrix(double ageCholesterolCorrelation,
    double ageHeartRateCorrelation,
    double cholesterolHeartRateCorrelation) {
        System.out.println(ANSI_PURPLE + "\nCorrelation Matrix:" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Category" + ANSI_RESET + "           | " + ANSI_YELLOW + "Age" + ANSI_RESET + "              | " + ANSI_YELLOW + "Cholesterol" + ANSI_RESET + "       | " + ANSI_YELLOW + "Heart Rate" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "-----------------------------------------------------------------------------" + ANSI_RESET);
        System.out.printf("%-20s%-20.2f%-20.2f%-20.2f%n", "Age", 1.0, ageCholesterolCorrelation, ageHeartRateCorrelation);
        System.out.printf("%-20s%-20.2f%-20.2f%-20.2f%n", "Cholesterol", ageCholesterolCorrelation, 1.0, cholesterolHeartRateCorrelation);
        System.out.printf("%-20s%-20.2f%-20.2f%-20.2f%n", "Heart Rate", ageHeartRateCorrelation, cholesterolHeartRateCorrelation, 1.0);
    }



}


