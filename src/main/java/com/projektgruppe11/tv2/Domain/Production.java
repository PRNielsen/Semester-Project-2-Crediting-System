package com.projektgruppe11.tv2.Domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Production.
 */
public class Production {

    private UUID uuid;
    private String ext_id;
    private String name;
    private String description;
    private String productionYear;
    private String producedBy;
    private String episodeNumber;
    private String cast;
    private List<Credit> credits;
    private StringProperty nameProperty;
    private StringProperty uuidProperty;

    public Production(String name) {
        credits = new ArrayList<>();
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.nameProperty = new SimpleStringProperty(name);
        this.uuidProperty = new SimpleStringProperty(uuid.toString());
    }

    public Production(String name, UUID uuid) {
        credits = new ArrayList<>();
        this.name = name;
        this.uuid = uuid;
        this.nameProperty = new SimpleStringProperty(name);
        this.uuidProperty = new SimpleStringProperty(uuid.toString());
    }

    public Production(String ext_id, UUID uuid, String description, String name) {
        credits = new ArrayList<>();
        this.name = name;
        this.uuid = uuid;
        this.ext_id = ext_id;
        this.description = description;
        this.nameProperty = new SimpleStringProperty(name);
        this.uuidProperty = new SimpleStringProperty(uuid.toString());
    }

    public Production(String ext_id, UUID uuid, String description, String name, String episodeNumber, String productionYear, String producedBy, String cast) {
        credits = new ArrayList<>();
        this.name = name;
        this.uuid = uuid;
        this.ext_id = ext_id;
        this.description = description;
        this.producedBy = producedBy;
        this.productionYear = productionYear;
        this.episodeNumber = episodeNumber;
        this.cast = cast;
        this.nameProperty = new SimpleStringProperty(name);
        this.uuidProperty = new SimpleStringProperty(uuid.toString());
    }

    public StringProperty nameProperty() {
        return this.nameProperty;
    }

    public void setNameProperty(String name){
        nameProperty.set(name);
    }

    public StringProperty uuidProperty() {
        return this.uuidProperty;
    }

    public void setUuidProperty(UUID uuid){
        uuidProperty.set(uuid.toString());
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getExt_id() {
        return ext_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public String getProducedBy() {
        return producedBy;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public String getCast() {
        return cast;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setExt_id(String ext_id) {
        this.ext_id = ext_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }

    public void setProducedBy(String producedBy) {
        this.producedBy = producedBy;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }

    public void addCredit(Credit credit) {
        credit.setProduction_uuid(uuid);
        credits.add(credit);
    }

    public boolean hasCredits() {
        return credits.size() > 0;
    }

    public void removeCredit(Credit credit) {
        this.credits.remove(credit);
    }


    @Override
    public String toString() {
        return "Production{" +
                "ext_id=" + ext_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productionYear='" + productionYear + '\'' +
                ", producedBy='" + producedBy + '\'' +
                ", episodeNumber='" + episodeNumber + '\'' +
                ", cast='" + cast + '\'' +
                '}';
    }
}
