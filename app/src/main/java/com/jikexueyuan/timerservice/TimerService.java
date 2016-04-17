package com.jikexueyuan.timerservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TimerService extends Service {
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Service Started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("Service destroy");
    }
}
