/*
 * WHO Data Analysis - Program Two
 * Author: [Hamza Ahmad]
 * Date: [26/11/23]
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * The ProgramTwo class represents a program for analyzing Covid-19 data from a CSV file.
 * It includes methods for reading data, performing analyses, and displaying results.
 */

public class ProgramTwo {

    /**
     * The main method where the program execution starts.
     *
     * @param args Command-line arguments (not used in this program).
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file name (include .csv extension)");
        String csvFilePath = scanner.nextLine();

        // Read data from CSV and create an array of CovidData
        CovidData[] covidDataArray = readCsv(csvFilePath);

        if (covidDataArray != null && covidDataArray.length > 0) {
            //  Menu for different analyses
            System.out.println("Analysis Mode: \n1 for Overall\n2 for Specific Continent");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 2) {
                System.out.println("Enter Continent Code (EU, AF, AS, NA, SA, AU, OT):");
                String continent = scanner.nextLine();
                CovidData[] filteredData = filterByContinent(covidDataArray, continent);
                performAnalysis(filteredData);
            } else {
                performAnalysis(covidDataArray);
            }
        } else {
            System.out.println("No data found or error reading the CSV file.");
        }
    }

    // Mehtod to read data from CSV and return array of covidData
    private static CovidData[] readCsv(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String header = reader.readLine(); // Read and ignore the header line

            // Initialize an array to store CovidData objects
            CovidData[] covidDataArray = new CovidData[countLinesInFile(fileName) - 1]; // Subtract 1 for the header

            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String countryName = data[0].trim();
                    String countryCode = data[1].trim();
                    int cases = Integer.parseInt(data[2].trim());
                    int deaths = Integer.parseInt(data[3].trim());
                    String continent = data[4].trim();

                    covidDataArray[i] = new CovidData(countryName, countryCode, continent, cases, deaths);
                    i++;
                } else {
                    System.err.println("Skipping invalid data: " + line);
                }
            }

            return covidDataArray;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to count number of lines in file
    private static int countLinesInFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int count = 0;
            while (reader.readLine() != null) {
                count++;
            }
            return count;
        }
    }


    // Method to calculate death rate
    private static float calculateDeathRate(CovidData covidData) {
        if (covidData.getCases() > 0) {
            return (float) covidData.getDeaths() / covidData.getCases() * 100;
        } else {
            return 0;
        }
    }

    // Mehtod to sort the CovidData in decending order
    private static void sortCovidDataArrayDescending(CovidData[] covidDataArray, float[] deathRates) {
        int n = covidDataArray.length;
        boolean swapFlag;

        for (int i = 0; i < n - 1; i++) {
            swapFlag = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (deathRates[j] < deathRates[j + 1]) {
                    // Swap death rates
                    float tempDeathRate = deathRates[j];
                    deathRates[j] = deathRates[j + 1];
                    deathRates[j + 1] = tempDeathRate;

                    // Swap CovidData objects
                    CovidData tempData = covidDataArray[j];
                    covidDataArray[j] = covidDataArray[j + 1];
                    covidDataArray[j + 1] = tempData;

                    swapFlag = true;
                }
            }

            // If no two elements were swapFlag in inner loop, the array is already sorted
            if (!swapFlag) {
                break;
            }
        }
    }


    // Method for filtering CovidData array by continent
    private static CovidData[] filterByContinent(CovidData[] covidDataArray, String continent) {
        // Count how many entries are from the specified continent
        int count = 0;
        for (CovidData data : covidDataArray) {
            if (data.getContinent().equalsIgnoreCase(continent)) {
                count++;
            }
        }

        // Create a new array to store filtered data
        CovidData[] filteredArray = new CovidData[count];
        int index = 0;
        for (CovidData data : covidDataArray) {
            if (data.getContinent().equalsIgnoreCase(continent)) {
                filteredArray[index] = data;
                index++;
            }
        }

        return filteredArray;
    }

    // Method for sorting CovidData in descending order based on total cases
    private static void sortByTotalCases(CovidData[] covidDataArray) {
        // Bubble sort algorithm
        int n = covidDataArray.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (covidDataArray[j].getCases() < covidDataArray[j + 1].getCases()) {
                    // Swap CovidData objects
                    CovidData temp = covidDataArray[j];
                    covidDataArray[j] = covidDataArray[j + 1];
                    covidDataArray[j + 1] = temp;
                }
            }
        }
    }

    // Method for sorting array based on total deaths
    private static void sortByTotalDeaths(CovidData[] covidDataArray) {
        int n = covidDataArray.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (covidDataArray[j].getDeaths() < covidDataArray[j + 1].getDeaths()) {
                    // Swap CovidData objects
                    CovidData temp = covidDataArray[j];
                    covidDataArray[j] = covidDataArray[j + 1];
                    covidDataArray[j + 1] = temp;
                }
            }
        }
    }

    // Method to print CovidData Array
    private static void printCovidData(CovidData[] covidDataArray) {
        for (CovidData data : covidDataArray) {
            System.out.println(data.getCountryName() + ", " + data.getCases() + ", " + data.getDeaths());
        }
    }

    // Method to display death rates for countries
    private static void printDeathRates(CovidData[] covidDataArray) {
        System.out.println("\nDeath Rates:");
        for (CovidData covidData : covidDataArray) {
            float deathRate = calculateDeathRate(covidData);
            System.out.println("Country: " + covidData.getCountryName() + ", Death Rate: " + String.format("%.2f", deathRate) + "%");
        }
    }


    // Method for performing analysis on data
    private static void performAnalysis(CovidData[] covidDataArray) {
        if (covidDataArray.length == 0) {
            System.out.println("No data available for analysis.");
            return;
        }

        printDeathRates(covidDataArray);

        // Sorting array by total cases and show the result
        sortByTotalCases(covidDataArray);
        System.out.println("Sorted by Total Cases:");
        printCovidData(covidDataArray);

        sortByTotalDeaths(covidDataArray);
        System.out.println("Sorted by Total Deaths:");
        printCovidData(covidDataArray);

        // Calculating death rates for array
        float[] deathRates = new float[covidDataArray.length];
        for (int i = 0; i < covidDataArray.length; i++) {
            deathRates[i] = calculateDeathRate(covidDataArray[i]);
        }

        sortCovidDataArrayDescending(covidDataArray, deathRates);

        System.out.println("Country with the highest death rate: " + covidDataArray[0].getCountryName());
    }

}

