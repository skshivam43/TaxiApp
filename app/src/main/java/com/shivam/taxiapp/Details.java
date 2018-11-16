package com.shivam.taxiapp;

public class Details {
    private String userID;
    private String name;
    private String email;
    private String type;
    private String phone;
    private String department;
    public Details(){

    }

    public Details(String userID,String name, String email, String type,String phone,String department) {
        this.userID=userID;
        this.name=name;
        this.email=email;
        this.type=type;
        this.phone=phone;
        this.department=department;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getType() {
        return type;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }
}
