package com.projektgruppe11.tv2.Domain;

import java.util.UUID;

/**
 * The type Person.
 */
public class Person {

    private Long ext_id;
    private String name;
    private String email;
    private UUID uuid;

    /**
     * Instantiates a new Person.
     *
     * @param name the name
     */
    public Person(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public Person(UUID uuid, String name) {
        this.name = name;
        this.uuid = uuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
}
