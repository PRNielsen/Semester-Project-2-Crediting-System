package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Production;

import java.io.File;
import java.util.Map;

public interface IDAOXML {

    public void saveToXML(File xml, ProductionRepository creditingSystem);

    public Map<String, Production> readXML(File xml);

    Map<String, Production> getProductionsfromXML();

    public DAOXMLDatabase getDAOXMLDatabase();


}
