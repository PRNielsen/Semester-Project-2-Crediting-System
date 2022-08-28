package com.projektgruppe11.tv2.Domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Credit {

    private Person person;
    private String role;
    private UUID uuid;
    private UUID production_uuid;
    private StringProperty nameProperty;
    private StringProperty roleProperty;


    public Credit(Person person, String role){
        this.uuid = UUID.randomUUID();
        this.person = person;
        this.role = role;
        this.nameProperty = new SimpleStringProperty(person.getName());
        this.roleProperty = new SimpleStringProperty(role);
    }

    public Credit(UUID uuid, Person person, String role){
        this.uuid = uuid;
        this.person = person;
        this.role = role;
        this.nameProperty = new SimpleStringProperty(person.getName());
        this.roleProperty = new SimpleStringProperty(role);
    }

    public Credit(UUID uuid, Person person, String role, UUID production_uuid){
        this.uuid = uuid;
        this.person = person;
        this.role = role;
        this.production_uuid = production_uuid;
        this.nameProperty = new SimpleStringProperty(person.getName());
        this.roleProperty = new SimpleStringProperty(role);
    }

    public void setProduction_uuid(UUID production_uuid) {
        this.production_uuid = production_uuid;
    }

    public UUID getProduction_uuid() {
        return production_uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName(){
        return person.getName();
    }


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StringProperty nameProperty() {
        return this.nameProperty;
    }

    public StringProperty roleProperty() {
        return this.roleProperty;
    }

    public void setNameProperty(String name){
        nameProperty.set(name);
    }

    public void setRoleProperty(String Role){
        roleProperty.set(role);
    }

    @Override
    public String toString() {
        return person + " - " + role;
    }
}
