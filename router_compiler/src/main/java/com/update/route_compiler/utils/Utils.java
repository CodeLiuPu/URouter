package com.update.route_compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class Utils {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll){
        return coll ==null || coll.isEmpty();
    }

    public static boolean isEmpty(Map<?,?> map){
        return map ==null || map.isEmpty();
    }
}
