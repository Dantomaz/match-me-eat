package com.matchmeeat.configuration;

import com.matchmeeat.utils.AppContext;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class PersistenceConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(AppContext.getUsername());
    }
}
