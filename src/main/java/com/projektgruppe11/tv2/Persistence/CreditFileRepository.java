package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Credit;
import com.projektgruppe11.tv2.Domain.Person;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreditFileRepository implements CreditRepository {

    private String FILE_PATH = "persons.json";

    //HashMap.
    private HashMap<UUID, Credit> credits;
    private static CreditFileRepository instance = null;

    public static CreditFileRepository getInstance() {
        if (instance == null) {
            instance = new CreditFileRepository();
        }

        return instance;
    }

    public CreditFileRepository() {
        parseCredits();
    }

    public void parseCredits() {
        credits = new HashMap<>();

        String contents = null;
        try {
            contents = new String((Files.readAllBytes(Paths.get(FILE_PATH))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jo = new JSONArray(contents);

        for (int i = 0; i < jo.length(); i++) {
            JSONObject value = jo.getJSONObject(i);
            String name = value.getString("name");
            String uuid_String = value.getString("id");
            UUID uuid = UUID.fromString(uuid_String);
            String role = value.getString("role");
            String production_uuid = value.getString("production_id");
            Person person = new Person(name);
            Credit credit = new Credit(uuid, person, role, UUID.fromString(production_uuid));
            credits.put(uuid, credit);
        }
    }

    //saves and adds person to json and hashmap
    @Override
    public void saveCredits(Credit... credits) {
        JSONArray ja = new JSONArray();
        for (Credit credit : credits) {
            JSONObject personObject = new JSONObject();
            personObject.put("id", credit.getUuid());
            personObject.put("production_id", credit.getProduction_uuid());
            personObject.put("name", credit.getName());
            personObject.put("role", credit.getRole());
            ja.put(personObject);
        }
        try {
            Files.write(Paths.get(FILE_PATH), ja.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reads person from hashmap
    @Override
    public Credit get(UUID uuid) {
        return credits.get(uuid);
    }

    //update deletes and adds person entities from hashmap and json
    @Override
    public void update(Credit credit) {
        UUID uuid = credit.getUuid();
        credits.put(uuid, credit);
        saveCredits(credits.values().toArray(new Credit[0]));
    }

    //deletes person in hashmap and json
    @Override
    public void delete(UUID uuid) {
        if (credits.get(uuid) != null) {
            credits.remove(uuid);
        }
        saveCredits(credits.values().toArray(new Credit[0]));
    }

    @Override
    public List<Credit> getCreditsFromProduction(UUID productionUUID) {
        List<Credit> credits = new ArrayList<>();

        for (Credit credit : this.credits.values()) {
            if (credit.getProduction_uuid().equals(productionUUID)) {
                credits.add(credit);
            }
        }

        return credits;
    }
}
