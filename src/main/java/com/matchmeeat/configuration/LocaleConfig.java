package com.matchmeeat.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @PostConstruct
    public void setDefaultLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }
}
