package com.update.module1;

import android.app.Activity;
import android.os.Bundle;

import com.update.base.RouterConstants;
import com.update.router_annotation.Route;

@Route(path = RouterConstants.Module1.ACTIVITY)
public class Module1Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
    }
}