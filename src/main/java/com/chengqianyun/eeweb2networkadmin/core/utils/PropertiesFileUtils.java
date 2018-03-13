package com.chengqianyun.eeweb2networkadmin.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesFileUtils extends PropertyPlaceholderConfigurer {

    private static Map<String, String> ctxPropertiesMap;
    private ConfigurableListableBeanFactory beanFactory;
    private Properties props;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        this.beanFactory = beanFactory;
        this.props = props;
        super.processProperties(beanFactory, props);
        ctxPropertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    /**
     * 根据key获取属性值
     * @param key
     * @return
     */
    public static String getContextProperty(String key) {
        return ctxPropertiesMap.get(key);
    }

    /**
     * 获取int类型的值
     * @param key
     * @return
     */
    public static int getIntValue(String key){
        return Integer.parseInt(ctxPropertiesMap.get(key));
    }
}
