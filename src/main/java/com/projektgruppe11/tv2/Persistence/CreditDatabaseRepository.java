package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Credit;
import com.projektgruppe11.tv2.Domain.Person;
import org.postgresql.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditDatabaseRepository implements CreditRepository {

    private Connection connection;
    private static CreditDatabaseRepository instance = null;

    public static CreditDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new CreditDatabaseRepository();
        }

        return instance;
    }

    public CreditDatabaseRepository(){
        connect();
    }

    public void connect(){
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gruppe_11", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void saveCredits(Credit... credits) {
        try {
            for(Credit credit : credits) {
                //TODO: One line query instead of 2'
                //Persons insert - Could also insert email and other information
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO persons (id, name) VALUES (?, ?)");
                insertStatement.setString(1, credit.getPerson().getUuid().toString());
                insertStatement.setString(2, credit.getPerson().getName());
                insertStatement.execute();

                //Credit insert
                insertStatement = connection.prepareStatement("INSERT INTO credits (id, role, production_id, persons_id) VALUES(?, ?, ?, ?)");
                insertStatement.setString(1, credit.getUuid().toString());
                insertStatement.setString(2, credit.getRole());
                insertStatement.setString(3, credit.getProduction_uuid().toString());
                insertStatement.setString(4, credit.getPerson().getUuid().toString());
                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Credit get(UUID uuid) {
        try {
            //Get credit information
            PreparedStatement queryStatement = connection.prepareStatement("SELECT c.*, p.name from credits c INNER JOIN persons p on c.persons_id = p.id WHERE c.id = ?");
            queryStatement.setString(1, uuid.toString());
            ResultSet resultSet = queryStatement.executeQuery();
            UUID credit_id = UUID.fromString(resultSet.getString("id"));
            String role = resultSet.getString("role");
            UUID production_id = UUID.fromString(resultSet.getString("production_id"));
            UUID persons_id = UUID.fromString(resultSet.getString("persons_id"));
            String name = resultSet.getString("name");
            Person person = new Person(persons_id, name);

            //Return credit
            return new Credit(credit_id, person, role, production_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Credit credit) {
        try {
            //TODO: One line query instead of 2
            //Update persons
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE persons SET id = ?, name = ? WHERE id = ?");
            updateStatement.setString(1, credit.getPerson().getUuid().toString());
            updateStatement.setString(2, credit.getPerson().getName());
            updateStatement.setString(3, credit.getPerson().getUuid().toString());
            updateStatement.execute();

            //Update credit
            updateStatement = connection.prepareStatement("UPDATE credits SET id = ?, role = ?, production_id = ? WHERE id = ?");
            updateStatement.setString(1, credit.getUuid().toString());
            updateStatement.setString(2, credit.getRole());
            updateStatement.setString(3, credit.getProduction_uuid().toString());
            updateStatement.setString(4, credit.getUuid().toString());
            updateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID uuid) {
        try {
            //TODO: One line query instead of 2
            //Delete credit
            PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM credits WHERE id = ?");
            deleteStatement.setString(1, uuid.toString());
            deleteStatement.execute();

            //Delete persons (TODO: Delete persons_id)
            deleteStatement = connection.prepareStatement("DELETE FROM persons WHERE id = ?");
            deleteStatement.setString(1, uuid.toString());
            deleteStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Credit> getCreditsFromProduction(UUID productionUUID) {
        List<Credit> credits = new ArrayList<>();

        try {
            //Get credits
            PreparedStatement queryStatement = connection.prepareStatement("SELECT c.*, p.name from credits c INNER JOIN persons p on c.persons_id = p.id WHERE c.id = ?");
            queryStatement.setString(1, productionUUID.toString());
            ResultSet resultSet = queryStatement.executeQuery();
            while(resultSet.next()){
                //For all credits
                Person person = new Person(UUID.fromString(resultSet.getString("persons_id")), resultSet.getString("name"));
                Credit credit = new Credit(UUID.fromString(resultSet.getString("id")), person, resultSet.getString("role"), UUID.fromString(resultSet.getString("production_id")));
                credits.add(credit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return credits;
    }
}
