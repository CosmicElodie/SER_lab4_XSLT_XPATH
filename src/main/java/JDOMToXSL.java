import org.jdom2.Document;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class JDOMToXSL {

    /*public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException
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
    }*/

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xslt = (Document) db.parse("sheet1.xsl");
        Document xml = (Document) db.newDocument();
        xml.appendChild(xml.createElementNS(null, "root"));
        Document result = transformXML(xml, xslt);
        System.out.println(result.getDocumentElement().getTextContent());
        LSSerializer serializer = ((DOMImplementationLS) xml.getImplementation()).createLSSerializer();
        System.out.println(serializer.writeToString((Node) result));
    }

    public static Document transformXML(Document xml, Document xslt) throws TransformerException, ParserConfigurationException, FactoryConfigurationError {

        Source xmlSource = new DOMSource((Node) xml);
        Source xsltSource = new DOMSource((Node) xslt);
        DOMResult result = new DOMResult();

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact
                = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);

        trans.transform(xmlSource, result);

        Document resultDoc = (Document) result.getNode();

        return resultDoc;
    }
}
