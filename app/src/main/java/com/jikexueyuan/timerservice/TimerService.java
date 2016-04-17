package com.jikexueyuan.timerservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class TimerService extends Service {

    public TimerService() {
    }

    private RemoteCallbackList<TimerServiceCallback> callbackList = new RemoteCallbackList<>();
    private String data = "默认数据";
    private boolean running =false;
    private int numIndex = 0;

    @Override
    public IBinder onBind(Intent intent) {
       return new IServiceRemoteBinder.Stub() {

           @Override
           public void setData(String data) throws RemoteException {
                TimerService.this.data = data;
           }

           @Override
           public void registCallback(TimerServiceCallback callback) throws RemoteException {
               callbackList.register(callback);
           }

           @Override
           public void unRegistCallback(TimerServiceCallback callback) throws RemoteException {
               callbackList.unregister(callback);
           }
       };

    }


    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Service Started");



        new Thread(){
            @Override
            public void run() {
                super.run();

                running =true;

                for(numIndex = 0; running; numIndex++){

                    System.out.println(numIndex);

                    int count = callbackList.beginBroadcast();
                    while (count-- > 0){
                        try {
                             callbackList.getBroadcastItem(count).onTimer(numIndex);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    callbackList.finishBroadcast();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("Service Destroy");

        running = false;
    }
    private Callback callback = null;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public static interface Callback{
        void onDataChange(String data);
    }

}
