/**
 * Laboratoire  : n°4 - XSLT - XPATH
 * Élèves       : Dutu Launay Marion, Lagier Elodie, Stalder Nicodème
 * Date         : 16.06.2019
 */

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
	private JComboBox<String>	continents;
	private JComboBox<String>	langages;
			JButton 			createXSL = new JButton("Générer XSL");
	private JTextField 			superficieMin = new JTextField(5);
	private JTextField 			superficieMax = new JTextField(5);

	private List<String> 		continentsList = new ArrayList<>();
	private static String 		continentsArray[];
	private List<String> 		languagesList = new ArrayList<>();
	private static String 		languageArray[];

	private DocumentBuilderFactory 	factory;
	private NodeList 				countries;

	public InterfaceRecherchePays(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		createXSL.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);

				String 	continent	= Objects.requireNonNull(continents.getSelectedItem()).toString(),
						langage		= Objects.requireNonNull(langages.getSelectedItem()).toString(),
						min			= superficieMin.getText(),
						max			= superficieMax.getText();

				ParserXSLToXML parser = new ParserXSLToXML();
				ParserXSLToXML.writeXSLCountry(continent, langage, min, max);
				parser.displayInformationsTerminal(continent, langage, min, max);
			}
		});

		setPanelList();

		continents	= new JComboBox<>(continentsArray);
		langages	= new JComboBox<>(languageArray);

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

	public void setPanelList() throws ParserConfigurationException, SAXException, IOException {
		setContinent();
		setLanguage();
	}

	public NodeList getNodeList(DocumentBuilderFactory factory)
			throws ParserConfigurationException, SAXException, IOException
	{
		final DocumentBuilder builder	= this.factory.newDocumentBuilder();
		final Document input 			= builder.parse(new File("src/naviDisplay/countries.xml"));
		final Element inputCountries 	= input.getDocumentElement();

		return inputCountries.getElementsByTagName("element");
	}
	public void setLanguage() throws IOException, SAXException, ParserConfigurationException {

		factory = DocumentBuilderFactory.newInstance();
		countries = getNodeList(factory);

		try {
			int 	indexCountry 	= 0,
					indexLanguage	= 0,
					countriesSize 	= countries.getLength();
			short 	elementNode 	= Node.ELEMENT_NODE;

			if(indexLanguage == 0)
			{
				languagesList.add("");
			}

			while(indexCountry < countriesSize)
			{
				indexLanguage = 0;
				if (countries.item(indexCountry).getNodeType() == elementNode)
				{
					final Element country 	= (Element) countries.item(indexCountry);
					NodeList languages 		= country.getElementsByTagName("languages");
					int langLength 			= languages.getLength();

					while(indexLanguage < langLength)
					{

						boolean isNodeSimilar = (elementNode == languages.item(indexLanguage).getNodeType());

						if (isNodeSimilar)
						{
							Element language_element = (Element) languages.item(indexLanguage);
							boolean isLanguageElementNotNull 	=
									(language_element.getElementsByTagName("element")
											.item(0) != null),
									isLanguageNameNotNull 					=
											!languagesList.contains((
													(Element)language_element.getElementsByTagName("element")
															.item(0))
													.getElementsByTagName("name")
													.item(0).getTextContent());

							if (isLanguageElementNotNull && isLanguageNameNotNull)
							{
								languagesList.add(((Element) language_element.getElementsByTagName("element").item(0))
										.getElementsByTagName("name").item(0).getTextContent());
							}
						}
						++indexLanguage;
					}
				}
				++indexCountry;
			}
			languageArray = languagesList.toArray(new String[0]);

		} catch (DOMException e) {
			e.printStackTrace();
		}
	}

	public void setContinent()
	{
		final String regionString = "region";
		factory = DocumentBuilderFactory.newInstance();

		try {
			countries = getNodeList(factory);
			Element country;
			short elementNode = Node.ELEMENT_NODE;
			int countriesLength = countries.getLength(),
					indexCountries = 0,
					firstItem = 0;

			while(indexCountries < countriesLength)
			{
				if(indexCountries == 0)
				{
					continentsList.add("");
				}
				else if (countries.item(indexCountries).getNodeType() == elementNode)
				{
					country = (Element) countries.item(indexCountries);
					boolean isElementNotNull = (country.getElementsByTagName(regionString).item(firstItem) != null);

					if (isElementNotNull && (!continentsList
									.contains(country.getElementsByTagName(regionString)
									.item(firstItem)
									.getTextContent())))
						continentsList.add(country.getElementsByTagName(regionString).item(firstItem).getTextContent());
				}
				++indexCountries;
			}
			continentsArray = continentsList.toArray(new String[0]);

		} catch (final ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String... args) throws IOException, SAXException, ParserConfigurationException
	{
		new InterfaceRecherchePays(new File("src/naviDisplay/countries.xml"));
	}

}
