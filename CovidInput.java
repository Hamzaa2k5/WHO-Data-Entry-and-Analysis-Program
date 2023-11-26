import java.io.*;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;

public class CovidInput {


    public static int countryCount;

    public static void main(String[] args) {
        System.out.println("Welcome to the WHO Data Entry Program");
        Scanner scan = new Scanner(System.in);

        System.out.print("How many countries' data will you enter: ");
        countryCount = scan.nextInt();
        scan.nextLine(); // Consume the newline character

        String[] countryNames = new String[countryCount];
        String[] countryCodes = new String[countryCount];
        String[] continents = new String[countryCount];
        int[] cases = new int[countryCount];
        int[] deaths = new int[countryCount];

        for (int i = 0; i < countryCount; i++) {
            System.out.println("Enter details for Country #" + (i + 1));

            countryNames[i] = getUserInput("Enter Country #" + (i + 1) + " Name: ", scan);
            countryCodes[i] = getUserInput("Enter Country #" + (i + 1) + " Code: ", scan);
            continents[i] = getValidContinent(scan);
            cases[i] = getPositiveIntegerInput("Enter Country #" + (i + 1) + " Total Detected Cases: ", scan);
            deaths[i] = getPositiveIntegerInput("Enter Country #" + (i + 1) + " Deaths: ", scan);

            while (deaths[i] > cases[i]) {
                System.out.println("# of Deaths is higher than # of cases, try again");
                deaths[i] = getPositiveIntegerInput("Enter Country #" + (i + 1) + " Deaths: ", scan);
            }
        }

        String fileName = askFileName(scan);
        String fullFileName = fileName;

        if (fileName != null) {

            // Display entered data
            System.out.println("Entered Data:");
            for (int i = 0; i < countryCount; i++) {
                System.out.println("Nation " + (i + 1) + " - " +
                        "Name: " + countryNames[i] +
                        ", Code: " + countryCodes[i] +
                        ", Cases: " + cases[i] +
                        ", Deaths: " + deaths[i] +
                        ", Continent: " + continents[i]);
            }

        writeCsv(fullFileName, countryNames, countryCodes, continents, cases, deaths);
        }else {
            System.out.println("Invalid file name. Please enter a non-empty file name.");
        }

        scan.close();
    }



    private static String getValidContinent(Scanner scan) {
        String continent;
        do {
            System.out.print("Continent must be OT, AF, AS, EU, NA, SA, or AU: ");
            continent = scan.nextLine().trim();
        } while (!isContinent(continent));
        return continent;
    }

    public static boolean isContinent(String continent) {
        String[] continents = {"NA", "SA", "EU", "AF", "AU", "AT", "AS", "OT"};
        for (String validContinent : continents) {
            if (continent.equals(validContinent)) {
                return true;
            }
        }
        return false;
    }

    private static String getUserInput(String message, Scanner scan) {
        String userInput;
        do {
            System.out.print(message);
            userInput = scan.nextLine().trim();
        } while (userInput.isEmpty());
        return userInput;
    }

    private static int getPositiveIntegerInput(String message, Scanner scan) {
        int userInput;
        do {
            System.out.print(message);
            while (!scan.hasNextInt()) {
                System.out.println("Please enter a valid integer.");
                scan.next(); // Consume invalid input
            }
            userInput = scan.nextInt();
        } while (userInput <= 0);
        return userInput;
    }

    private static void writeCsv(String fullFileName, String[] countryNames, String[] countryCodes, String[] continents, int[] cases, int[] deaths) {
            try (PrintWriter writer = new PrintWriter(fullFileName)) {

                // Write headers as sample
                writer.println("Country Name, National Code, Detected Cases, Deaths, Continent");

                // Write data for every index
                for (int i = 0; i < countryNames.length; i++) {
                    writer.println(countryNames[i] + "," +
                            countryCodes[i] + "," +
                            cases[i] + "," +
                            deaths[i] + "," +
                            continents[i]);
                }

                System.out.println("Data has been written to " + fullFileName);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    private static String askFileName(Scanner scanner) {
        System.out.print("Enter the CSV file name: ");
        scanner.nextLine();
        String fileName = scanner.nextLine().trim();
        return fileName.isEmpty() ? null : fileName + ".csv" ;
    }
}
