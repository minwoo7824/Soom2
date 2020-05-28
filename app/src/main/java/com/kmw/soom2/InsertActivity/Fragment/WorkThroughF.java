package com.kmw.soom2.InsertActivity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kmw.soom2.Home.HomeActivity.MainActivity;
import com.kmw.soom2.R;


public class WorkThroughF extends Fragment {
    TextView textView;
    public WorkThroughF() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_through_f, container, false);
        textView = (TextView) view.findViewById(R.id.pass_to_start_text_view_fragment_work_through);
        SpannableString con = new SpannableString(textView.getText());
        con.setSpan(new UnderlineSpan(), 0, con.length(), 0);
        textView.setText(con);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}
