// IServiceRemoteBinder.aidl
package com.jikexueyuan.timerservice;

// Declare any non-default types here with import statements
import com.jikexueyuan.timerservice.TimerServiceCallback;

interface IServiceRemoteBinder {

    void setData(String data);

    void registCallback(TimerServiceCallback callback);
    void unRegistCallback(TimerServiceCallback callback);

}
