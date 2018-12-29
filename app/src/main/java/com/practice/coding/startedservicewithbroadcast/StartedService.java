package com.practice.coding.startedservicewithbroadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

public class StartedService extends Service {
    private DownloadThread downloadThread;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(Constants.TAG, "onCreate Callled : ");
        downloadThread = new DownloadThread();
        downloadThread.start();

        while (downloadThread.mHandler == null)
        {
            //loop till mHandler is inialized
        }

        downloadThread.mHandler.setStartedServiceRef(this);
        downloadThread.mHandler.setContext(this); //in service class we get context..but in handler class not
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "onStartCommand called with startId: "+startId);
        Log.d(Constants.TAG, "onStartCommand run on Thread Name : "+Thread.currentThread().getName());

        String songName = intent.getStringExtra(Constants.MESSAGE_KEY);
        Message message = Message.obtain();
        message.obj = songName;
        message.arg1 = startId;

        downloadThread.mHandler.sendMessage(message);

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.TAG, "onDestroy Called.");
    }
}
