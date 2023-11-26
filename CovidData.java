public class CovidData {
    private String countryName;
    private String countryCode;
    private String continent;
    private int cases;
    private int deaths;

    // Constructor
    public CovidData(String countryName, String countryCode, String continent, int cases, int deaths) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.continent = continent;
        this.cases = cases;
        this.deaths = deaths;
    }

    // Getters (you can generate these automatically in many IDEs)
    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getContinent() {
        return continent;
    }

    public int getCases() {
        return cases;
    }

    public int getDeaths() {
        return deaths;
    }
}
