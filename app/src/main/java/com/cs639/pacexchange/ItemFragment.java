package com.cs639.pacexchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    RecyclerView mRecyclerView;
    Context mContext;
    List<ListData> mListData;
    MyItemRecyclerViewAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    //Load stuff from database here
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListData = new ArrayList<>();
        mListData.add(new ListData("John Doe", "tickets", 5));
        mListData.add(new ListData("Sebastian Dabrowski", "tutoring", 3));
        mListData.add(new ListData("Alexandra Stephens", "meal plan money", 2));
        mListData.add(new ListData("Jacob Oiler", "chair", 7));
        mListData.add(new ListData("Tinna Rider", "charger", 4));
        mListData.add(new ListData("Tim Fargoe", "pencils", 1));
        mListData.add(new ListData("Patryck Steward", "textbook", 5));
        mListData.add(new ListData("Anna White", "cupcakes", 2));
        mListData.add(new ListData("Jaden Williams", "tickets", 4));
        mListData.add(new ListData("Jaden Williams", "tickets", 4));
        mListData.add(new ListData("Jaden Williams", "tickets", 4));
        mListData.add(new ListData("Jaden Williams", "tickets", 4));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = view.findViewById(R.id.list);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new MyItemRecyclerViewAdapter(mContext, mListData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSellingRowClicked(v);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(mRecyclerView.getContext(), manager.getOrientation());
        mRecyclerView.addItemDecoration(itemDecor);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void onSellingRowClicked(View v)
    {
        Intent intent = new Intent(getActivity(), ItemTradeDetailsActivity.class);
        int itemPosition = mRecyclerView.getChildAdapterPosition(v);
        ListData sellerData = mAdapter.getItem(itemPosition);
        intent.putExtra("SELLER_NAME", sellerData.getSeller());
        intent.putExtra("SELLER_ITEM", sellerData.getItemType());
        intent.putExtra("SELLER_ITEM_VALUE", sellerData.getPrice());
        startActivity(intent);
    }
}
