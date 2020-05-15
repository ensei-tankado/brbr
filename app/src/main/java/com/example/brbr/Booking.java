package com.example.brbr;

public class Booking {
    public String barberID;
    public String userID;
    public String date;
    public String time;
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    Booking()
    {

    }

    Booking(String barberID, String userID, String date, String time)
    {
        this.barberID = barberID;
        this.userID = userID;
        this.date = date;
        this.time = time;
        this.status = "pending";
    }

    public String getBarberID() {
        return barberID;
    }

    public void setBarberID(String barberID) {
        this.barberID = barberID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
