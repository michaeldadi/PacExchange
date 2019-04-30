package com.cs639.pacexchange;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    RecyclerView mRecyclerView;
    Context mContext;
    List<ListData> mListData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    //Loadv stuff from database here
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

        MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(mContext, mListData, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSellingRowClicked(v);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }

    private void onSellingRowClicked(View v)
    {

    }
}
