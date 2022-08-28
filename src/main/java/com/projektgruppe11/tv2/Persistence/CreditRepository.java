package com.projektgruppe11.tv2.Persistence;


import com.projektgruppe11.tv2.Domain.Credit;
import com.projektgruppe11.tv2.Domain.Person;

import java.util.List;
import java.util.UUID;

public interface CreditRepository {

    //CRUD methods
    void saveCredits(Credit... credits);

    Credit get(UUID uuid);

    void update(Credit person);

    void delete(UUID uuid);

    List<Credit> getCreditsFromProduction(UUID productionUUID);
}

