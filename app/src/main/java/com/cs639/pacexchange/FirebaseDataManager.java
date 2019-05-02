package com.cs639.pacexchange;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDataManager
{
    FirebaseAuth authentication;
    FirebaseFirestore dbInstance;
    FirebaseUser user;
    public FirebaseDataManager()
    {
        authentication = FirebaseAuth.getInstance();
        dbInstance = FirebaseFirestore.getInstance();
        user = authentication.getCurrentUser();
    }
    public void addUserToDatabase(DatabaseUser u)
    {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("DatabaseUser", u);
        dbInstance.collection("users").document(user.getUid()).set(userMap);
    }
    public DatabaseUser retrieveUserFromDatabase()
    {
        return null;
    }
    public String getUserDisplayName()
    {
        return user.getDisplayName();
    }
    public String getUserEmail()
    {
        return user.getEmail();
    }
}
