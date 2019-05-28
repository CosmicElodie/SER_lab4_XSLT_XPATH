import org.jdom2.Document;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class JDOM2ToXSL {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
    {
        //  read the XML to a JDOM2 document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder dombuilder = factory.newDocumentBuilder();

        org.w3c.dom.Document w3cDocument = dombuilder.parse("src/main/java/countries.xml");
        DOMBuilder jdomBuilder = new DOMBuilder();
        Document jdomDocument = jdomBuilder.build(w3cDocument);

        // create the JDOMSource from JDOM2 document
        JDOMSource source = new JDOMSource(jdomDocument);

        // create the transformer
        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource("countries.xsl"));

        // create the JDOMResult object
        JDOMResult out = new JDOMResult();

        // perform the transformation
        transformer.transform(source, out);

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        System.out.println(outputter.outputString(out.getDocument()));
    }
}
