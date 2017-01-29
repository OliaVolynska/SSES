package com.example.olia.test.Entity;


public class User {
    private int idUser;
    private String login;
    private String fullName;
    private String pass;
    private int isAdmin;

    public User(){}

    public User(String login, String fullName, String pass, int isAdmin) {
        this.login = login;
        this.fullName = fullName;
        this.pass = pass;
        this.isAdmin = isAdmin;
    }

    public User(int idUser, String login, String fullName, String pass, int isAdmin) {
        this.idUser = idUser;
        this.login = login;
        this.fullName = fullName;
        this.pass = pass;
        this.isAdmin = isAdmin;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
