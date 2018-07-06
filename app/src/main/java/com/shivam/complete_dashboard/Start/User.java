package com.shivam.complete_dashboard.Start;

/**
 * Created by shivam sharma on 2/7/2018.
 */

public class User
{


    private String name;
    private String email;
    private String number;
    private String password;

    public User()
    {
    }


    public User(String name, String email, String number, String password)
    {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password =password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
