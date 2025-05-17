package org.whilmarbitoco.database.repository;

import org.whilmarbitoco.core.database.Repository;
import org.whilmarbitoco.database.model.Token;

public class TokenRepository extends Repository<Token> {
    public TokenRepository() {
        super(Token.class);
    }
}
