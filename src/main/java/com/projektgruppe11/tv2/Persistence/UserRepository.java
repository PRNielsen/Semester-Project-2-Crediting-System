package com.projektgruppe11.tv2.Persistence;

import com.projektgruppe11.tv2.Domain.User;

import java.util.UUID;

public interface UserRepository {

    //CRUD methods
    void saveUsers(User... users);

    User get(UUID uuid);

    void update(User user);

    void delete(UUID uuid);

    User authenticate(String username, String password);
}

