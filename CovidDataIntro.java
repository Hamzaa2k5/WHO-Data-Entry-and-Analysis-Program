import java.util.Scanner;

public class CovidDataIntro {

    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);

            String  countryName, nationalCode, continent;
            int[] detectCases, deathsFromCovid;

            System.out.print("Enter country Name: ");
            countryName = scanner.nextLine();

            System.out.print("Enter National Code: ");
            nationalCode = scanner.nextLine();

            System.out.print("Enter Detected cases of COVID (comma-seperated): ");
            deathsFromCovid = parseCSVInput(scanner.nextLine());

            System.out.print("Enter Continent (EU/AF/AS/NA/SA/AU/OT): ");
            continent = scanner.nextLine();

        }
    private static int[] parseCSVInput(String input) {
        String[] values = input.split(",");
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.parseInt(values[i].trim());
        }
        return result;
    }
}

