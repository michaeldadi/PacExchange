package com.cs639.pacexchange;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private List<Inventory> inventoryList = new ArrayList<>();
    private MyInventoryRecyclerViewAdapter mAdapter;
    Button btnAddItem;

    public InventoryFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAddItem.setOnClickListener(v ->
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddItemFragment()).commit());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        btnAddItem = view.findViewById(R.id.add_item);
            RecyclerView recyclerView = view.findViewById(R.id.inventory_list);
            mAdapter = new MyInventoryRecyclerViewAdapter(getContext(), inventoryList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

            prepareAlbums();
        return view;
    }

    private void prepareAlbums() {

        Inventory a = new Inventory("Books", 13, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Tutoring", 8, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Water bottles", 11, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Food", 12, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Honeymoon", 14, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("I Need a Doctor", 1, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Loud", 11, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Legend", 14, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Hello", 11, R.drawable.user_placeholder);
        inventoryList.add(a);

        a = new Inventory("Greatest Hits", 17, R.drawable.user_placeholder);
        inventoryList.add(a);

        mAdapter.notifyDataSetChanged();
    }

}
