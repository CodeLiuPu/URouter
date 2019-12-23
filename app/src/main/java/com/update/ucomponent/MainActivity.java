package com.update.ucomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.update.base.RouterConstants;
import com.update.router_core.URouter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URouter.get()
                .build(RouterConstants.Module1.ACTIVITY)
                .navigation();
    }
}
