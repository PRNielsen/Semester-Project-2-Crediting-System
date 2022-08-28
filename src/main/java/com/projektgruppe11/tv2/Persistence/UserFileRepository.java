package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public class UserFileRepository implements UserRepository {

    private String FILE_PATH = "users.json";
    //HashMap
    private HashMap<UUID, User> users;

    private static UserFileRepository instance = null;

    public static UserFileRepository getInstance() {
        if (instance == null) {
            instance = new UserFileRepository();
        }

        return instance;
    }

    public UserFileRepository() {
        parseUsers();
    }

    public void parseUsers() {
        users = new HashMap<>();

        String contents = null;
        try {
            contents = new String((Files.readAllBytes(Paths.get(FILE_PATH))));
        } catch (IOException e) {
            System.out.println("Could not find users file");
        }
        JSONArray jo = new JSONArray(contents);

        for (int i = 0; i < jo.length(); i++) {
            JSONObject value = jo.getJSONObject(i);
            String username = value.getString("username");
            String uuid_String = value.getString("id");
            UUID uuid = UUID.fromString(uuid_String);
            String password = value.getString("password");
            String role = value.getString("role");
            User user = new User(uuid, username, password, User.Role.valueOf(role));
            users.put(uuid, user);
        }
    }


    //saves and adds user to json and hashmap
    @Override
    public void saveUsers(User... users) {
        JSONArray ja = new JSONArray();

        for (User user : users) {
            JSONObject jo = new JSONObject();
            jo.put("id", user.getUuid().toString());
            jo.put("username", user.getUsername());
            jo.put("password", user.getPassword());
            jo.put("role", user.getRole().toString());
            ja.put(jo);
        }
        try {
            Files.write(Paths.get(FILE_PATH), ja.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reads users from hashmap
    public User get(UUID uuid) {
        return users.get(uuid);
    }

    //update deletes and adds user entities from hashmap and json
    @Override
    public void update(User newUser) {
        users.put(newUser.getUuid(), newUser);
        saveUsers(users.values().toArray(new User[0]));
    }

    //deletes user in hashmap and json
    @Override
    public void delete(UUID uuid) {
        if (users.get(uuid) != null) {
            users.remove(uuid);
        }
        saveUsers(users.values().toArray(new User[0]));
    }

    //Returns user if successful. Returns null if not½
    @Override
    public User authenticate(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
