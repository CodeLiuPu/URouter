package com.update.module2;

import android.app.Activity;
import android.os.Bundle;

import com.update.base.RouterConstants;
import com.update.router_annotation.Route;

@Route(path = RouterConstants.Module2.ACTIVITY)
public class Module2Activity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module2);

    }
}