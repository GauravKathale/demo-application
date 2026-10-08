package com.vodafone.gno.config.jira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class RestClientConfig {

    @Value("${jira.proxy.host:}")
    private String proxyHost;

    @Value("${jira.proxy.port:0}")
    private int proxyPort;

    @Bean
    public RestClient restClient() {
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            factory.setProxy(proxy);
            return RestClient.builder().requestFactory(factory).build();
        }
        return RestClient.create();
    }
}

