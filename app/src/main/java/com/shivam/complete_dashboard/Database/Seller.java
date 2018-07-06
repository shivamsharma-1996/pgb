package com.shivam.complete_dashboard.Database;

/**
 * Created by shivam sharma on 8/17/2017.
 */

public class Seller {

    private String name;
    private String email;
    private String number;
    private String password;


    public Seller(String name, String email, String number, String password) {
         setName(name);
         setEmail(email);
         setNumber(number);
         setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String contact) {
        this.number = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
