Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-01-22 14:42:05 [main] ERROR o.s.boot.SpringApplication  - userId= - correlationId= - Application run failed
org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'org.apache.geode.cache.client.ClientCache' available: expected single matching bean but found 2: gemfireCache,clientCacheFactoryBean

  @Configuration
@ClientCacheApplication
@EnableEntityDefinedRegions(basePackages = ENTITY_DEFINED_REGIONS )
@EnableGemfireRepositories(basePackages = CACHE_REPOSITORIES )
public class GemFireConfiguration  {
}


@Configuration
public class GeodeConfig {
    @Bean
    public PdxSerializer pdxSerializer(){
        return new ReflectionBasedAutoSerializer("com.epay.admin.*");
    }

    @Bean(name = "customClientCache")
    public ClientCacheFactoryBean clientCacheFactoryBean(){
        ClientCacheFactoryBean cacheFactoryBean = new ClientCacheFactoryBean();
        cacheFactoryBean.setPdxSerializer(pdxSerializer());
        cacheFactoryBean.setPdxReadSerialized(true);
        return  cacheFactoryBean;
    }
}
