import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ProgramTwo {

    public static void main(String[] args) {
        // Specify the CSV file path
        String csvFilePath = "hola.csv";

        // Read data from CSV and create an array of CovidData objects
        CovidData[] covidDataArray = readCsv("Hola.csv");

        if (covidDataArray != null && covidDataArray.length > 0) {
            // Additional User Interaction for different analyses
            Scanner scanner = new Scanner(System.in);
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

    private static int countLinesInFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int count = 0;
            while (reader.readLine() != null) {
                count++;
            }
            return count;
        }
    }


    private static float calculateDeathRate(CovidData covidData) {
        if (covidData.getCases() > 0) {
            return (float) covidData.getDeaths() / covidData.getCases() * 100;
        } else {
            return 0;
        }
    }


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

    private static void sortByTotalCases(CovidData[] covidDataArray) {
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

    private static void printCovidData(CovidData[] covidDataArray) {
        for (CovidData data : covidDataArray) {
            System.out.println(data.getCountryName() + ", " + data.getCases() + ", " + data.getDeaths());
        }
    }

    private static void printDeathRates(CovidData[] covidDataArray) {
        System.out.println("\nDeath Rates:");
        for (CovidData covidData : covidDataArray) {
            float deathRate = calculateDeathRate(covidData);
            System.out.println("Country: " + covidData.getCountryName() + ", Death Rate: " + String.format("%.2f", deathRate) + "%");
        }
    }


    private static void performAnalysis(CovidData[] covidDataArray) {
        if (covidDataArray.length == 0) {
            System.out.println("No data available for analysis.");
            return;
        }

        printDeathRates(covidDataArray);

        sortByTotalCases(covidDataArray);
        System.out.println("Sorted by Total Cases:");
        printCovidData(covidDataArray);

        sortByTotalDeaths(covidDataArray);
        System.out.println("Sorted by Total Deaths:");
        printCovidData(covidDataArray);

        float[] deathRates = new float[covidDataArray.length];
        for (int i = 0; i < covidDataArray.length; i++) {
            deathRates[i] = calculateDeathRate(covidDataArray[i]);
        }

        sortCovidDataArrayDescending(covidDataArray, deathRates);

        System.out.println("Country with the highest death rate: " + covidDataArray[0].getCountryName());
    }

}

