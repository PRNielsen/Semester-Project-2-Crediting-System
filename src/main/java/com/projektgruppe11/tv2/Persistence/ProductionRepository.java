package com.projektgruppe11.tv2.Persistence;


import com.projektgruppe11.tv2.Domain.Production;

import java.util.HashMap;
import java.util.UUID;

public interface ProductionRepository {

    //CRUD methods
    void saveProductions(Production... productions);

    Production get(UUID uuid);

    void update(Production production);

    void delete(UUID uuid);

    HashMap<UUID, Production> getProductions();

}

