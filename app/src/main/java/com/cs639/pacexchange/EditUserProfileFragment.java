package com.cs639.pacexchange;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class EditUserProfileFragment extends Fragment {

    CircularImageView mProfilePicture;
    ImageButton mSaveChanges, mDiscardChanges;
    FloatingActionButton changeProfilePhotoButton;
    EditText mName, mEmail, mGradYear, mPhoneNumber;

    StorageReference mStorageRef;
    FirebaseStorage mFirebaseStorage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //CollectionReference ref = db.collection("users").document(user.getUid()).collection("t");

    public EditUserProfileFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
        //Initialize UI elements
        mSaveChanges = view.findViewById(R.id.save_changes_button);
        mDiscardChanges = view.findViewById(R.id.discard_changes_button);
        changeProfilePhotoButton = view.findViewById(R.id.floatingActionButton);
        mProfilePicture = view.findViewById(R.id.imageview_account_profile);
        mName = view.findViewById(R.id.edit_change_name);
        mEmail = view.findViewById(R.id.edit_change_email);
        mGradYear = view.findViewById(R.id.edit_change_grad_year);
        mPhoneNumber = view.findViewById(R.id.edit_change_phone_number);
        //Initialize firebase user and storage reference
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = mAuth.getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        //Set display name and email values to UI
        mName.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());

        //Set path to write user profile image
        mStorageRef = mStorageRef.child("Images").child("profile pictures");

        mStorageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(mProfilePicture));
        //Set user profile grad year and phone #
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                mGradYear.setText(document.get("year").toString());
                mPhoneNumber.setText(document.getString("phone"));
            }
        });

        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addButtonClickListeners();

    }

    private void addButtonClickListeners() {
        mPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        mSaveChanges.setOnClickListener(v -> {
            updateUserInfo();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment()).commit();

        }); //Open popup menu on floating action button click with options for profile image
        mDiscardChanges.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment()).commit());
        changeProfilePhotoButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.profile_photo, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.open_camera:
                        if (ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2000);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivity(intent);
                        }
                        break;
                    case R.id.open_gallery:
                        if (ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                        } else {
                            startGallery();
                        }
                        break;
                    case R.id.remove_photo:
                        StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl("gs://pacexchange-1527c.appspot.com/Images/profile pictures/" + user.getUid());
                        photoRef.delete();
                        mProfilePicture.setImageResource(R.drawable.user_placeholder);
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    private void updateUserInfo() {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(mName.getText().toString()).build();
        user.updateProfile(profileUpdates);

        user.updateEmail(mEmail.getText().toString());

        //Create firestore doc for user data
        Map<String, Object> userUpdate = new HashMap<>();
        //Save user fields to created doc
        userUpdate.put("name", mName.getText().toString());
        userUpdate.put("email", mEmail.getText().toString());
        userUpdate.put("phone", mPhoneNumber.getText().toString());
        userUpdate.put("year", Integer.parseInt(mGradYear.getText().toString()));
        db.collection("users").document(user.getUid()).update(userUpdate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                //Firebase storage for uploaded image
                StorageReference filePath = mStorageRef.child(user.getUid());
                filePath.putFile(returnUri).addOnFailureListener(e -> Toast.makeText(getContext(),"Upload Failed",Toast.LENGTH_LONG).show());

                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mProfilePicture.setImageBitmap(bitmapImage);
            }
        }
    }
}
