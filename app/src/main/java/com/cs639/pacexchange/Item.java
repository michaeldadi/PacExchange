package com.cs639.pacexchange;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Item {
    float mPrice;
    String mName, mDescription, mCategory, mUid;
    Date mTimestamp;

    public Item() {}

    public Item(String name, String description, String uid, int price) {
        mName = name;
        mDescription = description;
        mUid = uid;
        mPrice = price;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getUserId() {
        return mUid;
    }

    public void setUserId(String mUid) {
        this.mUid = mUid;
    }

    @ServerTimestamp
    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date mTimestamp) {
        this.mTimestamp = mTimestamp;
    }
}