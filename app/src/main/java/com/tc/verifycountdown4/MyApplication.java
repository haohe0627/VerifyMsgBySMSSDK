package com.tc.verifycountdown4;

import android.app.Application;

import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.SMSSDK;

/**
 * Created by haohe on 2017/6/28 0028.
 */

public class MyApplication extends Application {

    private int countDownNo = 30;
    private Timer timer;
    private VerifyCallBack callBack;

    public void setCallBack(VerifyCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        MobSDK.init(this, "key", "id");
    }

    public void startCountDown(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                if(callBack != null){
                    countDownNo --;
                    callBack.CountingDown(countDownNo);

                    if(countDownNo == 0){
                        countDownNo = 30;
                        callBack.CountDownComplete();
                        timer.cancel();
                        timer = null;
                        cancel();
                    }
                }
            }
        };
        timer.schedule(timerTask,0, 1000 );
    }

    public boolean isCountingDown(){
        return countDownNo != 30;
    }
}
