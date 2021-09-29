package com.example.deliverymanagement.Models;

public class DeliveryDetails {

    String id,productName,productPrice, userName, userAddress, userContact, qty;

    public DeliveryDetails() {
    }

    public DeliveryDetails(String id, String productName, String productPrice, String userName, String userAddress, String userContact, String qty) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userContact = userContact;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
