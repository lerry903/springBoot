package org.yctech.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 包含继承关系的ResourceBoudle
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 15:46
 */
public class ResourceBoxBundle {
    /**
     * 基本名
     */
    private String[] baseNames = null;

    /**
     * Locale
     */
    private Locale locale = null;

    /**
     * 使用的ClassLoader
     */
    private ClassLoader loader = null;

    /**
     * 存放Resource的容器
     */
    private Map resourceMap = null;

    //    private static String RELATIONSHIP_POSTFIX = ".relationships";
    private static String RESOURCE_POSTFIX = ".properties";
    private static char RESOURCE_SEPARATOR = '/';

    private static String READ_ENCODE = ResourceConfig.SOURCE_ENCODE;
    private static String TARGET_ENCODE = ResourceConfig.TARGET_ENCODE;

    /**
     * 构建方法
     *
     * @param _baseNames 基本名
     * @param _locale    所处区域
     * @param _loader    使用的ClassLoader
     */
    protected ResourceBoxBundle(String[] _baseNames, Locale _locale, ClassLoader _loader) {
        this.baseNames = _baseNames;
        this.locale = _locale;
        this.loader = _loader;
//        resourceMap = getResourceMap();
        resourceMap = new HashMap();
        for (int i = 0; i < baseNames.length; i++) {
            String name = baseNames[i];
            Map map = getResourceMapOfOneFile(name, _locale, _loader);
            resourceMap.putAll(map);
        }
    }

    private static Map getResourceMapOfOneFile(String _baseName, Locale _locale, ClassLoader _loader) throws IllegalStateException {


        Map map = new HashMap();

        String baseResourceName = _baseName.replace('.', RESOURCE_SEPARATOR) + "_" + _locale + RESOURCE_POSTFIX;

        InputStream is = _loader.getResourceAsStream(baseResourceName);

        if (is != null) {
            Properties baseProperties = new Properties();

            try {
                baseProperties.load(is);

                Iterator keys = baseProperties.keySet().iterator();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String resource = (String) baseProperties.get(key);
                    if (ResourceConfig.VALUE_NEED_ENCODE) {
                        resource = new String((resource).getBytes(READ_ENCODE), TARGET_ENCODE);
                    }
                    map.put(key, resource);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            initRelationships();
        }
        return map;
    }

    // Getter and Setter
    public Map getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map _resourceMap) {
        resourceMap = _resourceMap;
    }


    /**
     *
     */
    public Collection getKeys() {
        return resourceMap.keySet();
    }


    public String getString(String _key)
            throws MissingResourceException {
        if (resourceMap.keySet().contains(_key)) {
            return (String) resourceMap.get(_key);
        } else {
            throw new MissingResourceException("Missing Resource, the key is " + _key, this.getClass().getName(), _key);
        }
    }

}
