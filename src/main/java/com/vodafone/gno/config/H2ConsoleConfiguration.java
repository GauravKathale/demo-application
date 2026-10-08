/*
package com.vodafone.gno.config;

import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
public class H2ConsoleConfiguration implements WebMvcConfigurer {

    @Bean
    public ServletRegistrationBean<Servlet> h2ConsoleServletRegistration(
            @Value("${spring.h2.console.path:/h2-console}") String configuredPath) {

        String path = normalizePath(configuredPath);
        ServletRegistrationBean<Servlet> registration =
                new ServletRegistrationBean<>(createH2ConsoleServlet(), path + "/*");
        registration.setName("H2Console");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/h2-console", "/h2-console/");
    }

    private Servlet createH2ConsoleServlet() {
        for (String className : new String[]{
                "org.h2.server.web.JakartaWebServlet",
                "org.h2.server.web.WebServlet"
        }) {
            try {
                Class<?> servletClass = Class.forName(className);
                return (Servlet) servletClass.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException ignored) {
                // Try the next known H2 servlet class name.
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to initialize H2 console servlet: " + className, ex);
            }
        }

        throw new IllegalStateException("No compatible H2 console servlet class found on the classpath.");
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank() || "/".equals(path)) {
            return "/h2-console";
        }
        return path.startsWith("/") ? path : "/" + path;
    }
}


*/
