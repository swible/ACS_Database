package ru.swible.pojo;

public class User {
    private Long id;
    private String login;
    private String firstName;
    private String sureName;
    private String lastName;

    public User() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSureName() {
        return sureName;
    }

    public String getLastName() {
        return lastName;
    }
}
