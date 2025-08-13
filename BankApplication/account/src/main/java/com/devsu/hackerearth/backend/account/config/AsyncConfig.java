package com.devsu.hackerearth.backend.account.config;

import java.time.Duration;
import java.util.concurrent.Executor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

// Configuración
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder b) {
        return b.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    // (Opcional) Pool para @Async
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // numero minimo de hilos activos
        executor.setMaxPoolSize(8); // Numero maximo de hilos simultaneos
        executor.setQueueCapacity(100); // Capacidad de la cola de espera
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true); // Esperar tareas al apagar
        executor.setAwaitTerminationSeconds(10); // Tiempo de espera
        executor.initialize();
        return executor;
    }
}