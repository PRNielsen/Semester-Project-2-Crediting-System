package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Credit;
import com.projektgruppe11.tv2.Domain.Person;
import com.projektgruppe11.tv2.Domain.Production;
import org.postgresql.Driver;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class ProductionDatabaseRepository implements ProductionRepository {

    private static ProductionDatabaseRepository instance = null;
    private Connection connection;

    public static ProductionDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new ProductionDatabaseRepository();
        }

        return instance;
    }

    private ProductionDatabaseRepository() {
        connect();
    }

    public void connect() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gruppe_11", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    public void saveProductions(Production... productions) {
        try {
            for (Production production : productions) {
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO productions (id, name) VALUES(?, ?)");
                insertStatement.setString(1, production.getUuid().toString());
                insertStatement.setString(2, production.getName());
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Production get(UUID uuid) {
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT p.*, c.id as CreditId, per.name as CreditName, c.role, per.id as PersonId FROM productions p LEFT JOIN credits c on c.production_id = ? LEFT JOIN persons per on c.persons_id = per.id");
            queryStatement.setString(1, uuid.toString());
            ResultSet resultSet = queryStatement.executeQuery();
            String productionName = resultSet.getString("name");
            UUID production_id = UUID.fromString(resultSet.getString("id"));
            Production production = new Production(productionName, production_id);
            while(resultSet.next()){
                String creditName = resultSet.getString("CreditName");
                String role = resultSet.getString("role");
                UUID credit_id = UUID.fromString(resultSet.getString("CreditId"));
                Person person = new Person(creditName);
                production.addCredit(new Credit(credit_id, person, role, production_id));
            }
            return production;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Production production) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE productions SET name = ?, id = ? WHERE id = ?");
            updateStatement.setString(1, production.getName());
            updateStatement.setString(2, production.getUuid().toString());
            updateStatement.setString(3, production.getUuid().toString());
            updateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        try {
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM productions WHERE id = ?");
            deleteStatement.setString(1, uuid.toString());
            deleteStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<UUID, Production> getProductions() {
        HashMap<UUID, Production> productions = new HashMap<>();
        try {
            PreparedStatement queryStatement = connection.prepareStatement("SELECT p.*, c.id as CreditId, per.name as CreditName" + 
                    ", c.role, per.id as PersonId FROM productions p" + 
                    " LEFT JOIN credits c on c.production_id = p.id LEFT JOIN persons per on c.persons_id = per.id");
            ResultSet resultSet = queryStatement.executeQuery();
            //Iterate through all productions
            while(resultSet.next()){
                String productionName = resultSet.getString("name");
                UUID production_id = UUID.fromString(resultSet.getString("id"));

                Production production;
                if(productions.containsKey(production_id)){
                    //If production is already in list, then get production from that list (to not override credits)
                    production = productions.get(production_id);
                }else{
                    //If production is not already in list, then make new production
                    production = new Production(productionName, production_id);
                }

                //has credits
                if(resultSet.getString("CreditId") != null){
                    String creditName = resultSet.getString("CreditName");
                    String role = resultSet.getString("role");
                    UUID credit_id = UUID.fromString(resultSet.getString("CreditId"));
                    UUID person_id = UUID.fromString(resultSet.getString("PersonId"));

                    //Add all credits to the production
                    Person person = new Person(person_id, creditName);
                    production.addCredit(new Credit(credit_id, person, role, production_id));
                }

                //Add them to the list
                productions.put(production_id, production);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productions;
    }

}
