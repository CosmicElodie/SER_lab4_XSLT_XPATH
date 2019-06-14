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

	private List<String> regiontest = new ArrayList<>();
	private List<String> langagetest = new ArrayList<>();

	private DocumentBuilderFactory factory;
	private NodeList countries;

	public InterfaceRecherchePays(File xmlFile) throws ParserConfigurationException, SAXException, IOException {


		createXSL.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				super.mouseClicked(e);

				// Création des fichiers XSL selon ce qui est demandé
				String continent = Objects.requireNonNull(continents.getSelectedItem()).toString();
				String langage = Objects.requireNonNull(langages.getSelectedItem()).toString();
				String min = superficieMin.getText();
				String max = superficieMax.getText();

				//Affichage sur le terminal
				ParserXSLToXML parser = new ParserXSLToXML();
				ParserXSLToXML.writeXSLCountry(continent, langage, min, max);
				parser.displayInformations(continent, langage, min, max);
			}
		});

		String[] region = setRegion();
		String[] language = setLanguage();

		continents = new JComboBox<>(region);
		langages = new JComboBox<>(language);

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

		JPanel supMin = new JPanel();
		supMin.add(new JLabel("Superficie minimum"));
		supMin.add(superficieMin);
		panelRecherche.add(supMin);

		JPanel supMax = new JPanel();
		supMax.add(new JLabel("Superficie maximum"));
		supMax.add(superficieMax);
		panelRecherche.add(supMax);

		JPanel createButton = new JPanel();
		createButton.add(createXSL);
		panelRecherche.add(createButton);

		add(panelRecherche, BorderLayout.CENTER);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setTitle("Interface de recherche de pays");
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
							&& !regiontest.contains(country.getElementsByTagName("region").item(0).getTextContent()))
						regiontest.add(country.getElementsByTagName("region").item(0).getTextContent());
				}

				++indexCountries;
			}

		} catch (final ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return regiontest.toArray(new String[0]);
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
									&& !langagetest.contains(((Element) lang.getElementsByTagName("element").item(0))
									.getElementsByTagName("name").item(0).getTextContent()))
							{

								langagetest.add(((Element) lang.getElementsByTagName("element").item(0))
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

		return langagetest.toArray(new String[0]);
	}

	public static void main(String... args) throws IOException, SAXException, ParserConfigurationException
	{
		new InterfaceRecherchePays(new File("src/naviDisplay/countries.xml"));
	}

}
