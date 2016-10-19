# project for Spring Boot
# java version 1.8
# Spring Boot version 1.3.8
# sourceEncoding UTF-8
# 系统中配置文件的使用如下
#   取constants_zh_CN.properties值使用以下方式,例如文件中为 successful.0=失败
#       GlobalConstant.getConstantResource("successful",0);
#   取message_zh_CN.properties值使用以下方式,例如文件中为 business_exception_100=操作成功!
#       ResourceBox.getInstance().getResource("business_exception_100");
#   如果是取配置文件的值，如database.properties
#       static ResourceUtil resourceUtil = new ResourceUtil("database");
#       resourceUtil.getResource("database.url");
# 另外，如果需要做国际化则需要将resource-config.properties中localeList属性的值以英文逗号分割(,)，如localeList=en_US,zh_CN,zh_TW
# 在切换语言的时候需要将ResourceConfig中DEFAULT_LOCALE值按照对应的格式赋值，格式如zh_CN，默认为zh_CN，需要修改默认值则修改resource-config.properties中defaultLocale