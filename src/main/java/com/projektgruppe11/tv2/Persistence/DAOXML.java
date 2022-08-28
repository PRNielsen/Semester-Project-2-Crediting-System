package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Production;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DAOXML implements IDAOXML {

    private Production production;
    private UUID uuid;
    private File xmlFile;
    private Map<String, Production> ext_idProductionMap;
    private Document document;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private final DAOXMLDatabase daoxmlDatabase = new DAOXMLDatabase();

    private static DAOXML instance = null;

    public static DAOXML getInstance() {
        if (instance == null) {
            instance = new DAOXML();
        }
        return instance;
    }

    public DAOXML() {
        xmlFile = new File("20200307_TV 2_ZULU_142316.xml");
        this.ext_idProductionMap = readXML(xmlFile);
    }

    @Override
    public void saveToXML(File xml, ProductionRepository productions) {

        // instance of a DocumentBuilderFactory
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // create instance of DOM
            document = documentBuilder.newDocument();

            //possible to first creat element of 'epg' to match tv2 xml.

            // create the root element programlist
            Element root = document.createElement("programlist");
            document.appendChild(root);

            if (productions.getProductions() != null) {
                for (Map.Entry<UUID, Production> entry : productions.getProductions().entrySet()) {
                    Element programElement = document.createElement("program");
                    root.appendChild(programElement);

                    //create attribute for program programElement
                    Attr attrId = document.createAttribute("ext_id");
                    attrId.setValue(String.valueOf(entry.getValue().getExt_id()));
                    programElement.setAttributeNode(attrId);

                    Attr attrTitle = document.createAttribute("title");
                    attrTitle.setValue(entry.getValue().getName());
                    programElement.setAttributeNode(attrTitle);

                    Attr productionyear = document.createAttribute("productionyear");
                    productionyear.setValue(entry.getValue().getProductionYear());
                    programElement.setAttributeNode(productionyear);

                    Attr producedBy = document.createAttribute("produced_by");
                    producedBy.setValue(entry.getValue().getProducedBy());
                    programElement.setAttributeNode(producedBy);

                    Attr extendedcast = document.createAttribute("extendedcast");
                    extendedcast.setValue((entry.getValue().getCast()));
                    programElement.setAttributeNode(extendedcast);

                    Attr description = document.createAttribute("description");
                    description.setValue((entry.getValue().getDescription()));
                    programElement.setAttributeNode(description);

                    //series programElement under program programElement
                    Element seriesElement = document.createElement("series");
                    programElement.appendChild(seriesElement);
                    //create attribute for series programElement
                    Attr attrEpisodenumber = document.createAttribute("episode_number");
                    attrEpisodenumber.setValue(entry.getValue().getEpisodeNumber());

                    //attrEpisodenumber.setValue(production.getEpisodeNumber());
                    seriesElement.setAttributeNode(attrEpisodenumber);
                }
            } else {
                System.out.println("Creditingsystem has no productions");
            }

            //Create the xml file
            //Transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(String.valueOf(xml)));

            transformer.transform(domSource, streamResult);
            System.out.println("Created XML file");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }


    @Override
    public Map<String, Production> readXML(File xml) {
        ext_idProductionMap = new HashMap<>();

        // Make an  instance of the DocumentBuilderFactory
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            document = documentBuilder.parse(new File(String.valueOf(xml)));

            document.getDocumentElement().normalize();

            NodeList listprograms = document.getElementsByTagName("program");

            Element programElement;

            for (int i = 0; i < listprograms.getLength(); i++) {
                programElement = (Element) listprograms.item(i);

                String ext_id = (programElement.getAttribute("ext_id"));
                String name = programElement.getAttribute("title");
                String description = programElement.getAttribute("description");
                String productionYear = programElement.getAttribute("productionyear");
                String producedBy = programElement.getAttribute("produced_by");
                String cast = programElement.getAttribute("extendedcast");


                Element seriesElement = (Element) programElement.getFirstChild().getNextSibling();
                String episodeNumber = seriesElement.getAttribute("episode_number");

                uuid = UUID.randomUUID();

                production = new Production(ext_id, uuid, description, name, episodeNumber, productionYear, producedBy, cast);

                ext_idProductionMap.put(ext_id, production);
            }
            return ext_idProductionMap;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Production> getProductionsfromXML() {
        return ext_idProductionMap;
    }

    @Override
    public DAOXMLDatabase getDAOXMLDatabase() {
        return daoxmlDatabase;
    }
}