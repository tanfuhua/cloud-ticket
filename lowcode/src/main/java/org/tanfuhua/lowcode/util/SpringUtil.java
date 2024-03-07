package org.tanfuhua.lowcode.util;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext =
                Optional.of(applicationContext).orElseThrow(() -> new RuntimeException("spring start error"));
    }

    /**
     * 获取应用上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象工厂
     */
    public static DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return (DefaultListableBeanFactory) ((AnnotationConfigApplicationContext) applicationContext).getBeanFactory();
    }

    /**
     * 根据对象名字获取对象
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 根据类型获取对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据名字和类型获取对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}

