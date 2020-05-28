package com.kmw.soom2.CommunityFragmentFunc.Fragments.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityAdapter;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.itemsArrayList;

public class CommunityLeftFragment extends Fragment {

    private String TAG = "CommunityLeftFragment";
    ListView listView;
    CommunityAdapter adapter;

    public CommunityLeftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community_left, container, false);
        listView = (ListView)view.findViewById(R.id.list_community);

        adapter = new CommunityAdapter(getActivity(),this);

        if (itemsArrayList != null){
            for (int i = 0; i < itemsArrayList.size(); i++){
                adapter.addItem(itemsArrayList.get(i));

            }
            listView.setAdapter(adapter);
        }

        return view;
    }
}
