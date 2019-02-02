package com.bitehunter.bitehunter.Model;

/**
 * Created by Yash on 10/20/2017.
 */

public class Customer {

    private String id;
    private String name;
    private String email;
    private String contact;
    private String password;


    public Customer(String id, String name, String email, String contact, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }
//getters and setters inorder to get and assign user input to the specific functions
    public Customer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
