package com.example.broker.Room;

public class room {
    String roomName;
    String roomAddress;
    int roomBedrooms;
    int roomBathrooms;
    int roomKitchen;
    int roomRent;
    int roomAdvance;
    public room(String RoomName,String RoomAddress,int RoomBedrooms,int RoomBathrooms,int RoomKitchen,int RoomRent,int RoomAdvance){
        this.roomName = RoomName;
        this.roomAddress = RoomAddress;
        this.roomBedrooms = RoomBedrooms;
        this.roomBathrooms = RoomBathrooms;
        this.roomKitchen = RoomKitchen;
        this.roomRent = RoomRent;
        this.roomAdvance = RoomAdvance;
    }
}
