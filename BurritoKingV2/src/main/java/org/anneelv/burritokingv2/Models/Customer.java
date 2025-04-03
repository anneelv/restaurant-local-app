package org.anneelv.burritokingv2.Models;

/*
* The Customer class keeps track of current active user's information
* */
public class Customer {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String status;
    private int pointsCollected;

    public Customer(String username, String firstName, String lastName, String status, String password){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.status = status;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstname() {
        return this.firstName;
    }

    public void setFirstname(String firstname){
        this.firstName = firstname;
    }

    public String getLastname() {
        return this.lastName;
    }

    public void setLastname(String lastname){
        this.lastName = lastname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    /*
    * Ensure that only VIP users can get collected points from past orders
    * */
    public int getPointsCollected(){
        if (this.getStatus().equals("VIP")) return this.pointsCollected;
        else return 0;
    }

    public void setPointsCollected(int pointsCollected){
        this.pointsCollected = pointsCollected;
    }
}
