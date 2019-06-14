import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InterfaceRecherchePays extends JFrame {

	private JComboBox<String> continents;
	private JComboBox<String> langages;
	JButton createXSL = new JButton("Générer XSL");
	private JTextField superficieMin = new JTextField(5);
	private JTextField superficieMax = new JTextField(5);

	private List<String> regions = new ArrayList<>();
	private static String region[];
	private List<String> languages = new ArrayList<>();
	private static String language[];

	private DocumentBuilderFactory factory;
	private NodeList countries;

	public InterfaceRecherchePays(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		createXSL.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{

				super.mouseClicked(e);

				// Création des fichiers XSL selon ce qui est demandé
				String 	continent	= Objects.requireNonNull(continents.getSelectedItem()).toString(),
						langage		= Objects.requireNonNull(langages.getSelectedItem()).toString(),
						min			= superficieMin.getText(),
						max			= superficieMax.getText();

				//Affichage sur le terminal
				ParserXSLToXML parser = new ParserXSLToXML();
				ParserXSLToXML.writeXSLCountry(continent, langage, min, max);
				parser.displayInformationsTerminal(continent, langage, min, max);
			}
		});

		displayPanelList();

		continents	= new JComboBox<>(region);
		langages	= new JComboBox<>(language);

		JPanel panelRecherche = new JPanel(new FlowLayout());
		panelRecherche.setLayout(new BoxLayout(panelRecherche, BoxLayout.PAGE_AXIS));

		JPanel continent = new JPanel();
		continent.add(new JLabel("Choix d'un continent"));
		continent.add(continents);
		panelRecherche.add(continent);

		JPanel langage = new JPanel();
		langage.add(new JLabel("Choix d'une langue"));
		langage.add(langages);
		panelRecherche.add(langage);

		JPanel minSuperficy = new JPanel();
		minSuperficy.add(new JLabel("Superficie minimum"));
		minSuperficy.add(this.superficieMin);
		panelRecherche.add(minSuperficy);

		JPanel maxSuperficy = new JPanel();
		maxSuperficy.add(new JLabel("Superficie maximum"));
		maxSuperficy.add(this.superficieMax);
		panelRecherche.add(maxSuperficy);

		JPanel button = new JPanel();
		button.add(createXSL);
		panelRecherche.add(button);

		add(panelRecherche, BorderLayout.CENTER);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setTitle("Interface de recherche de pays");
	}

	public void displayPanelList() throws ParserConfigurationException, SAXException, IOException {
		region = setRegion();
		region[0] = "";
		language = setLanguage();
		language [0] = "";
	}

	private NodeList getNodeList(DocumentBuilderFactory factory) throws ParserConfigurationException, SAXException, IOException
	{
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document input = builder.parse(new File("src/naviDisplay/countries.xml"));
		final Element inputCountries = input.getDocumentElement();
		return inputCountries.getElementsByTagName("element");
	}


	private String[] setRegion()
	{
		factory = DocumentBuilderFactory.newInstance();
		try {
			countries = getNodeList(factory);
			short elementNode = Node.ELEMENT_NODE;
			int countriesLength = countries.getLength(),
				indexCountries = 0;

			while(indexCountries < countriesLength)
			{
				if (countries.item(indexCountries).getNodeType() == elementNode) {
					final Element country = (Element) countries.item(indexCountries);

					if (country.getElementsByTagName("region").item(0) != null
							&& !regions.contains(country.getElementsByTagName("region").item(0).getTextContent()))
						regions.add(country.getElementsByTagName("region").item(0).getTextContent());
				}

				++indexCountries;
			}

		} catch (final ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return regions.toArray(new String[0]);
	}

	private String[] setLanguage() throws IOException, SAXException, ParserConfigurationException {

		factory = DocumentBuilderFactory.newInstance();
		countries = getNodeList(factory);

		try {
			int 	indexCountry 	= 0,
					indexLanguage,
					countriesSize 	= countries.getLength();
			short 	elementNode 	= Node.ELEMENT_NODE;

			while(indexCountry < countriesSize)
			{
				indexLanguage = 0;
				if (countries.item(indexCountry).getNodeType() == elementNode)
				{
					final Element country = (Element) countries.item(indexCountry);
					NodeList languages = country.getElementsByTagName("languages");
					int langLength = languages.getLength();

					while(indexLanguage < langLength)
					{
						if (elementNode == languages.item(indexLanguage).getNodeType())
						{
							Element lang = (Element) languages.item(indexLanguage);
							if (lang.getElementsByTagName("element").item(0) != null
									&& !this.languages.contains(((Element) lang.getElementsByTagName("element").item(0))
									.getElementsByTagName("name").item(0).getTextContent()))
							{

								this.languages.add(((Element) lang.getElementsByTagName("element").item(0))
										.getElementsByTagName("name").item(0).getTextContent());
							}
						}
						++indexLanguage;
					}
				}

				++indexCountry;
			}

		} catch (DOMException e) {
			e.printStackTrace();
		}

		return languages.toArray(new String[0]);
	}

	public static void main(String... args) throws IOException, SAXException, ParserConfigurationException
	{
		new InterfaceRecherchePays(new File("src/naviDisplay/countries.xml"));
	}

}
