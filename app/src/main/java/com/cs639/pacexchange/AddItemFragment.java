package com.cs639.pacexchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner categorySpinner;
    EditText mName, mDescription, mPrice;
    Button mUploadImage;
    String selectedCategory;

    public AddItemFragment() {
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        categorySpinner.setOnItemSelectedListener(this);
        mUploadImage.setOnClickListener(v -> onItemAdded());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        categorySpinner = view.findViewById(R.id.category_spinner);
        mName = view.findViewById(R.id.edit_item_name);
        mDescription = view.findViewById(R.id.edit_item_description);
        mPrice = view.findViewById(R.id.edit_item_price);
        mUploadImage = view.findViewById(R.id.btn_upload_item_image);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        selectedCategory = parent.getItemAtPosition(pos).toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onItemAdded() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Create firestore doc for user data
        Map<String, Object> items = new HashMap<>();
        //Save user fields to created doc
        items.put("category", selectedCategory);
        items.put("name", mName.getText().toString());
        items.put("description", mDescription.getText().toString());
        items.put("price", Integer.parseInt(mPrice.getText().toString()));
        items.put("userId", user.getUid());
        items.put("timestamp", Calendar.getInstance().getTime());
        db.collection("items").add(items);

        Map<String, Object> userUpdate = new HashMap<>();

        userUpdate.put("items", FieldValue.increment(1));
        db.collection("users").document(user.getUid()).update(userUpdate);
    }
}
