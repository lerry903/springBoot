package org.yctech.common;

import org.yctech.resource.ConstantsResource;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 14:56
 */
public class GlobalConstant {

    public static HashMap constantResourceCache = new HashMap();

    public static String getConstantResource(String category, Object key) {
        if (!constantResourceCache.containsKey(category + "." + key)) {
            constantResourceCache.put(category + "." + key, ConstantsResource.getInstance().getResource(category, key.toString()));
        }
        return (String) constantResourceCache.get(category + "." + key);
    }

}
