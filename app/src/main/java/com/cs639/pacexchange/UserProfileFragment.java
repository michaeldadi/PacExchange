package com.cs639.pacexchange;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {

    private ImageButton mSettingsDropDown;
    private TextView mName, mEmail, mGradYear, mPhoneNumber, mReputation, mItemNumber, mSalesNumber;
    private CircularImageView profileImage;
    private MyInventoryRecyclerViewAdapter inventoryRecyclerViewAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    public UserProfileFragment() {

    }

    @SuppressLint("StringFormatMatches")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        //Initialize UI elements
        mSettingsDropDown = view.findViewById(R.id.edit);
        mName = view.findViewById(R.id.user_name);
        mEmail = view.findViewById(R.id.user_email);
        mGradYear = view.findViewById(R.id.user_grad_year);
        mPhoneNumber = view.findViewById(R.id.user_phone_number);
        profileImage = view.findViewById(R.id.imageview_account_profile);
        mReputation = view.findViewById(R.id.reputation);
        mItemNumber = view.findViewById(R.id.item_count);
        mSalesNumber = view.findViewById(R.id.sales_count);
        inventoryRecyclerViewAdapter = new MyInventoryRecyclerViewAdapter(getContext());
        //Initialize firebase vars
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Set email and name in profile
        mEmail.setText(user.getEmail());
        mName.setText(user.getDisplayName());
//        mItemNumber.setText(inventoryRecyclerViewAdapter.getItemCount());
        //Set path to write user profile image
        mStorageRef = mStorageRef.child("Images").child("profile pictures");
        //Assign user image to image view
        mStorageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));
        //Set user profile grad year and phone #

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                mGradYear.setText(String.format(getString(R.string.grad_year), document.get("year")));
                mPhoneNumber.setText(document.getString("phone"));
                mReputation.setText(document.get("reputation").toString());
                mSalesNumber.setText(document.get("sales").toString());
            }
        });
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Cache data locally
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        addButtonClickListeners();
    }

    private void addButtonClickListeners() {
        mSettingsDropDown.setOnClickListener(v ->
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditUserProfileFragment()).commit());
    }
}
