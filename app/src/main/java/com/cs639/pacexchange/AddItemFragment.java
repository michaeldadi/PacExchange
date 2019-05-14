package com.cs639.pacexchange;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner categorySpinner;
    ImageView mItemImage;
    EditText mName, mDescription, mPrice;
    Button mAddItem;
    String selectedCategory, docKey;

    StorageReference mStorageRef;

    public AddItemFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        categorySpinner.setOnItemSelectedListener(this);
        mAddItem.setOnClickListener(v -> onItemAdded());
        docKey = FirebaseFirestore.getInstance().collection("items").document().getId();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        categorySpinner = view.findViewById(R.id.category_spinner);
        mName = view.findViewById(R.id.edit_item_name);
        mDescription = view.findViewById(R.id.edit_item_description);
        mPrice = view.findViewById(R.id.edit_item_price);
        mAddItem = view.findViewById(R.id.btn_add_item);
        mItemImage = view.findViewById(R.id.item_image);
        registerForContextMenu(mItemImage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);
        //Set path for item images
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Images").child("items");

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_upload_image, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_camera:
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 2000);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }
                return true;
            case R.id.open_gallery:
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                } else {
                    startGallery();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        selectedCategory = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    public void onItemAdded() {
        if (mName.getText().toString().isEmpty())
            Toast.makeText(getContext(), "Please enter a product name", Toast.LENGTH_LONG).show();
        else if (mDescription.getText().toString().isEmpty())
            Toast.makeText(getContext(), "Please enter a product description", Toast.LENGTH_LONG).show();
        else if (mPrice.getText().toString().isEmpty())
            Toast.makeText(getContext(), "Please enter a product price", Toast.LENGTH_LONG).show();
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            //Create firestore doc for user data
            Map<String, Object> items = new HashMap<>();
            //Save user fields to created doc
            items.put("category", selectedCategory);
            items.put("name", mName.getText().toString());
            items.put("description", mDescription.getText().toString());
            items.put("price", NumberFormat.getCurrencyInstance().format(Double.parseDouble(mPrice.getText().toString())));
            items.put("userId", user.getUid());
            items.put("userName", user.getDisplayName());
            items.put("timestamp", Calendar.getInstance().getTime());
            db.collection("items").document(docKey).set(items);

            Map<String, Object> userUpdate = new HashMap<>();
            userUpdate.put("items", FieldValue.increment(1));
            db.collection("users").document(user.getUid()).update(userUpdate);
        }
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super method removed
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                //Firebase storage for uploaded image
                StorageReference filePath = mStorageRef.child(docKey);
                filePath.putFile(returnUri).addOnFailureListener(e -> Toast.makeText(getContext(),"Upload Failed: " + e, Toast.LENGTH_LONG).show());

                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mItemImage.setImageBitmap(bitmapImage);
            }
        }
    }
}
