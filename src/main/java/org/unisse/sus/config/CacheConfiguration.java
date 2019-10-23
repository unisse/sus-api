package org.unisse.sus.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.unisse.sus.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.unisse.sus.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.unisse.sus.domain.User.class.getName());
            createCache(cm, org.unisse.sus.domain.Authority.class.getName());
            createCache(cm, org.unisse.sus.domain.User.class.getName() + ".authorities");
            createCache(cm, org.unisse.sus.domain.UnidadeSaude.class.getName());
            createCache(cm, org.unisse.sus.domain.TipoUnidadeSaude.class.getName());
            createCache(cm, org.unisse.sus.domain.SituacaoUnidadeSaude.class.getName());
            createCache(cm, org.unisse.sus.domain.Registro.class.getName());
            createCache(cm, org.unisse.sus.domain.Alteracao.class.getName());
            createCache(cm, org.unisse.sus.domain.TipoRegistro.class.getName());
            createCache(cm, org.unisse.sus.domain.Comentario.class.getName());
            createCache(cm, org.unisse.sus.domain.Importancia.class.getName());
            createCache(cm, org.unisse.sus.domain.Opiniao.class.getName());
            createCache(cm, org.unisse.sus.domain.Denuncia.class.getName());
            createCache(cm, org.unisse.sus.domain.SolicitacaoAtualizacao.class.getName());
            createCache(cm, org.unisse.sus.domain.SolicitacaoCriacao.class.getName());
            createCache(cm, org.unisse.sus.domain.TipoDenuncia.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
