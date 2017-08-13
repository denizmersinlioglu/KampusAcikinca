package com.ggg.denizmersinlioglu.kampusacikinca;


import android.widget.ImageView;

/**
 * Created by DenizMersinlioglu on 01/08/2017.
 */

public class Deal {

    public Deal() {
    }

    @Override
    public String toString() {
        return "Deal{" +
                "time=" + time +
                ", isPrivateDeal=" + isPrivateDeal +
                ", userName='" + userName + '\'' +
                ", dealName='" + dealName + '\'' +
                ", campusName='" + campusName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    public Deal(int time ,boolean isPrivateDeal, String dealName, String campusName, String restaurantName, String description) {
        this.time = time;
        this.isPrivateDeal = isPrivateDeal;
        this.dealName = dealName;
        this.campusName = campusName;
        this.restaurantName = restaurantName;
        this.description = description;

    }

    private boolean isPrivateDeal;
    private String dealName;
    private String userName;
    private String campusName;
    private String restaurantName;
    private String description;
    private int time;
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userName = user.getUserName();
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isPrivateDeal() {
        return isPrivateDeal;
    }

    public void setPrivateDeal(boolean privateDeal) {
        isPrivateDeal = privateDeal;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


    public String getUserName() {
        return this.userName;
    }
}
