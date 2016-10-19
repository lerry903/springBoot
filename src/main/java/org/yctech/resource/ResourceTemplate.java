package org.yctech.resource;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 15:47
 */
public class ResourceTemplate {
    protected static final int NORMAL = 1;

    protected static final int LIKE_ARGUMENT = 2;
    protected static final int BEGIN_ARGUMENT = 3;
    protected static final int IN_ARGUMENT = 4;

    protected static final int LIKE_REFERENCE = 5;
    protected static final int BEGIN_REFERENCE = 6;
    protected static final int IN_REFERENCE = 7;

    //resource key
    protected String key;

    //primal resource
    protected String template;

    //Locale
    protected Locale locale;

    //forbidden references
    protected Set noReferences;

    /**
     * @param _key      resource��key.
     * @param _template ��ʼ�ġ�û�о�������ͺϳɵ�resource��ֵ.
     */
    public ResourceTemplate(String _key, String _template, Locale _locale) {
        key = _key;
        template = _template;
        locale = _locale;
        noReferences = new HashSet();
        noReferences.add(key);
    }

    protected void addNoReferences(Set _noReferences) {
        for (Iterator it = noReferences.iterator(); it.hasNext();) {
            Object item = it.next();
            _noReferences.add(item);
        }
    }

    protected Set getNoReferences() {
        return noReferences;
    }

    /**
     * �õ�ģ��Ķ�̬��ɵ����ݣ�ģ���еĲ������_propsȷ�������ģ���е�
     * ������_props��û�ж��壬ʹ�ÿ��ַ�""������֡�
     *
     * @param _props �滻����
     *               return �����滻��Ĳ���
     */
    public String getString(Properties _props) {
        StringBuffer resultBuf = new StringBuffer();
        StringBuffer propNameBuf = new StringBuffer();
        StringBuffer refNameBuf = new StringBuffer();
        char ch;
        int status = NORMAL;
        for (int i = 0; i < template.length(); ++i) {
            ch = template.charAt(i);
            switch (status) {
                case NORMAL:
                    if (ch == '$') {
                        status = LIKE_ARGUMENT;
                    } else if (ch == '&') {
                        status = LIKE_REFERENCE;
                    } else {
                        resultBuf.append(ch);
                    }
                    break;
                case LIKE_ARGUMENT:
                    if (ch == '{') {
                        status = BEGIN_ARGUMENT;
                    } else {
                        resultBuf.append('$');
                        resultBuf.append(ch);
                        status = NORMAL;
                    }
                    break;
                case LIKE_REFERENCE:
                    if (ch == '{') {
                        status = BEGIN_REFERENCE;
                    } else {
                        resultBuf.append('&');
                        resultBuf.append(ch);
                        status = NORMAL;
                    }
                    break;
                case BEGIN_ARGUMENT:
                    if (ch == '}') {
                        status = NORMAL;
                    } else {
                        propNameBuf.append(ch);
                        status = IN_ARGUMENT;
                    }
                    break;
                case BEGIN_REFERENCE:
                    if (ch == '}') {
                        status = NORMAL;
                    } else {
                        refNameBuf.append(ch);
                        status = IN_REFERENCE;
                    }
                    break;
                case IN_ARGUMENT:
                    if (ch == '}') {
                        String propValue = _props.getProperty(propNameBuf.toString(), "");

                        resultBuf.append(propValue);
                        propNameBuf.delete(0, propNameBuf.length());
                        status = NORMAL;
                    } else {
                        propNameBuf.append(ch);
                    }
                    break;
                case IN_REFERENCE:
                    if (ch == '}') {
                        String refName = refNameBuf.toString();

                        Set noReferreds = getNoReferences();
                        if (noReferreds.contains(refName)) {
                            throw new IllegalStateException("Circular reference occurs, the reference is " + refName);
                        } else {
                            String reference = "";
                            try {
                                reference = ResourceBox.getInstance().getString(refName, locale);
                                ResourceTemplate nextTemplate = new ResourceTemplate(refName, reference, locale);
                                nextTemplate.addNoReferences(noReferreds);
                                reference = nextTemplate.getString();
                            } catch (MissingResourceException exc) {
                            }
                            resultBuf.append(reference);
                            refNameBuf.delete(0, refNameBuf.length());
                            status = NORMAL;
                        }
                    } else {
                        refNameBuf.append(ch);
                    }
            }
        }
        return resultBuf.toString();
    }

    /**
     * �õ�ģ��Ķ�̬��ɵ����ݣ�ģ���еĲ����ɿ��ַ�""���
     */
    public String getString() {
        return getString(new Properties());
    }
}
