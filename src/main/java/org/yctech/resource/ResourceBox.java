package org.yctech.resource;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 15:44
 */
public class ResourceBox {

    /**
     * ȱʡ��Locale
     */
    private Locale defaultLocale = null;

    /**
     * ���õ�Resource Box����
     */
    protected static ResourceBox box = null;


    /**
     * ��������Locale��Resource��Ϣ������
     */
    private Map globeResource;


    /**
     * resource�Ļ���
     */
    private String[] baseNames;


    /**
     * Resource��Locale�б�
     */
    private List localeList;


    /**
     * ָ����Resource��ClassLoader
     */
    private ClassLoader classLoader;


    /**
     * ˽�еĹ�������
     */
    private ResourceBox() {
        init();

    }


    /**
     * ȱʡLocale
     * @return
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public List getLocaleList() {
        return localeList;
    }

    /**
     * ����ȱʡLocale
     * @param defaultLocale
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }


    /**
     * ��ȡResourceBox��ʵ��
     *
     * @return ResourceBox��Ψһʵ��
     */
    public synchronized static ResourceBox getInstance() {
        if (box == null) {
            box = new ResourceBox();
        }
        return box;
    }

    protected void init(){

        String defaultLocaleStr = ResourceConfig.DEFAULT_LOCALE;
        StringTokenizer st = new StringTokenizer(defaultLocaleStr, "_");

        defaultLocale = new Locale(st.nextToken(), st.nextToken());

        String[] langStrs = ResourceConfig.LOCALE_LIST.split(",");
        ArrayList localeList = new ArrayList();
        for (int i = 0; i < langStrs.length; i++) {
            String[] sLocale = langStrs[i].split("_");
            localeList.add(new Locale(sLocale[0],sLocale[1]));
        }

        String baseNameStr = ResourceConfig.RESOURCEBOX_FILES;
        StringTokenizer st2  = new StringTokenizer(baseNameStr,";");
        baseNames = new String[st2.countTokens()];
        int i = 0;
        while(st2.hasMoreTokens()) {
            baseNames[i++] = st2.nextToken().trim();
        }

        initialize(baseNames,localeList,null);
    }


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
     * ��ȡ������Ϊkey��resource������messsage��ʽΪ " The table ${0} not exists"
     *  String msg = ResourceBox.getInstance().getResource("key","sales_order_table");
     *  �򷵻�ֵΪ" The table sales_order_table not exists"
     * @param _key
     * @param param0
     * @return
     */
    public String getResource(String _key,String param0) {
        Properties property = new Properties();
        property.setProperty("0",param0);
        return getResource(_key, property);
    }

    /**
     * ��ȡ������Ϊkey��resource������messsage��ʽΪ " The table ${0} not exists"
     *  String msg = ResourceBox.getInstance().getResource("key","sales_order_table",locale);
     *  �򷵻�ֵΪ" The table sales_order_table not exists"
     * @param _key
     * @param param0
     * @param locale
     * @return
     */

    public String getResource(String _key,String param0,Locale locale) {
        Properties property = new Properties();
        property.setProperty("0",param0);
        return getResource(_key, property,locale);
    }

    /**
     * ��ȡ������Ϊkey��resource������messsage��ʽΪ " The table ${0} can't be updated by user ${1}"
     *  String msg = ResourceBox.getInstance().getResource("key","sales_order_table","scott");
     *  �򷵻�ֵΪ" The table sales_order_table can't be updated by user scott"
     * @param _key
     * @param param0
     * @param param1
     * @return
     */
    public String getResource(String _key,String param0,String param1) {
        Properties property = new Properties();
        property.setProperty("0",param0);
        property.setProperty("1",param1);
        return getResource(_key, property);
    }

    public String getResource(String _key,String param0,String param1,String param2) {
        Properties property = new Properties();
        property.setProperty("0",param0);
        property.setProperty("1",param1);
        property.setProperty("2",param2);
        return getResource(_key, property);
    }

