package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class IdProvider implements InitializingBean, DisposableBean, BeanPostProcessor {

    private final Logger logger = Logger.getLogger(IdProvider.class);

    public String provideId( Book book ) {
      return this.hashCode() + "_" + book.hashCode();
    }

    private void initIdProvider() {
      logger.info( "IdProvider.initIdProvider" );
    }

    private void destroyIdProvider() {
      logger.info( "IdProvider.destroyIdProvider" );
    }

    private void defaultInit() {
      logger.info( "IdProvider.defaultInit" );
    }

    private void defaultDestroy() {
      logger.info( "IdProvider.defaultDestroy" );
    }

    @Override
    public void destroy() throws Exception {
      logger.info( "IdProvider.destroy" );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
      logger.info( "IdProvider.afterPropertiesSet" );
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      logger.info( "IdProvider.postProcessAfterInitialization = " + beanName );
      return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
      logger.info( "IdProvider.postProcessBeforeInitialization = " + beanName );
      return null;
    }

    @PostConstruct
    public void postConstructIdProvider() {
      logger.info( "IdProvider.postConstructIdProvider" );
    }

    @PreDestroy
    public void preDestroyIdProvider() {
        logger.info( "IdProvider.preDestroyIdProvider" );
    }
}
