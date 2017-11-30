//package com.snilov.bank.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
//
//import java.util.List;
//
//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class MvcConfig implements WebMvcConfigurer {
//
//    private final RequestMappingHandlerAdapter repositoryExporterHandlerAdapter;
//
//    @Autowired
//    public MvcConfig(@Qualifier("repositoryExporterHandlerAdapter") RequestMappingHandlerAdapter repositoryExporterHandlerAdapter) {
//        this.repositoryExporterHandlerAdapter = repositoryExporterHandlerAdapter;
//    }
//
//    @Override
//    public void addArgumentResolvers(
//            List<HandlerMethodArgumentResolver> argumentResolvers) {
//        List<HandlerMethodArgumentResolver> customArgumentResolvers = repositoryExporterHandlerAdapter.getCustomArgumentResolvers();
//        assert customArgumentResolvers != null;
//        argumentResolvers.addAll(customArgumentResolvers);
//    }
//
//}
