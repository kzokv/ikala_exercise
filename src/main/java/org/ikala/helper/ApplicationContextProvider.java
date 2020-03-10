package org.ikala.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Wrapper to always return a reference to the Spring Application
 * Context from within non-Spring enabled beans. Unlike Spring MVC's
 * WebApplicationContextUtils
 * we do not need a reference to the Servlet context for this. All we need is
 * for this bean to be initialized during application startup.
 */

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * @return an object of ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * This method is basically automatically executed once a brand new ApplicationContext is instantiated
     *
     * @param ac set ApplicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

    /**
     * This is about the same as context.getBean("beanName"), except it has its
     * own static handle to the Spring context, so calling this method statically
     * will give access to the beans by name in the Spring application context.
     * As in the context.getBean("beanName") call, the caller must cast to the
     * appropriate target class. If the bean does not exist, then a Runtime error
     * will be thrown.
     * @param beanName bean name
     * @param beanCls Class of the beanName
     * @param <T> return the generic type of the given class
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> beanCls) {
        return context.getBean(beanName, beanCls);
    }
}
