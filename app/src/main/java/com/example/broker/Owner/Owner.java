package com.example.broker.Owner;

public class Owner {
    private String uid,name,Email,profileImage,age,phoneNumber;

    public  Owner(){

    }

    public Owner(String uid,String name,String Email,String profileImage,String age,String phoneNumber){
        this.uid = uid;
        this.name = name;
        this.Email = Email;
        this.profileImage = profileImage;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return Email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }
}
