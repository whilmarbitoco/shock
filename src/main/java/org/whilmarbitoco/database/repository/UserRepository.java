package org.whilmarbitoco.database.repository;

import org.whilmarbitoco.core.database.Repository;
import org.whilmarbitoco.database.model.User;

// Sample Repository
public class UserRepository extends Repository<User> {
    public UserRepository() {
        super(User.class);
    }

//    Dev Note: Add custom helper method here (e.g findByEmail)
}
