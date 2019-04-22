package com.z.h2jpa.configuration

import com.z.h2jpa.utils.AuditorAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
class JpaAuditing {
    @Bean fun auditorAwareImpl() = AuditorAwareImpl()
}