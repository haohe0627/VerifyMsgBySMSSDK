package com.tc.verifycountdown4;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends BaseActivity implements VerifyCallBack, OnSendMessageHandler{

    private Button btn, submit;
    private EditText ed;
    private TextView txt;
    private EventHandler eh;

    @Override
    protected void init() {

        btn = (Button) findViewById(R.id.btn);
        txt = (TextView)findViewById(R.id.txt);
        ed = (EditText)findViewById(R.id.edit);
        submit = (Button) findViewById(R.id.submit);
//        submit.setSelected(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getMyApp().isCountingDown()){

                    getMyApp().setCallBack(MainActivity.this);
                    getMyApp().startCountDown();
                    btn.setSelected(true);

                    SMSSDK.getVerificationCode("86", "your telephone"); // 5 times per day
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!submit.isSelected())
                SMSSDK.submitVerificationCode("86", "your telephone", ed.getText().toString() );
            }
        });

        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                                submit.setSelected(false);
                            }
                        });
                    }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }else if( event == SMSSDK.RESULT_ERROR){
                        // 验证码错误

                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    try {
                        JSONObject object = new JSONObject(((Throwable)data).getMessage());
                        final String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, des, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }catch (Exception e){

                    }
                }
            }
        };

        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void CountingDown(final int count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn.setText(count +"");
            }
        });
    }

    @Override
    public void CountDownComplete() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn.setSelected(false);
                btn.setText("CLICK");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    public boolean onSendMessage(String s, String s1) {
        //用途是在发送短信之前，开发者自己执行一个操作，来根据电话号码判断是否需要发送短信
        return false;
    }
}
