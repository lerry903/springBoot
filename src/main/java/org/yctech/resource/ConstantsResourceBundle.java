package org.yctech.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 15:02
 */
public class ConstantsResourceBundle {
    /**
     * ����
     */
    private String[] baseNames = null;

    /**
     * Locale
     */
    private Locale locale = null;

    /**
     * ʹ�õ�ClassLoader
     */
    private ClassLoader loader = null;

    /**
     * ���Resource������
     */
    private Map resourceMap = null;

    /**
     * ��������б������
     */
    private Map typeMap = null;

    private static String RESOURCE_POSTFIX = ".properties";
    private static char RESOURCE_SEPARATOR = '/';
    public static String KEY_KEY = "constants_key";
    public static String KEY_VALUE = "constants_value";

    private static String CONSTANTS_TYPE_SEPARATOR = ".";

    private static String READ_ENCODE = ResourceConfig.SOURCE_ENCODE;
    private static String TARGET_ENCODE = ResourceConfig.TARGET_ENCODE;

    /**
     * ��������
     *
     * @param _baseName ����
     * @param _locale   ������
     * @param _loader   ʹ�õ�ClassLoader
     */
    protected ConstantsResourceBundle(String[] _baseName, Locale _locale, ClassLoader _loader) throws IllegalStateException {
        locale = _locale;
        loader = _loader;
        this.baseNames = _baseName;
        typeMap = new HashMap();
        resourceMap = new HashMap();

        for (int i = 0; i < baseNames.length; i++) {
            String name = baseNames[i];
            Map type = new HashMap();
            Map resource = new HashMap();
            init(name,locale,loader,type,resource);

            typeMap.putAll(type);
            resourceMap.putAll(resource);
        }
    }

    protected static void  init(String _baseName, Locale _locale, ClassLoader _loader,Map typeMap,Map resourceMap) throws IllegalStateException {
//        baseNames = _baseName;


        String baseResourceName = _baseName.replace('.', RESOURCE_SEPARATOR) + "_" + _locale + RESOURCE_POSTFIX;

        InputStream is = _loader.getResourceAsStream(baseResourceName);

        if (is != null) {
            Properties baseProperties = new Properties();

            try {
                baseProperties.load(is);

                Iterator keys = baseProperties.keySet().iterator();

                while (keys.hasNext()) {
                    String key = (String) keys.next();

                    StringTokenizer st = new StringTokenizer(key, CONSTANTS_TYPE_SEPARATOR);

                    String type = st.nextToken();
                    String subKey = st.nextToken();

                    List typeResource = (List) typeMap.get(type);

                    if (typeResource == null) {
                        typeResource = new ArrayList();
                        typeMap.put(type, typeResource);
                    }

                    String resource = (String) baseProperties.get(key);
                    if (ResourceConfig.VALUE_NEED_ENCODE) {
                        resource = new String((resource).getBytes(READ_ENCODE), TARGET_ENCODE);
                    }

                    Map value = new HashMap();
                    value.put(KEY_KEY, subKey);
                    value.put(KEY_VALUE, resource);

                    typeResource.add(value);

                    resourceMap.put(key, resource);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Getter and Setter
    public Map getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map _resourceMap) {
        resourceMap = _resourceMap;
    }

    public Map getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map typeMap) {
        this.typeMap = typeMap;
    }

    /**
     * ��ȡConstants�ķ���
     *
     * @param _type ����
     * @param _key  ��ֵ
     * @return Constants
     * @throws MissingResourceException ���û���ҵ�Constants
     */
    public String getResource(String _type, String _key)
            throws MissingResourceException {
        String fullKey = _type + CONSTANTS_TYPE_SEPARATOR + _key;

        if (resourceMap.keySet().contains(fullKey)) {
            return (String) resourceMap.get(fullKey);
        } else {
            throw new MissingResourceException("Missing Resource, the type is "
                    + _type + ",the key is " + _key, this.getClass().getName(), _key);
        }
    }


    /**
     * ��� Constants�����ͻ�ȡ�б�
     *
     * @param _type ����
     * @return ���ص�Constants�б�
     * @throws MissingResourceException ������Ͳ��������׳�
     */
    public List getResourceByType(String _type)
            throws MissingResourceException {
        if (typeMap.keySet().contains(_type)) {
            return (List) typeMap.get(_type);
        } else {
            throw new MissingResourceException("Missing Resource, the type is "
                    + _type, this.getClass().getName(), _type);
        }

    }
}
