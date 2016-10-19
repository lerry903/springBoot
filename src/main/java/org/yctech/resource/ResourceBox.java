package org.yctech.resource;

import java.util.*;

/**
 * 初始化一个ResourceBox实例，而且这个实例只能被初始化一次。初始化的时候允许
 * 定义一组Local对象，ResourceBox将根据Local对象选择Resource文件并初始化，如果
 * 不设定，ResourceBox将根据junco.propertie中resource_language的值来获取默认的
 * Local。
 * 在第一次调用getResource方法前，必须使用initialize方法初始化。通常，我们使用
 * 一个空的properties文件作为所有property resource的容器，然后在这个properties
 * 文件对应的relationships文件中描述它所包含的具体的properties文件。
 * property resource文件可以使用参数和引用。参数的符号是${argument}，argument表
 * 示参数的名称，我们可以在getResource方法中传递Properties对象，在Properties对
 * 象中指定augument参数的值，这样就可以动态生成resource。
 * 引用的符号是&{refered_key}，refered_key表示被引用的resource的key，在getReso
 * urce方法中，会引入被引用的resource。
 * <p/>
 * <p/>
 * 　　1）仍然使用原有的树状结构存储Resource文件，只是ResourceBox可以根据初始化定义的Local列表建立多个这样的树，树的结构采用同一套Relationship文件定义，相互区分根据文件名的Local后缀。
 * 2）仍然支持原先的Parameter机制，在Resource内部和Resource获取时进行替换
 * 3）ResourceBox提供新的方法支持根据指定Local获取Resource。但是需要对Junco的Action，Jsp，TagLib作一定的封装，做到多语言化对一般开发人员透明。
 * 4）Properties文件一律采用UTF-8方式存储。
 *
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 15:44
 */
public class ResourceBox {

    /**
     * 缺省的Locale
     */
    private Locale defaultLocale = null;

    /**
     * 内置的Resource Box对象
     */
    protected static ResourceBox box = null;


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
    private ResourceBox() {
        init();

    }


    /**
     * 缺省Locale
     *
     * @return
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public List getLocaleList() {
        return localeList;
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
     * 获取ResourceBox的实例
     *
     * @return ResourceBox的唯一实例
     */
    public synchronized static ResourceBox getInstance() {
        if (box == null) {
            box = new ResourceBox();
        }
        return box;
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

        String baseNameStr = ResourceConfig.RESOURCEBOX_FILES;
        StringTokenizer st2 = new StringTokenizer(baseNameStr, ";");
        baseNames = new String[st2.countTokens()];
        int i = 0;
        while (st2.hasMoreTokens()) {
            baseNames[i++] = st2.nextToken().trim();
        }

        initialize(baseNames, localeList, null);
    }

