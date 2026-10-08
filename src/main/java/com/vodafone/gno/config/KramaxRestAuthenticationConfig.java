package com.vodafone.gno.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vodafone.kramax.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;

@Configuration
public class KramaxRestAuthenticationConfig {


    @Bean

    public FilterRegistrationBean<ProcessEngineAuthenticationFilter>

    processEngineAuthenticationFilter() {


        FilterRegistrationBean<ProcessEngineAuthenticationFilter> registration =

                new FilterRegistrationBean<>();


        registration.setFilter(

                new ProcessEngineAuthenticationFilter()

        );


        registration.addUrlPatterns("/engine-rest/*");


        registration.addInitParameter(

                "authentication-provider",

                "org.vodafone.kramax.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider"

        );


        return registration;

    }

}
