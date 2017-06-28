package com.tc.verifycountdown4;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by haohe on 2017/6/28 0028.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        init();
    }

    protected abstract void init();
    protected abstract int setLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected MyApplication getMyApp(){
        return (MyApplication) getApplication();
    }
}
