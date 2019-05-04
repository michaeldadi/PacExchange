package com.cs639.pacexchange;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<Item> mListData;
    View.OnClickListener mRowClickListener;

    public MyItemRecyclerViewAdapter(Context context, List<Item> listData, View.OnClickListener rowClickListener)
    {
        mContext = context;
        mListData = listData;
        mRowClickListener = rowClickListener;
    }

    @NonNull
    @Override
    //If no views available inflate one here and find views by id in viewholder constructor
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //View view = View.inflate(mContext, R.layout.listrow, null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_item, viewGroup, false);
        return new ViewHolder(view, mRowClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ViewHolder recyclerViewHolder = viewHolder;

        Item data = getItem(i);

        recyclerViewHolder.mSellerView.setText(data.getSeller());
        recyclerViewHolder.mItemTypeView.setText(data.getItemType());
        recyclerViewHolder.mPriceView.setText(data.getPrice() + "");
        recyclerViewHolder.itemView.setTag(i);
    }

    public Item getItem(int position)
    {
        return mListData.get(position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mItemImage;
        TextView mSellerView, mItemTypeView, mPriceView;

        public ViewHolder(View itemView, View.OnClickListener rowClickListener)
        {
            super(itemView);
            this.itemView.setOnClickListener(rowClickListener);
            mItemImage = itemView.findViewById(R.id.item_image);
            mSellerView = itemView.findViewById(R.id.seller_view);
            mItemTypeView = itemView.findViewById(R.id.item_type_view);
            mPriceView = itemView.findViewById(R.id.price_view);
        }
    }
}
