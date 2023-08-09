package com.example.broker.Room;

public class room {
    private String roomName;
    private String roomAddress;
    private String roomBedrooms;
    private String roomBathrooms;
    private String roomKitchen;
    private String roomRent;
    private String roomAdvance;
    private String roomOwner;
    private String imageUrl;

    public room() {

    }

    public room(String RoomName, String RoomAddress, String RoomBedrooms, String RoomBathrooms, String RoomKitchen, String RoomRent, String RoomAdvance, String RoomOwner, String ImageUrl) {
        this.roomName = RoomName;
        this.roomAddress = RoomAddress;
        this.roomBedrooms = RoomBedrooms;
        this.roomBathrooms = RoomBathrooms;
        this.roomKitchen = RoomKitchen;
        this.roomRent = RoomRent;
        this.roomAdvance = RoomAdvance;
        this.roomOwner = RoomOwner;
        this.imageUrl = ImageUrl;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public String getRoomBedrooms() {
        return roomBedrooms;
    }

    public void setRoomBedrooms(String roomBedrooms) {
        this.roomBedrooms = roomBedrooms;
    }

    public String getRoomBathrooms() {
        return roomBathrooms;
    }

    public void setRoomBathrooms(String roomBathrooms) {
        this.roomBathrooms = roomBathrooms;
    }

    public String getRoomKitchen() {
        return roomKitchen;
    }

    public void setRoomKitchen(String roomKitchen) {
        this.roomKitchen = roomKitchen;
    }

    public String getRoomRent() {
        return roomRent;
    }

    public void setRoomRent(String roomRent) {
        this.roomRent = roomRent;
    }

    public String getRoomAdvance() {
        return roomAdvance;
    }

    public void setRoomAdvance(String roomAdvance) {
        this.roomAdvance = roomAdvance;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
