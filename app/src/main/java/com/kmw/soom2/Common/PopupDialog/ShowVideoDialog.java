package com.kmw.soom2.Common.PopupDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.kmw.soom2.R;
import com.kmw.soom2.databinding.DialogShowvideoBinding;

public class ShowVideoDialog  extends Dialog {

    DialogShowvideoBinding binding;

    private Uri videoUri;

    private Activity activity;
    Context context;

    public ShowVideoDialog(Context context, Uri videoUri, Activity activity) {
        super(context);
        this.videoUri = videoUri;
        this.activity = activity;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_showvideo, null, false);
        setContentView(binding.getRoot());
        String uriStr = this.videoUri.toString();
        uriStr = uriStr.substring(16, uriStr.length() - 12);
        binding.videoView.setVideoURI(this.videoUri);
        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.videoView.start();
            }
        });

        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               cancel();
            }
        });
    }
}
