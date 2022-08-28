package com.projektgruppe11.tv2.Domain;


import com.projektgruppe11.tv2.Persistence.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Crediting system.
 */
public class CreditingSystem {

    private CreditRepository creditRepository;
    private ProductionRepository productionRepository;
    private UserRepository userRepository;
    private IDAOXML idaoxml;
    private Production production;

    /**
     * Instantiates a new Crediting system.
     */

    public CreditingSystem(){
        creditRepository = CreditDatabaseRepository.getInstance();
        productionRepository = ProductionDatabaseRepository.getInstance();
        userRepository = UserDatabaseRepository.getInstance();
        idaoxml = DAOXML.getInstance();

        //Save
        /*try {
            fileManager.saveProductions(productions);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*Production production = new Production("Test Production");
        Person person = new Person("Kristian Have");
        Person person2 = new Person("Hans Hansen");

        Credit credit1 = new Credit(person, "Producer-in");
        Credit credit2 = new Credit(person2, "co-producer");

        production.addCredit(credit1);
        production.addCredit(credit2);

        Production production1 = new Production("Test Production Number 2");
        Person person3 = new Person("Jens Jensen");
        Credit credit3 = new Credit(person3, "Stand-in");
        production1.addCredit(credit3);

        Production production2 = new Production("Test Production Number 3");
        productionRepository.saveProductions(production, production1, production2);
        creditRepository.saveCredits(credit1, credit2, credit3);*/

       /* Production production = new Production("Test Production");
        Person person = new Person(new Credit("Kristian Have", "Producer"));
        Person person2 = new Person(new Credit("Hans Hansen", "co-producer"));
        production.addPerson(person);
        production.addPerson(person2);

        Production production1 = new Production("Test Production Number 2");
        Person person3 = new Person(new Credit("Jens Jensen", "Stand-in"));
        production1.addPerson(person3);

        Production production2 = new Production("Test Production Number 3");
        productionRepository.saveProductions(production, production1, production2);
        personRepository.savePersons(person, person2, person3);

        User admin = new User("admin", "password", User.Role.ADMIN);
        User producer = new User("producer", "password", User.Role.PRODUCER);
        User user = new User("user", "password", User.Role.USER);
        userRepository.saveUsers(admin, producer, user);*/

    }

    public Map<UUID, Production> getProductions(){
        return productionRepository.getProductions();
    }

    public User authenticate(String username, String password) {
        return userRepository.authenticate(username, password);
    }

    public List<Credit> getCreditsFromProduction(UUID production_id){
        return creditRepository.getCreditsFromProduction(production_id);
    }

    public Map<String, Production> getProductionsfromXML() {
        return idaoxml.getProductionsfromXML();
    }

    public void saveXMLtoDatabase() {

        HashMap<UUID, Production> productionsForPDR = new HashMap<>();

        for (Map.Entry<String, Production> entry : getProductionsfromXML().entrySet()) {
            boolean checkextId = idaoxml.getDAOXMLDatabase().getProductions().containsKey(entry.getKey());
            if (!checkextId) {
                production = new Production(entry.getValue().getExt_id(), entry.getValue().getUuid(),
                        entry.getValue().getDescription(), entry.getValue().getName(), entry.getValue().getEpisodeNumber(),
                        entry.getValue().getProductionYear(), entry.getValue().getProducedBy(), entry.getValue().getCast());

                saveProductiontoDaoxmlDATABASE(production);
                productionsForPDR.put(entry.getValue().getUuid(), production);
            }
        }

        if (!productionsForPDR.isEmpty()) {
            for (Map.Entry<UUID, Production> entry : productionsForPDR.entrySet()) {
                production = new Production(entry.getValue().getExt_id(), entry.getValue().getUuid(),
                        entry.getValue().getDescription(), entry.getValue().getName(), entry.getValue().getEpisodeNumber(),
                        entry.getValue().getProductionYear(), entry.getValue().getProducedBy(), entry.getValue().getCast());

                saveProduction(production);
            }
        }

    }

    public void saveToXML(File xml) {
        idaoxml.saveToXML(xml, productionRepository);
    }

    public void updateProduction(Production production) {
        productionRepository.update(production);
    }

    public void saveProduction(Production production) {
        productionRepository.saveProductions(production);
    }

    public void updateCredit(Credit credit){
        creditRepository.update(credit);
    }

    public void saveCredit(Credit credit){
        creditRepository.saveCredits(credit);
    }

    public void saveProductiontoDaoxmlDATABASE(Production production) {
        idaoxml.getDAOXMLDatabase().saveProductions(production);
    }

}

