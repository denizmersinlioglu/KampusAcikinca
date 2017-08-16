package com.ggg.denizmersinlioglu.kampusacikinca;

import android.location.Location;

/**
 * Created by DenizMersinlioglu on 01/08/2017.
 */

public class User {
    //<--------------  Decleration zone --------------->
    private String email;
    private String userName;
    private String userPass;
    private User[] friendList;
    private long userRating;
    private Location location;
    //<------------------------------------------------>


    public User() {
    }

    public User(String email, String userName, String userPass) {
        this.email = email;
        this.userName = userName;
        this.userPass = userPass;
    }

    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }


    public User[] getFrindList() {
        return friendList;
    }

    public void setFrindList(User[] friendList) {
        this.friendList = friendList;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public long getUserRating() {
        return userRating;
    }

    public void setUserRating(long userRating) {
        this.userRating = userRating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
