Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-01-22 14:42:05 [main] ERROR o.s.boot.SpringApplication  - userId= - correlationId= - Application run failed
org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'org.apache.geode.cache.client.ClientCache' available: expected single matching bean but found 2: gemfireCache,clientCacheFactoryBean
