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
     * ȱʡ��Locale
     */
    private Locale defaultLocale = null;

    /**
     * ���õ�ConstantsResource����
     */
    protected static ConstantsResource resource = null;


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
    private ConstantsResource() {
        init();
    }

    /**
     * ��ȡConstantsResource��ʵ��
     *
     * @return ConstantsResource��Ψһʵ��
     */
    public synchronized static ConstantsResource getInstance() {
        if (resource == null) {
            resource = new ConstantsResource();
        }
        return resource;
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

//        ResourceBox.getInstance().initialize("resourcebox",localeList);


        String constantsBasename = ResourceConfig.CONSTANTS_FILES;
        StringTokenizer st2  = new StringTokenizer(constantsBasename,";");
        baseNames = new String[st2.countTokens()];
        int i = 0;
        while(st2.hasMoreTokens()) {
            baseNames[i++] = st2.nextToken().trim();
        }
        initialize(baseNames,localeList,null);

    }

    /**
     * ��ʼ��ConstantsResource��ʹ��ָ����baseName��locale��classLoader��ȡproperty
     * resource�ļ���
     * ���û�м̳й�ϵ���ļ����ظ�������resourceʱ��������Ϣ�ᱻ��¼������
     * �����������С�
     *
     * @param _baseName    resource�ļ��Ļ�����
     * @param _localeList  ��Ҫ�����Locale���б�������Item��һ��Locale����
     * @param _classLoader ʹ�õ�ClassLoader
     * @throws java.util.MissingResourceException
     *          ָ����properties�ļ�����̳е��ļ��Ҳ���ʱ�׳�
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
     * ȡȱʡLocale
     * @return
     */

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * ����ȱʡLocale

     * @param defaultLocale
     */
    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * ���_key�õ���Ӧ��resource��
     *
     * @param _key  ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @param _type Constants ������
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws java.util.MissingResourceException
     *          �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public String getResource(String _type, String _key)
            throws MissingResourceException {
        return getResource(_type, _key, defaultLocale);
    }


    /**
     * ���_key��_arguments�õ���Ӧ��resource��
     *
     * @param _type Constants������
     * @return ���ػ�ȡ��Resource List�������ڷ��ؿ��ַ�
     * @throws java.util.MissingResourceException
     *          �Ҳ���ָ����resourceʱ���׳�MissingResourceException
     */
    public List getResourceByType(String _type)
            throws MissingResourceException {
        return getResourceByType(_type, defaultLocale);
    }


    /**
     * ���_key�õ���Ӧ��resource��
     *
     * @param _key    ���ָ���ļ�ֵ��ȡȱʡ��Resource
     * @param _type   Constants ������
     * @param _locale ָ����������Ϣ
     * @return ���ػ�ȡ��Resource�������ڷ��ؿ��ַ�
     * @throws java.util.MissingResourceException
     *          �Ҳ���ָ����resourceʱ���׳�MissingResourceException
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
     * ���_key��_arguments�õ���Ӧ��resource��
     *
     * @param _type   Constants������
     * @param _locale ָ����������Ϣ
     * @return ���ػ�ȡ��Resource List�������ڷ��ؿ��ַ�
     * @throws java.util.MissingResourceException
     *          �Ҳ���ָ����resourceʱ���׳�MissingResourceException
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
     * �ͷ�������е�singleton instance.
     */
    public static void release() {
        if (resource != null) {
            resource = null;
        }
    }

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
        return getResourceArrayByType(_type,defaultLocale);

    }

}
