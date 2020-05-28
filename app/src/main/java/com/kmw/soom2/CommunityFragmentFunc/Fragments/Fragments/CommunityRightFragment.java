package com.kmw.soom2.CommunityFragmentFunc.Fragments.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.kmw.soom2.CommunityFragmentFunc.Adapters.NoticeAdapter;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.itemsArrayList;


public class CommunityRightFragment extends Fragment {

    private String TAG = "CommunityRightFragment";
    ListView listView;
    NoticeAdapter adapter;

    public CommunityRightFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_right, container, false);

        listView = (ListView)view.findViewById(R.id.list_notice);

        adapter = new NoticeAdapter(getActivity(),this);

        try{
            for (int i = 0; i < itemsArrayList.size(); i++){
                if (itemsArrayList.get(i).getLv().equals("99")){
                    adapter.addItem(itemsArrayList.get(i));
                }
            }
        }catch (Exception e){

        }

        listView.setAdapter(adapter);

        return view;
    }
}
