package com.example.esureapp;

public class RFIDUsersFirebase {
    String rfid, name, email, phone_number, input, password, username;

    public RFIDUsersFirebase(String rfid, String name, String phone_number, String email, String password, String username) {
        this.rfid = rfid;
        this.name = name;
        this.phone_number = email;
        this.email = phone_number;
        this.password = password;
        this.username = username;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
