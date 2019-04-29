package com.cs639.pacexchange;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<ListData> mListData;
    View.OnClickListener mRowClickListener;

    public MyItemRecyclerViewAdapter(Context context, List<ListData> listData, View.OnClickListener rowClickListener)
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

        ListData data = getItem(i);
        recyclerViewHolder.mSellerView.setText(data.getSeller());
        recyclerViewHolder.mItemTypeView.setText(data.getItemType());
        recyclerViewHolder.mPriceView.setText(data.getPrice() + "");
        recyclerViewHolder.itemView.setTag(i);
    }

    public ListData getItem(int position)
    {
        return mListData.get(position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mSellerView;
        public TextView mItemTypeView;
        public TextView mPriceView;

        public ViewHolder(View itemView, View.OnClickListener rowClickListener)
        {
            super(itemView);
            this.itemView.setOnClickListener(rowClickListener);
            mSellerView = itemView.findViewById(R.id.seller_view);
            mItemTypeView = itemView.findViewById(R.id.item_type_view);
            mPriceView = itemView.findViewById(R.id.price_view);
        }
    }
}
