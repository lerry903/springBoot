package org.yctech.resource;

import org.yctech.util.CollectionUtil;

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
     * @param _key      resource的key.
     * @param _template 初始的、没有经过解析和合成的resource的值.
     */
    public ResourceTemplate(String _key, String _template, Locale _locale) {
        key = _key;
        template = _template;
        locale = _locale;
        noReferences = new HashSet();
        noReferences.add(key);
    }

    protected void addNoReferences(Set _noReferences) {
        CollectionUtil.add(noReferences, _noReferences);
    }

    protected Set getNoReferences() {
        return noReferences;
    }

    /**
     * 得到模板的动态生成的内容，模板中的参数部分由_props确定。如果模板中的
     * 参数在_props中没有定义，使用空字符串""替代参数部分。
     *
     * @param _props 替换参数
     *               return 返回替换过的参数
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
     * 得到模板的动态生成的内容，模板中的参数由空字符串""替代。
     */
    public String getString() {
        return getString(new Properties());
    }
}