    /**
     * ��ȡ������Ϊkey��resource������messsage��ʽΪ " The table ${0} can't be updated by user ${1}"
     *  String msg = ResourceBox.getInstance().getResource("key","sales_order_table","scott", locale);
     *  �򷵻�ֵΪ" The table sales_order_table can't be updated by user scott"
     * @param _key
     * @param param0
     * @param param1
     * @param locale
     * @return
     */
    public String getResource(String _key,String param0,String param1,Locale locale) {
        Properties property = new Properties();
        property.setProperty("0",param0);
        property.setProperty("1",param1);
        return getResource(_key, property,locale);
    }


    public String getResource(String _key,String param0,String param1,String param2,Locale locale) {
        Properties property = new Properties();
        property.setProperty("0",param0);
        property.setProperty("1",param1);
        property.setProperty("2",param2);
        return getResource(_key, property,locale);
    }


    /**
     * ���_key�õ���Ӧ��resource��
     *
     * @param _key ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public String getResource(String _key)
            throws MissingResourceException {
        return getResource(_key, new Properties());
    }


    /**
     * ���_key��_arguments�õ���Ӧ��resource��
     *
     * @param _key       ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @param _arguments �����滻��������Լ���
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public String getResource(String _key, Properties _arguments)
            throws MissingResourceException {
        return getResource(_key, _arguments, defaultLocale);
    }


    /**
     * ���_key��_arguments�õ���Ӧ��resource��
     *
     * @param _key    ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @param _locale ָ����Resource��Locale
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
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
     * ���_key��_arguments�õ���Ӧ��resource��
     *
     * @param _key       ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @param _arguments �����滻��������Լ���
     * @param _locale    ָ����Resource��Locale
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public String getResource(String _key, Properties _arguments, Locale _locale)
            throws MissingResourceException {


        if(_locale == null) {
            _locale = defaultLocale;
        }

//        String resource = _locale == null ? getString(_key, defaultLocale) : getString(_key, _locale);
        String resource = getString(_key, _locale);

        if (resource == null) {
            return "";
        }

//        ResourceTemplate template = _locale == null ? new ResourceTemplate(_key, resource, defaultLocale) : new ResourceTemplate(_key, resource, _locale);
        ResourceTemplate template =  new ResourceTemplate(_key, resource, _locale);
        return template.getString(_arguments);
    }


    /**
     * ���_key�õ���ʼ�ġ�û�о�������ͺϳɵ�resource.
     *
     * @param _key    ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @param _locale ָ����Resource��Locale
     * @return ���س�ʼ�ġ�û�о�������ͺϳɵ�resource
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
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
     * ���errorcode�õ���Ӧ��resource��
     *
     * @param errorCode ���ָ���ļ�ֵ��ȡȱʡ��Message
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public String getExceptionMessage(String errorCode)
            throws MissingResourceException {
        return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties());
    }

    /**
     * ���errorcode�õ���Ӧ��resource��
     *
     * @param errorCode ���ָ���ļ�ֵ��ȡȱʡ��Message
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
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
     * ���errorcode�õ���Ӧ��resource��
     *
     * @param errorCode ���ָ���ļ�ֵ��ȡȱʡ��Message
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public String getExceptionMessage(int errorCode)
            throws MissingResourceException {
        return getResource(ResourceConfig.EXCEPTION_MESSAGE_PREFIX + errorCode, new Properties());
    }

    /**
     * ���errorcode�õ���Ӧ��resource��
     *
     * @param errorCode ���ָ���ļ�ֵ��ȡȱʡ��Message
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws MissingResourceException �Ҳ���ָ����resourceʱ���׳�MissingResourceException
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
     * �ͷ�������е�singleton instance.
     */
    public static void release() {
        if (box != null) {
            box = null;
        }
    }


    /**
     * ˢ��ResourceBox�е���Ϣ
     */
    public void refresh() {
        initialize(this.baseNames, this.localeList, this.classLoader);
    }

}
