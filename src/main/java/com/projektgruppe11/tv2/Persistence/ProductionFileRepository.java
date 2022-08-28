package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.Production;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public class ProductionFileRepository implements ProductionRepository {

    //Hashmap
    private String FILE_PATH = "productions.json";
    private HashMap<UUID, Production> productions;

    private static ProductionFileRepository instance = null;

    public static ProductionFileRepository getInstance() {
        if (instance == null) {
            instance = new ProductionFileRepository();
        }

        return instance;
    }

    private ProductionFileRepository() {
        parseProductions();
    }

    public void parseProductions() {
        productions = new HashMap<>();

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
            Production production = new Production(name, uuid);
            productions.put(uuid, production);
        }
    }

    //saves and adds productions to json and hashmap
    @Override
    public void saveProductions(Production... productions) {
        JSONArray ja = new JSONArray();

        for (Production production : productions) {
            JSONObject jo = new JSONObject();
            jo.put("id", production.getUuid().toString());
            jo.put("name", production.getName());
            ja.put(jo);
        }
        try {
            Files.write(Paths.get(FILE_PATH), ja.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reads productions from hashmap
    @Override
    public Production get(UUID uuid) {
        return productions.get(uuid);
    }

    //update deletes and adds production entities from hashmap and json
    @Override
    public void update(Production newProduction) {
        productions.put(newProduction.getUuid(), newProduction);
        saveProductions(productions.values().toArray(new Production[0]));
    }

    //deletes production in hashmap and json
    @Override
    public void delete(UUID uuid) {
        if (productions.get(uuid) != null) {
            productions.remove(uuid);
        }
        saveProductions(productions.values().toArray(new Production[0]));
    }


    @Override
    public HashMap<UUID, Production> getProductions() {
        return productions;
    }

    @Override
    public String toString() {
        return "ProductionFileRepository{" +
                '}';
    }
}
