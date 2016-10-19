package org.yctech.util;

import java.util.ResourceBundle;

/**
 * Created by lerry on 2016/10/19.
 * 获取配置文件中的值
 */
public class ResourceUtil {

    private static ResourceBundle resourceBundle = null;

    public ResourceUtil(){

    }

    public ResourceUtil(String propertieName){
        resourceBundle = ResourceBundle.getBundle(propertieName);
    }

    public static String getResource(String key) {
        String result = null;
        try{
            result = resourceBundle.getString(key);
        }catch (Exception e){

        }
        return result;
    }

}
