package org.yctech.resource;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/19
 * Time: 15:00
 */
public interface ResourceConfig {

    public ResourceBundle config = ResourceBundle.getBundle("resource-config");

    public String DEFAULT_LOCALE = config.getString("defaultLocale");

    public String EXCEPTION_MESSAGE_PREFIX = config.getString("exception_message_prefix");

    public boolean VALUE_NEED_ENCODE = new Boolean(config.getString("stringEncode")).booleanValue();

    public String SOURCE_ENCODE = config.getString("sourceEncode");

    public String TARGET_ENCODE = config.getString("targetEncode");

    public String LOCALE_LIST = config.getString("localeList");

    public String RESOURCEBOX_FILES = config.getString("resourcebox_files");

    public String CONSTANTS_FILES = config.getString("constants_files");

}
