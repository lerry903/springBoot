package org.yctech.resource;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 14:58
 */
public class ConstantsResource {
    /**
     * 缺省的Locale
     */
    private Locale defaultLocale = null;

    /**
     * 内置的ConstantsResource对象
     */
    protected static ConstantsResource resource = null;


    /**
     * 保存所有Locale的Resource信息的容器
     */
    private Map globeResource;


    /**
     * resource的基本名
     */
    private String[] baseNames;


    /**
     * Resource的Locale列表
     */
    private List localeList;


    /**
     * 指定的Resource的ClassLoader
     */
    private ClassLoader classLoader;


    /**
     * 私有的构建方法
     */
    private ConstantsResource() {
        init();
    }

    /**
     * 获取ConstantsResource的实例
     *
     * @return ConstantsResource的唯一实例
     */
    public synchronized static ConstantsResource getInstance() {
        if (resource == null) {
            resource = new ConstantsResource();
        }
        return resource;
    }

    protected void init() {
        String defaultLocaleStr = ResourceConfig.DEFAULT_LOCALE;
        StringTokenizer st = new StringTokenizer(defaultLocaleStr, "_");
        defaultLocale = new Locale(st.nextToken(), st.nextToken());

        String[] langStrs = ResourceConfig.LOCALE_LIST.split(",");
        ArrayList localeList = new ArrayList();
        for (int i = 0; i < langStrs.length; i++) {
            String[] sLocale = langStrs[i].split("_");
            localeList.add(new Locale(sLocale[0], sLocale[1]));
        }

//        ResourceBox.getInstance().initialize("resourcebox",localeList);


        String constantsBasename = ResourceConfig.CONSTANTS_FILES;
        StringTokenizer st2 = new StringTokenizer(constantsBasename, ";");
        baseNames = new String[st2.countTokens()];
        int i = 0;
        while (st2.hasMoreTokens()) {
            baseNames[i++] = st2.nextToken().trim();
        }
        initialize(baseNames, localeList, null);

    }

    /**
     * 初始化ConstantsResource，使用指定的baseName、locale、classLoader读取property
     * resource文件。
     * 如果没有继承关系的文件中重复定义了resource时，错误信息会被记录下来，
     * 程序依旧运行。
     *
     * @param _baseName    resource文件的基本名字
     * @param _localeList  需要载入的Locale的列表，里面包含的Item是一个Locale对象
     * @param _classLoader 使用的ClassLoader
     * @throws java.util.MissingResourceException 指定的properties文件或被它继承的文件找不到时抛出
     */
    private void initialize(String[] _baseName,
                            List _localeList,
                            ClassLoader _classLoader)
            throws MissingResourceException {

        this.baseNames = _baseName;
        this.localeList = _localeList;

        if (_classLoader != null) {
            this.classLoader = _classLoader;
        } else {
            this.classLoader = this.getClass().getClassLoader();
        }

        globeResource = new HashMap();

        for (int i = 0; i < _localeList.size(); i++) {
            Locale locale = (Locale) _localeList.get(i);

            ConstantsResourceBundle bundle = new ConstantsResourceBundle(baseNames, locale, classLoader);

            globeResource.put(locale.toString(), bundle);
        }
    }

    /**
     * 取缺省Locale
     *
     * @return
     */

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * 设置缺省Locale
     *
     * @param defaultLocale
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * 根据_key得到相应的resource。
     *
     * @param _key  根据指定的键值获取缺省的Resource
     * @param _type Constants 的类型
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws java.util.MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getResource(String _type, String _key)
            throws MissingResourceException {
        return getResource(_type, _key, defaultLocale);
    }


    /**
     * 根据_key和_arguments得到相应的resource。
     *
     * @param _type Constants的类型
     * @return 返回获取的Resource List，如果不存在返回空字符串。
     * @throws java.util.MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public List getResourceByType(String _type)
            throws MissingResourceException {
        return getResourceByType(_type, defaultLocale);
    }


    /**
     * 根据_key得到相应的resource。
     *
     * @param _key    根据指定的键值获取缺省的Resource
     * @param _type   Constants 的类型
     * @param _locale 指定的区域信息
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws java.util.MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getResource(String _type, String _key, Locale _locale)
            throws MissingResourceException {
        if (_locale == null) {
            _locale = defaultLocale;
        }
        ConstantsResourceBundle bundle =
                (ConstantsResourceBundle) globeResource.get(_locale.toString());

        if (bundle == null) {
            throw new IllegalArgumentException("ConstantsResource is not contains the locale " + _locale);
        }

        return bundle.getResource(_type, _key);
    }


    /**
     * 根据_key和_arguments得到相应的resource。
     *
     * @param _type   Constants的类型
     * @param _locale 指定的区域信息
     * @return 返回获取的Resource List，如果不存在返回空字符串。
     * @throws java.util.MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public List getResourceByType(String _type, Locale _locale)
            throws MissingResourceException {
        ConstantsResourceBundle bundle =
                (ConstantsResourceBundle) globeResource.get(_locale.toString());

        if (bundle == null) {
            throw new IllegalArgumentException("ConstantsResource is not contains the locale " + _locale);
        }

        return bundle.getResourceByType(_type);
    }


    /**
     * 释放这个类中的singleton instance.
     */
    public static void release() {
        if (resource != null) {
            resource = null;
        }
    }


    /**
     * 刷新ResourceBox中的信息
     */
    public void refresh() {
        initialize(this.baseNames, this.localeList, this.classLoader);
    }

    public String[][] getResourceArrayByType(String _type, Locale _locale) {
        ConstantsResourceBundle bundle =
                (ConstantsResourceBundle) globeResource.get(_locale.toString());

        if (bundle == null) {
            throw new IllegalArgumentException("ConstantsResource is not contains the locale " + _locale);
        }

        List resource = bundle.getResourceByType(_type);
        String[][] result = new String[resource.size()][2];
        for (int i = 0; i < resource.size(); i++) {
            Map valueMap = (Map) resource.get(i);
            result[i][0] = (String) valueMap.get(ConstantsResourceBundle.KEY_KEY);
            result[i][1] = (String) valueMap.get(ConstantsResourceBundle.KEY_VALUE);
        }
        return result;


    }

    public String[][] getResourceArrayByType(String _type) {
        return getResourceArrayByType(_type, defaultLocale);

    }

}
