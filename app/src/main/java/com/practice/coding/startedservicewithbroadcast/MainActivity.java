package com.practice.coding.startedservicewithbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private ProgressBar progressBar;

    //after registering broadcast receiver get data from the receiver..It run on the Main Thread
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(Constants.TAG, " Thread Name : "+Thread.currentThread().getName());
            String getSongName = intent.getStringExtra(Constants.MESSAGE_KEY);
            boolean isAllDonwloadsCompleted = intent.getBooleanExtra(Constants.PROGRESS_KEY, false);

            tv.append("\n" + getSongName+" : Downloaded!  stopSelfResult : "+isAllDonwloadsCompleted);

            if (isAllDonwloadsCompleted) {
                displayProgresBar(false);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //Register the broadcast
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, new IntentFilter(Constants.SERVICE_INTENT_FILTER_KEY));
    }

    @Override
    protected void onStop() {
        super.onStop();
        //un-register the broadcast for make sure no memory resources leakes
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBarHorizontal);
        tv = findViewById(R.id.textView);
    }

    public void runService(View view) {
        displayProgresBar(true);
        tv.setText("Code Running...");
        for (String song : Playlist.songs) {
            /*
            For every song intent executed and send msg to to StartedService class..like 3 songs 3 times intent executed and send message..
             */
            Intent intent = new Intent(this, StartedService.class);
            intent.putExtra(Constants.MESSAGE_KEY, song);


            startService(intent); //here StartedService Start and startService take intent parameter..
            //this intent is reveived in StartedService Java Class in onStartCommand..
        }
    }

    public void displayProgresBar(boolean display) {
        if (display) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}