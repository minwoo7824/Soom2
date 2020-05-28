package com.kmw.soom2.MyPage.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.kmw.soom2.R;

public class SecessionDialog  extends Dialog implements View.OnClickListener {

    Button cancelButton,secessionButton;
    private SecessionButtonListener secessionButtonListener;

    public SecessionDialog(Context context, SecessionButtonListener secessionButtonListener) {
        super(context);
        this.secessionButtonListener = secessionButtonListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.secession_dialog);
    }

    public interface SecessionButtonListener {

        void secessionButton(boolean data);
    }
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secession_dialog);

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams wm = getWindow().getAttributes();
        wm.copyFrom(getWindow().getAttributes());
        setCancelable(false);
        wm.width = (int) (width * 0.89);
        wm.height = (int) (height * 0.47);
        cancelButton = (Button) findViewById(R.id.cancel_button_secession_dialog);
        secessionButton = (Button) findViewById(R.id.secession_button_secession_dialog);
        cancelButton.setOnClickListener(this);
        secessionButtonListener.secessionButton(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_button_secession_dialog:
                dismiss();

            case R.id.secession_button_secession_dialog:
                secessionButtonListener.secessionButton(true);
                dismiss();
        }
    }
}



