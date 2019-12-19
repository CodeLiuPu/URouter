package com.update.router_core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.update.router_annotation.model.RouteMeta;
import com.update.router_core.callback.NavigationCallback;
import com.update.router_core.exception.NoRouteFoundException;
import com.update.router_core.template.IRouteGroup;
import com.update.router_core.template.IRouteRoot;
import com.update.router_core.template.IService;
import com.update.router_core.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class URouter {

    private static final String TAG = "URouter";
    private static final String ROUTE_ROOT_PACKAGE = "com.update.router_core";
    private static final String SDK_NAME = "URouter";
    private static final String SEPARATOR = "$$";
    private static final String SUFFIX_ROOT = "Root";
    private static Application mApp;

    private URouter() {
    }

    public static URouter get() {
        return Holder.INSTANCE;
    }

    /**
     * 初始化
     */
    public static void init(Application application) {
        mApp = application;
    }

    public static void loadInfo() throws PackageManager.NameNotFoundException, InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 获取 所有 apt生成 的路由类的全类名 (路由表)
        Set<String> routerMap = ClassUtils.getFileNameByPackageName(mApp, ROUTE_ROOT_PACKAGE);
        for (String className : routerMap) {
            if (className.startsWith(ROUTE_ROOT_PACKAGE + "." + SDK_NAME
                    + SEPARATOR + SUFFIX_ROOT)) {
                // root 中 注册的是分组信息, 讲分组信息加入仓库中
                IRouteRoot iRouteRoot = (IRouteRoot) Class.forName(className).getConstructor().newInstance();
                iRouteRoot.loadInto(Warehouse.groupsIndex);
            }
        }
        //        for (Map.Entry<String, Class<? extends IRouteGroup>> stringClassEntry : Warehouse
//                .groupsIndex.entrySet()) {
//            Log.e(TAG, "Root映射表[ " + stringClassEntry.getKey() + " : " + stringClassEntry
//                    .getValue() + "]");
//        }
    }

    public Postcard build(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("path cannot be empty");
        } else {
            return build(path, extractGroup(path));
        }
    }

    public Postcard build(String path, String group) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
            throw new RuntimeException("path or group cannot be empty");
        } else {
            return new Postcard(path, group);
        }
    }

    /**
     * 获取组别
     */
    private String extractGroup(String path) {
        if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new RuntimeException(path + " : cannot get group");
        }

        try {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            if (TextUtils.isEmpty(defaultGroup)) {
                throw new RuntimeException(path + " : cannot get group");
            } else {
                return defaultGroup;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object navigation(Context context, final Postcard postcard,
                             final int requestCode, final NavigationCallback callback) {

        try {
            prepareCard(postcard);
        } catch (NoRouteFoundException e) {
            e.printStackTrace();
            // 没找到 路由
            if (null != callback) {
                callback.onLost(postcard);
            }
            return null;
        }
        if (null != callback) {
            callback.onFound(postcard);
        }

        switch (postcard.getType()) {
            case ACTIVITY: {
                final Context currentContext = (null == context) ? mApp : context;
                final Intent intent = new Intent(currentContext, postcard.getDestination());
                intent.putExtras(postcard.getExtras());
                int flag = postcard.getFlag();
                if (-1 != flag) {
                    intent.setFlags(flag);
                } else if (currentContext instanceof Activity) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 可能需要返回码
                        if (requestCode > 0) {
                            ActivityCompat.startActivityForResult((Activity) currentContext, intent,
                                    requestCode, postcard.getOptionsBundle());
                        } else {
                            ActivityCompat.startActivity(currentContext, intent, postcard
                                    .getOptionsBundle());
                        }

                        if ((0 != postcard.getEnterAnim() || 0 != postcard.getExitAnim())
                                && currentContext instanceof Activity) {
                            //老版本
                            ((Activity) currentContext).overridePendingTransition(postcard
                                            .getEnterAnim()
                                    , postcard.getExitAnim());
                        }
                        // 跳转完成
                        if (null != callback) {
                            callback.onArrival(postcard);
                        }
                    }
                });
                break;
            }
            case ISERVICE: {
                return postcard.getService();
            }
            default:
        }
        return null;
    }

    /**
     * 准备卡片
     */
    private void prepareCard(Postcard card) {
        RouteMeta routeMeta = Warehouse.routes.get(card.getPath());
        // 还未准备的
        if (null == routeMeta) {
            // 创建并调用 loadInto 函数, 并记录进仓库
            Class<? extends IRouteGroup> groupMeta = Warehouse.groupsIndex.get(card.getGroup());
            if (null == groupMeta) {
                String msg = "not found the route: " + card.getGroup() + " " + card.getPath();
                throw new NoRouteFoundException(msg);
            }
            IRouteGroup iRouteGroup;

            try {
                iRouteGroup = groupMeta.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("路由分组映射表记录失败.", e);
            }

            iRouteGroup.loadInto(Warehouse.routes);

            // 已经准备过 就可以移除了 (不会一直存在内存中)
            Warehouse.groupsIndex.remove(card.getGroup());
            // 再次进入 else
            prepareCard(card);
        } else {
            // 类 要跳转的 activity 或者 IService实现类
            card.setDestination(routeMeta.getDestination());
            card.setType(routeMeta.getType());

            switch (routeMeta.getType()) {
                case ISERVICE: {
                    Class<?> destination = routeMeta.getDestination();
                    IService service = Warehouse.services.get(destination);
                    if (null == service) {
                        try {
                            service = (IService) destination.getConstructor().newInstance();
                            Warehouse.services.put(destination, service);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    card.setService(service);
                    break;
                }
                case ACTIVITY: {
                    break;
                }
                default:
            }
        }
    }

    /**
     * 注入
     */
    public void inject(Activity activity) {
        ExtraManager.get().loadExtras(activity);
    }

    private static final class Holder {
        private static final URouter INSTANCE = new URouter();
    }
}
