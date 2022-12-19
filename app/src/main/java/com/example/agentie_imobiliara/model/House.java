package com.example.agentie_imobiliara.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Objects;

public class House {
    private String Address;
    private String Size;
    private String Rooms;
    private String Baths;
    private String Floors;
    private String Special;
    private String Owner;
    private String PictureName;
    private String Image;
    private String Price;
    private String key;

    public House()
    {

    }

    public House(String address, String size, String rooms, String baths, String floors, String special, String owner,String pictureName, String image, String price) {
        Address = address;
        Size = size;
        Rooms = rooms;
        Baths = baths;
        Floors = floors;
        Special = special;
        Owner = owner;
        PictureName = pictureName;
        Image = image;
        Price = price;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getRooms() {
        return Rooms;
    }

    public void setRooms(String rooms) {
        Rooms = rooms;
    }

    public String getBaths() {
        return Baths;
    }

    public void setBaths(String baths) {
        Baths = baths;
    }

    public String getFloors() {
        return Floors;
    }

    public void setFloors(String floors) {
        Floors = floors;
    }

    public String getSpecial() {
        return Special;
    }

    public void setSpecial(String special) {
        Special = special;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPictureName() {
        return PictureName;
    }

    public void setPictureName(String pictureName) {
        PictureName = pictureName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return Address.equals(house.Address) && Size.equals(house.Size) && Rooms.equals(house.Rooms) && Baths.equals(house.Baths) && Floors.equals(house.Floors) && Special.equals(house.Special) && Owner.equals(house.Owner) && PictureName.equals(house.PictureName) && Image.equals(house.Image) && Price.equals(house.Price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Address, Size, Rooms, Baths, Floors, Special, Owner, PictureName, Image, Price);
    }

    @Override
    public String toString() {
        return "House{" +
                "Address='" + Address + '\'' +
                ", Size='" + Size + '\'' +
                ", Rooms='" + Rooms + '\'' +
                ", Baths='" + Baths + '\'' +
                ", Floors='" + Floors + '\'' +
                ", Special='" + Special + '\'' +
                ", Owner='" + Owner + '\'' +
                ", PictureName='" + PictureName + '\'' +
                ", Image='" + Image + '\'' +
                ", Price='" + Price + '\'' +
                '}';
    }
}
