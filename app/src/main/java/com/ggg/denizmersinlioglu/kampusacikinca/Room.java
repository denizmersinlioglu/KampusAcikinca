package com.ggg.denizmersinlioglu.kampusacikinca;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by DenizMersinlioglu on 11/08/2017.
 */

public class Room {

    private User roomAuthor;
    private String roomName;
    private ArrayList<User> roomParticipantList;
    private int maximumParticipantNumber;
    private long maximumTotalAmount;
    private boolean isPrivateRoom;
    private int remainingTime;

    public Room() {
    }

    public Room(String roomName, User roomAuthor, int maximumParticipantNumber, long maximumTotalAmount, boolean isPrivateRoom, int remainingTime) {
        roomParticipantList = new ArrayList<User>();
        this.roomName = roomName;
        this.roomAuthor = roomAuthor;
        this.maximumParticipantNumber = maximumParticipantNumber;
        this.maximumTotalAmount = maximumTotalAmount;
        this.isPrivateRoom = isPrivateRoom;
        this.remainingTime = remainingTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getMaximumParticipantNumber() {
        return maximumParticipantNumber;
    }

    public void setMaximumParticipantNumber(int maximumParticipantNumber) {
        this.maximumParticipantNumber = maximumParticipantNumber;
    }

    public long getMaximumTotalAmount() {
        return maximumTotalAmount;
    }

    public void setMaximumTotalAmount(long maximumTotalAmount) {
        this.maximumTotalAmount = maximumTotalAmount;
    }

    public boolean isPrivateRoom() {
        return isPrivateRoom;
    }

    public void setPrivateRoom(boolean privateRoom) {
        isPrivateRoom = privateRoom;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public ArrayList<User> getRoomParticipantList() {
        return roomParticipantList;
    }

    public void setRoomParticipantList(ArrayList<User> roomParticipantList) {
        this.roomParticipantList = roomParticipantList;
    }

    public User getRoomAuthor() {
        return roomAuthor;
    }

    public void setRoomAuthor(User roomAuthor) {
        this.roomAuthor = roomAuthor;
    }
}
