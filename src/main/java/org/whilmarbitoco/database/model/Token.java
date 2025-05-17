package org.whilmarbitoco.database.model;

import org.whilmarbitoco.core.database.Column;
import org.whilmarbitoco.core.database.Primary;
import org.whilmarbitoco.core.database.Table;

@Table(name = "tokens")
public class Token {

    @Primary
    @Column(name = "id")
    private int id;

    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private int userID;

    public Token() {}

    public Token(String token, int userID) {
        this.token = token;
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}

