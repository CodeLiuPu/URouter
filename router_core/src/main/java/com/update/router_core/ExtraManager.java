package com.update.router_core;

import android.app.Activity;
import android.util.LruCache;

import com.update.router_core.template.IExtra;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class ExtraManager {
    public static final String SUFFIX_AUTOWIRED = "$$Extra";
    private LruCache<String, IExtra> mClassCache;

    private ExtraManager() {
        mClassCache = new LruCache<>(66);
    }

    public static ExtraManager get() {
        return Holder.INSTANCE;
    }

    public void loadExtras(Activity activity) {
        // 查找对应的 activity缓存
        String className = activity.getClass().getName();
        IExtra iExtra = mClassCache.get(className);

        if (null == iExtra) {
            try {
                String extraClassName = className + SUFFIX_AUTOWIRED;
                iExtra = (IExtra) Class.forName(extraClassName).getConstructor().newInstance();
                iExtra.loadExtra(activity);
                mClassCache.put(className, iExtra);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final class Holder {
        private static final ExtraManager INSTANCE = new ExtraManager();
    }
}
