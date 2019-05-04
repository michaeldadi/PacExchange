package com.cs639.pacexchange;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyInventoryRecyclerViewAdapter extends RecyclerView.Adapter<MyInventoryRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Inventory> inventoryList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
        }
    }
    public MyInventoryRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public MyInventoryRecyclerViewAdapter(Context mContext, List<Inventory> inventoryList) {
        this.mContext = mContext;
        this.inventoryList = inventoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inventory, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Inventory inventory = inventoryList.get(position);
        holder.title.setText(inventory.getName());
        holder.count.setText(inventory.getNumOfSongs() + " items");

        // loading inventory cover using Glide library
//        Glide.with(mContext).load(inventory.getThumbnail()).into(holder.thumbnail);

//        holder.overflow.setOnClickListener(view -> showPopupMenu(holder.overflow));
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_inventory_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    Toast.makeText(mContext, "Delete Item", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_edit:
                    Toast.makeText(mContext, "Edit Item", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_share:
                    Toast.makeText(mContext, "Share Item", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
}
