package com.kmw.soom2.MyPage.Fragment.ChildFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.CommunityFragmentFunc.Adapters.CommunityAdapter;
import com.kmw.soom2.R;

public class WriteFragment extends Fragment {

    private String TAG = "WriteFragment";
    ListView listView;
    CommunityAdapter adapter;

    public WriteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        listView = (ListView)view.findViewById(R.id.list_view_my_page_writing);

        adapter = new CommunityAdapter(getActivity(),this);

        for (int i = 0; i < Utils.itemsArrayList.size(); i++){
            if (Utils.itemsArrayList.get(i).getUserNo().equals(""+ Utils.userItem.getUserNo())){
                adapter.addItem(Utils.itemsArrayList.get(i));
            }
        }

        listView.setAdapter(adapter);

        return view;
    }
}
