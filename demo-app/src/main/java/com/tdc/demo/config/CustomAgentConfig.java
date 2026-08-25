package com.tdc.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomAgentConfig {

    @Value("${custom.agent.enabled:false}")
    private boolean customAgentEnabled;

    @PostConstruct
    public void configureAgent() {
        System.setProperty(
                "custom.agent.enabled",
                Boolean.toString(customAgentEnabled)
        );

        System.out.println(
                "[CustomAgentConfig] custom.agent.enabled=" + customAgentEnabled
        );
    }
}
