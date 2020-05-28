package com.kmw.soom2.InsertActivity.InsertActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.kmw.soom2.MyPage.Activity.PatientInfoActivity;
import com.kmw.soom2.R;

import java.text.SimpleDateFormat;

public class DatePickerDialogActivity extends DialogFragment {

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ConfirmButtonListener confirmButtonListener;
    int flag;

    public DatePickerDialogActivity(int flag, UserInfoActivity userInfoActivity, ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
        this.flag = flag;
    }

    public DatePickerDialogActivity(PatientInfoActivity patientInfo, ConfirmButtonListener confirmButtonListener) {
        this.confirmButtonListener = confirmButtonListener;
    }
    public DatePickerDialogActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button cancelButton, confirmButton;
        final DatePicker datePicker;
        final Dialog dialog = getDialog();
        datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
        preferences = getActivity().getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        cancelButton = (Button) dialog.findViewById(R.id.cancel_button_activity_date_picker_dialog);
        confirmButton = (Button) dialog.findViewById(R.id.confirm_button_activity_date_picker_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeight = (int) (metrics.heightPixels * 0.5);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(false);
        final Calendar pickedDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        },
                pickedDate.get(Calendar.YEAR),
                pickedDate.get(Calendar.MONTH),
                pickedDate.get(Calendar.DAY_OF_MONTH)

        );
        maxDate.set(pickedDate.get(Calendar.YEAR),pickedDate.get(Calendar.MONTH),pickedDate.get(Calendar.DAY_OF_MONTH));
        Log.i("maxDate", "" + maxDate);
        datePicker.setMaxDate(maxDate.getTimeInMillis());
        Log.i("maxDate", "" + maxDate.getTimeInMillis());

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        datePickerDialog.setCancelable(false);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                String confirmDate = format1.format(newDate.getTime());
                editor.putString("confirm_date",confirmDate);
                editor.apply();
                confirmButtonListener.confirmButton(confirmDate);
                dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_date_picker_dialog, container, false);
        TextView txtTitle = (TextView)v.findViewById(R.id.txt_date_picker_dialog_title);

        if (flag == 0){
            txtTitle.setText("생년월일");
        }else{
            txtTitle.setText("확진일");
        }

        return v;
    }

    public interface ConfirmButtonListener {
        void confirmButton(String data);
    }
}