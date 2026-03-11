package ru.praktikum.model;

public class Courier {
    private String login;
    private String password;
    private String firstName;
    //private Integer id;

    public Courier() {
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

   // public Integer getId() {
   //     return id;
   // }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

   // public void setId(Integer id) {
  //      this.id = id;
  //  }
}
