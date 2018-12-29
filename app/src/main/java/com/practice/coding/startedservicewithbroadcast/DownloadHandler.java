package com.practice.coding.startedservicewithbroadcast;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class DownloadHandler extends Handler {

    public StartedService startedServiceRef;
    private Context mContext;

    @Override
    public void handleMessage(Message msg) {

        String songName = msg.obj.toString();

        downloadSong(songName);

        //startedServiceRef.stopSelf(); //after complete task or if app is close or crash it shut dowm the service completely

      //  startedServiceRef.stopSelf(msg.arg1);
        boolean result = startedServiceRef.stopSelfResult(msg.arg1);

        //send data to UI using Local Broadcast
        sendDataToUI(songName, result);

    }

   // send data to UI using Local Broadcast
    private void sendDataToUI(String songName, boolean result) {
        Intent intent = new Intent(Constants.SERVICE_INTENT_FILTER_KEY);
        intent.putExtra(Constants.MESSAGE_KEY, songName);
        intent.putExtra(Constants.PROGRESS_KEY, result);

        //send intent with local broadcast
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void downloadSong(String songName) {
        try {
            Log.d(Constants.TAG, songName+" : Download Starting . . . ");
            Thread.sleep(4000);
            Log.d(Constants.TAG, songName+" : Download Completed!");
        } catch (InterruptedException e) {

        }
    }

    //get service class referance
    public void setStartedServiceRef(StartedService startedServiceRef) {
        this.startedServiceRef = startedServiceRef;
    }

    //get service class context
    public void setContext(Context context)
    {
        this.mContext = context;
    }
}