    /**
     * 初始化ResourceBox，使用指定的baseName、locale、classLoader读取property
     * resource文件。
     * 如果没有继承关系的文件中重复定义了resource时，错误信息会被记录下来，
     * 程序依旧运行。
     *
     * @param _baseName    resource文件的基本名字
     * @param _localeList  需要载入的Locale的列表，里面包含的Item是一个Locale对象
     * @param _classLoader 使用的ClassLoader
     * @throws java.util.MissingResourceException 指定的properties文件或被它继承的文件找不到时抛出
     */
    private void initialize(String _baseName[],
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

            ResourceBoxBundle boxBundle = new ResourceBoxBundle(baseNames, locale, classLoader);

            globeResource.put(locale.toString(), boxBundle);
        }
    }

    /**
     * 获取以数字为key的resource，比如messsage格式为 " The table ${0} not exists"
     * String msg = ResourceBox.getInstance().getResource("key","sales_order_table");
     * 则返回值为" The table sales_order_table not exists"
     *
     * @param _key
     * @param param0
     * @return
     */
    public String getResource(String _key, String param0) {
        Properties property = new Properties();
        property.setProperty("0", param0);
        return getResource(_key, property);
    }

    /**
     * 获取以数字为key的resource，比如messsage格式为 " The table ${0} not exists"
     * String msg = ResourceBox.getInstance().getResource("key","sales_order_table",locale);
     * 则返回值为" The table sales_order_table not exists"
     *
     * @param _key
     * @param param0
     * @param locale
     * @return
     */

    public String getResource(String _key, String param0, Locale locale) {
        Properties property = new Properties();
        property.setProperty("0", param0);
        return getResource(_key, property, locale);
    }

    /**
     * 获取以数字为key的resource，比如messsage格式为 " The table ${0} can't be updated by user ${1}"
     * String msg = ResourceBox.getInstance().getResource("key","sales_order_table","scott");
     * 则返回值为" The table sales_order_table can't be updated by user scott"
     *
     * @param _key
     * @param param0
     * @param param1
     * @return
     */
    public String getResource(String _key, String param0, String param1) {
        Properties property = new Properties();
        property.setProperty("0", param0);
        property.setProperty("1", param1);
        return getResource(_key, property);
    }

    public String getResource(String _key, String param0, String param1, String param2) {
        Properties property = new Properties();
        property.setProperty("0", param0);
        property.setProperty("1", param1);
        property.setProperty("2", param2);
        return getResource(_key, property);
    }

    /**
     * 获取以数字为key的resource，比如messsage格式为 " The table ${0} can't be updated by user ${1}"
     * String msg = ResourceBox.getInstance().getResource("key","sales_order_table","scott", locale);
     * 则返回值为" The table sales_order_table can't be updated by user scott"
     *
     * @param _key
     * @param param0
     * @param param1
     * @param locale
     * @return
     */
    public String getResource(String _key, String param0, String param1, Locale locale) {
        Properties property = new Properties();
        property.setProperty("0", param0);
        property.setProperty("1", param1);
        return getResource(_key, property, locale);
    }


    public String getResource(String _key, String param0, String param1, String param2, Locale locale) {
        Properties property = new Properties();
        property.setProperty("0", param0);
        property.setProperty("1", param1);
        property.setProperty("2", param2);
        return getResource(_key, property, locale);
    }


    /**
     * 根据_key得到相应的resource。
     *
     * @param _key 根据指定的键值获取缺省的Resource
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getResource(String _key)
            throws MissingResourceException {
        return getResource(_key, new Properties());
    }


    /**
     * 根据_key和_arguments得到相应的resource。
     *
     * @param _key       根据指定的键值获取缺省的Resource
     * @param _arguments 用于替换参数的属性集合
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getResource(String _key, Properties _arguments)
            throws MissingResourceException {
        return getResource(_key, _arguments, defaultLocale);
    }


    /**
     * 根据_key和_arguments得到相应的resource。
     *
     * @param _key    根据指定的键值获取缺省的Resource
     * @param _locale 指定的Resource的Locale
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getResource(String _key, Locale _locale)
            throws MissingResourceException {
        if (_locale == null) {
            return getResource(_key, new Properties(), defaultLocale);
        } else {
            return getResource(_key, new Properties(), _locale);
        }
    }


    /**
     * 根据_key和_arguments得到相应的resource。
     *
     * @param _key       根据指定的键值获取缺省的Resource
     * @param _arguments 用于替换参数的属性集合
     * @param _locale    指定的Resource的Locale
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getResource(String _key, Properties _arguments, Locale _locale)
            throws MissingResourceException {


        if (_locale == null) {
            _locale = defaultLocale;
        }

//        String resource = _locale == null ? getString(_key, defaultLocale) : getString(_key, _locale);
        String resource = getString(_key, _locale);

        if (resource == null) {
            return "";
        }

//        ResourceTemplate template = _locale == null ? new ResourceTemplate(_key, resource, defaultLocale) : new ResourceTemplate(_key, resource, _locale);
        ResourceTemplate template = new ResourceTemplate(_key, resource, _locale);
        return template.getString(_arguments);
    }


    /**
     * 根据_key得到初始的、没有经过解析和合成的resource.
     *
     * @param _key    根据指定的键值获取缺省的Resource
     * @param _locale 指定的Resource的Locale
     * @return 返回初始的、没有经过解析和合成的resource
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getString(String _key, Locale _locale)
            throws MissingResourceException {
        ResourceBoxBundle boxBundle =
                (ResourceBoxBundle) globeResource.get(_locale.toString());

        if (boxBundle == null) {
            throw new IllegalArgumentException("ResourceBox does not contains the locale " + _locale);
        }

        return boxBundle.getString(_key);
    }

    /**
     * 根据errorcode得到相应的resource。
     *
     * @param errorCode 根据指定的键值获取缺省的Message
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getExceptionMessage(String errorCode)
            throws MissingResourceException {
        return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties());
    }

    /**
     * 根据errorcode得到相应的resource。
     *
     * @param errorCode 根据指定的键值获取缺省的Message
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getExceptionMessage(String errorCode, Locale _locale)
            throws MissingResourceException {
        if (_locale == null) {
            return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties(), defaultLocale);
        } else {
            return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties(), _locale);
        }
    }

    /**
     * 根据errorcode得到相应的resource。
     *
     * @param errorCode 根据指定的键值获取缺省的Message
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getExceptionMessage(int errorCode)
            throws MissingResourceException {
        return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties());
    }

    /**
     * 根据errorcode得到相应的resource。
     *
     * @param errorCode 根据指定的键值获取缺省的Message
     * @return 返回获取的Resource，如果不存在返回空字符串。
     * @throws MissingResourceException 找不到指定的resource时，抛出MissingResourceException
     */
    public String getExceptionMessage(int errorCode, Locale _locale)
            throws MissingResourceException {
        if (_locale == null) {
            return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties(), defaultLocale);
        } else {
            return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties(), _locale);
        }
    }

    /**
     * 释放这个类中的singleton instance.
     */
    public static void release() {
        if (box != null) {
            box = null;
        }
    }


    /**
     * 刷新ResourceBox中的信息
     */
    public void refresh() {
        initialize(this.baseNames, this.localeList, this.classLoader);
    }
}
