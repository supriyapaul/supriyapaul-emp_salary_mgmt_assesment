package com.proj.test.salarymanagement.factory;

import com.proj.test.salarymanagement.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidationFactory {

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private Map<String,Class> mapper;

    public ValidationService getService(String type){
        switch (type){
            case "user":
                this.registerBeanDefinition("user_validation","user");
                return (ValidationService) beanFactory.getBean("user_validation");
            case "search":
                this.registerBeanDefinition("search_validation","search");
                return (ValidationService) beanFactory.getBean("search_validation");
        }
        return null;
    }

    private void registerBeanDefinition(String name,String type){
        if(!beanFactory.containsBean(name)) {
            BeanDefinitionBuilder b =
                    BeanDefinitionBuilder.rootBeanDefinition(mapper.get(type));
            beanFactory.registerBeanDefinition(name, b.getBeanDefinition());
        }
    }
}
