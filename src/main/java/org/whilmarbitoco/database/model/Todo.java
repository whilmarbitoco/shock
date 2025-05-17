package org.whilmarbitoco.database.model;


import org.whilmarbitoco.core.database.Column;
import org.whilmarbitoco.core.database.Primary;
import org.whilmarbitoco.core.database.Table;

@Table(name = "todos")
public class Todo {

    @Primary
    @Column(name = "id")
    private int id;

    @Column(name = "todo")
    private String todo;

    @Column(name = "user_id")
    private int userID;

    public Todo() {}

    public Todo(String todo, int userID) {
        this.todo = todo;
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
