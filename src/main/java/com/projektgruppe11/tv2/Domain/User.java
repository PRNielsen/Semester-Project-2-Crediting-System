package com.projektgruppe11.tv2.Domain;

import java.util.UUID;

public class User {

    //TODO: Encrypt
    private UUID uuid;
    private String username;
    private String password;
    private Role role;


    public User(String username, String password, Role role) {
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(UUID uuid, String username, String password, Role role) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public enum Role {
        ADMIN, PRODUCER, USER, REGDK;
    }
}

