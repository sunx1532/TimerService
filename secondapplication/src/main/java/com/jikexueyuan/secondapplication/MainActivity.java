package com.jikexueyuan.secondapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import com.jikexueyuan.timerservice.IServiceRemoteBinder;
import com.jikexueyuan.timerservice.TimerServiceCallback;


public class MainActivity extends Activity implements View.OnClickListener, ServiceConnection {

    private Intent serviceIntent;
    private IServiceRemoteBinder binder =null;
    private TextView tvTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.jikexueyuan.timerservice","com.jikexueyuan.timerservice.TimerService"));

        findViewById(R.id.btnBindService).setOnClickListener(this);
        findViewById(R.id.btnUnbindService).setOnClickListener(this);

        tvTimer = (TextView) findViewById(R.id.tvTimer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBindService:
                bindService(serviceIntent,this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindService:
                if (isServiceRunning()){
                    unbindService(this);
                }
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        System.out.println("Bind Service");
        System.out.println(service);
        binder = IServiceRemoteBinder.Stub.asInterface(service);
        try {
            binder.registCallback(onServiceCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private TimerServiceCallback.Stub onServiceCallback = new TimerServiceCallback.Stub() {
        @Override
        public void onTimer(int numberIndex) throws RemoteException {
            Message msg = new Message();
            msg.obj = MainActivity.this;
            msg.arg1 = numberIndex;
            handler.sendMessage(msg);
        }
    };


    private final MyHandler handler = new MyHandler();

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int index = msg.arg1;
            MainActivity _this = (MainActivity) msg.obj;
            _this.tvTimer.setText("Timer:" + index + "s");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        callUnRegistBinder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callUnRegistBinder();
    }

    private void callUnRegistBinder(){
        try {
            binder.unRegistCallback(onServiceCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private boolean isServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if ("com.jikexueyuan.timerservice.TimerService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
