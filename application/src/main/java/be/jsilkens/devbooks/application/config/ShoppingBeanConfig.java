package be.jsilkens.devbooks.application.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

@Configuration(proxyBeanMethods = false)
public class ShoppingBeanConfig {
    @Bean
    static BeanFactoryPostProcessor shoppingBeanFactoryPostProcessor(ApplicationContext beanRegistry) {
        return beanFactory ->
                devbooksApplicationContext(
                        (BeanDefinitionRegistry) ((GenericApplicationContext) beanRegistry)
                                .getBeanFactory());
    }

    static void devbooksApplicationContext(BeanDefinitionRegistry beanRegistry) {
        ClassPathBeanDefinitionScanner beanDefinitionScanner = new ClassPathBeanDefinitionScanner(beanRegistry);
        beanDefinitionScanner.addIncludeFilter(addUseCaseFilter());
        beanDefinitionScanner.scan(
                "be.jsilkens.devbooks.shopping.usecase"
        );
    }

    static TypeFilter addUseCaseFilter() {
        return (MetadataReader mr, MetadataReaderFactory mrf) -> mr.getClassMetadata()
                .getClassName()
                .endsWith("UseCase");
    }
}
