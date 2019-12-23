package com.update.ucomponent;

import android.util.Log;

import com.update.base.RouterConstants;
import com.update.base.TestService;
import com.update.router_annotation.Route;

/**
 * @author : liupu
 * date   : 2019/12/23
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
@Route(path = RouterConstants.Module3.SERVICE)
public class TestServiceImpl implements TestService {

    @Override
    public void test() {
        Log.e("update", "updatettttt setr");
    }
}
