package com.cs639.pacexchange;

public class DatabaseUser
{
    String mFirstName;
    String mLastName;
    int mGraduationYear;
    int mNumberOfSales = 0;
    String mEmail;

    public DatabaseUser(String firstName, String lastName, int graduationYear, String email)
    {
        mFirstName = firstName;
        mLastName = lastName;
        mGraduationYear = graduationYear;
        mEmail = email;
    }
    public String getFirstName()
    {
        return mFirstName;
    }
    public String getLastName()
    {
        return mLastName;
    }
    public int getGraduationYear()
    {
        return mGraduationYear;
    }
    public String getEmail()
    {
        return mEmail;
    }
    public int getNumberOfSales()
    {
        return mNumberOfSales;
    }
}
