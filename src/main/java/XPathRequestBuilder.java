public class XPathRequestBuilder {

    private String continent;
    private String language;
    private int superfMax;
    private int superfMin;

    public XPathRequestBuilder(String continent, String language, int superfMax, int superfMin) {
        this.continent = continent;
        this.language = language;
        this.superfMax = superfMax;
        this.superfMin = superfMin;
    }

    /**
     * La requete construite retourne une liste de noms de continent
     * pour chaque pays
     */
    public String requestForRegion() {

        return "/countries/element[region='" + continent + "']";
    }

    /**
     * La requete construite retourne une liste de noms de langue pour
     * chaque pays
     */
    public String requestForLanguage() {

        return "/countries/element[languages/element/name='" + language + "']";
    }

    /**
     * La requete construite retourne une liste des superficies pour
     * chaque pays
     */
    public String requestForArea() {

        return "/countries/element/area";
    }
}
