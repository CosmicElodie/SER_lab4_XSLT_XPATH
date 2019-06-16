/**
 * Laboratoire  : n°4 - XSLT - XPATH
 * Élèves       : Dutu Launay Marion, Lagier Elodie, Stalder Nicodème
 * Date         : 16.06.2019
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ParserXSLToXML
{
    final static String emptyString = "";
    private static String XSLCountry;

    static public void writeXSLCountry(String continent, String langage, String superficyMin, String superficyMax) {
        final int firstElement = 0;
        XSLCountry = emptyString;

        if(!continent.equals(emptyString)) {
            XSLCountry = "region = ('" + continent + "')";
        }

        if(!langage.equals(emptyString)) {

            if(!XSLCountry.equals("")) {
                XSLCountry += " and ";
            }

            XSLCountry += "./languages/element[(name = '" + langage + "')]";
        }

        if(!superficyMin.equals(emptyString)) {

            if(!XSLCountry.equals(emptyString)) {
                XSLCountry += " and ";
            }

            XSLCountry += "area >=" + superficyMin;
        }

        if(!superficyMax.equals(emptyString)) {

            if(!XSLCountry.equals(emptyString)) {
                XSLCountry += " and ";
            }

            XSLCountry += "area <=" + superficyMax;
        }

        try {
            DocumentBuilderFactory  docFactory  = DocumentBuilderFactory.newInstance();
            DocumentBuilder         docBuilder  = docFactory.newDocumentBuilder();
            Document                document    = docBuilder.parse("src/naviDisplay/countries.xsl");
            Node                    root        = document.getFirstChild();

            Element root_element    = (Element) root;
            Element template        = (Element)root_element.getElementsByTagName("xsl:template").item(firstElement);
            Element body            = (Element)template.getElementsByTagName("body").item(firstElement);
            Element div             = (Element)body.getElementsByTagName("div").item(firstElement);
            Element foreach         = (Element)div.getElementsByTagName("xsl:for-each").item(firstElement);

            if(XSLCountry.equals("")) {
                XSLCountry = "countries/element";
            }
            else if(!XSLCountry.equals("")) {
                XSLCountry = "countries/element[" + XSLCountry + "]";
            }

            foreach.setAttribute("select", XSLCountry);

            DOMSource source    = new DOMSource(document);
            FileWriter writer   = new FileWriter(new File("src/naviDisplay/countries.xsl"));
            StreamResult result = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void displayInformationsTerminal(String continent, String langage, String minSuperficy, String maxSuperficy)
    {
        String notSpecified = "Not specified.";
        if(continent.equals(""))
        {
            continent       = notSpecified;
        }
        if(langage.equals(""))
        {
            langage         = notSpecified;
        }
        if(minSuperficy.equals(""))
        {
            minSuperficy    = notSpecified;
        }
        if(maxSuperficy.equals(""))
        {
            maxSuperficy    = notSpecified;
        }

        System.out.println("***RECHERCHE***\n" +
                        "Continent\t\t: " + continent + "\n" +
                        "Langue(s)\t\t: " + langage + "\n" +
                        "Superficie min\t: " + minSuperficy + "\n" +
                        "Superficie max\t: " + maxSuperficy + "\n");
    }

}
