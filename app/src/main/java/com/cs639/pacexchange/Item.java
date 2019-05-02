package com.cs639.pacexchange;

public class Item {
    int mPrice;
    String mSeller;
    String mItemType;

    public Item(String seller, String itemType, int price)
    {
        mSeller = seller;
        mItemType = itemType;
        mPrice = price;
    }

    public int getPrice()
    {
        return mPrice;
    }

    public String getSeller()
    {
        return mSeller;
    }

    public String getItemType()
    {
        return mItemType;
    }
}