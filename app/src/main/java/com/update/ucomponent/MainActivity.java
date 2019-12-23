package com.update.ucomponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.update.base.RouterConstants;
import com.update.base.TestService;
import com.update.router_core.URouter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URouter.get()
                .build(RouterConstants.Module1.ACTIVITY)
                .navigation();

        TestService service = (TestService) URouter.get()
                .build(RouterConstants.Module3.SERVICE).navigation();
        service.test();

    }
}
