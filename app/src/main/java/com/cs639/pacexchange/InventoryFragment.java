package com.cs639.pacexchange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class InventoryFragment extends Fragment {

    private Button btnAddItem;
    private FirestoreRecyclerAdapter<Item, InventoryViewHolder> adapter;

    public InventoryFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAddItem.setOnClickListener(v ->
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddItemFragment()).commit());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        btnAddItem = view.findViewById(R.id.add_item);
        RecyclerView recyclerView = view.findViewById(R.id.inventory_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Query query = FirebaseFirestore.getInstance().collection("items")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        adapter = new FirestoreRecyclerAdapter<Item, InventoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull InventoryViewHolder holder, int position, @NonNull Item inventory) {
                holder.setTitle(inventory.getName());
                //TODO: Fix 2 lines below to be responsive
                holder.setCount(1);
                holder.setThumbnail(R.drawable.books);
                holder.itemView.setOnClickListener(v -> {

                });
            }
            @NonNull
            @Override
            public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_inventory, parent, false);
                return new InventoryFragment.InventoryViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_inventory_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    private class InventoryViewHolder extends RecyclerView.ViewHolder {
        View view;

        InventoryViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ImageView overflow = view.findViewById(R.id.overflow);
        }
        void setTitle(String productTitle) {
            TextView title = view.findViewById(R.id.title);
            title.setText(productTitle);
        }
        void setCount(int productCount) {
            TextView count = view.findViewById(R.id.count);
            count.setText(Integer.toString(productCount));
        }
        void setThumbnail(int imgRes) {
            ImageView thumbnail = view.findViewById(R.id.thumbnail);
            thumbnail.setImageResource(imgRes);
        }
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    Toast.makeText(getContext(), "Delete Item", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_edit:
                    Toast.makeText(getContext(), "Edit Item", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_share:
                    Toast.makeText(getContext(), "Share Item", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}
