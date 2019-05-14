package com.cs639.pacexchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ItemFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Context mContext;
    public FirestoreRecyclerAdapter<Item, ItemViewHolder> adapter;

    public ItemFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = view.findViewById(R.id.item_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        //Set query, order items
        Query query = FirebaseFirestore.getInstance().collection("items").orderBy("timestamp", Query.Direction.DESCENDING);
        //Set firestore options var
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();
        //Set adapter
        adapter = new FirestoreRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item itemModel) {
                holder.setName(itemModel.getName());
                holder.setDescription(itemModel.getDescription());
                holder.setPrice(itemModel.getPrice());
                //Get document ID
                DocumentSnapshot document = getSnapshots().getSnapshot(position);
                holder.setIcon(document.getId());

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(),ItemTradeDetailsActivity.class);
                    intent.putExtra("name", itemModel.getUserId());
                    intent.putExtra("category", itemModel.getCategory());
                    intent.putExtra("cost", itemModel.getPrice());
                    startActivity(intent);
                });
            }
            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
                return new ItemViewHolder(view);
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;

        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setName(String itemName) {
            TextView textView = view.findViewById(R.id.item_name_view);
            textView.setText(itemName);
        }
        void setDescription(String itemDescription) {
            TextView textView = view.findViewById(R.id.item_description_view);
            textView.setText(itemDescription);
        }
        void setPrice(String itemPrice) {
            TextView textView = view.findViewById(R.id.item_price_view);
            textView.setText(itemPrice);
        }

        void setIcon(String docId) {
            ImageView imageView = view.findViewById(R.id.item_image);
            FirebaseStorage.getInstance().getReference().child("Images").child("items").child(docId).getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.get().load(uri).into(imageView));
        }
    }
}
