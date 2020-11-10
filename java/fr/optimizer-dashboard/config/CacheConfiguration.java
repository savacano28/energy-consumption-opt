package fr.ifpen.synergreen.config;

import fr.ifpen.synergreen.domain.HistorianSource;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        log.debug("Initializing Cache configuration");
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        log.debug("Customizing CacheManager");
        return cm -> {
            cm.createCache(fr.ifpen.synergreen.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.MeasurementSource.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxNode.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BatteryModelSource.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BasicSource.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.CSVSource.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.PVModelSource.class.getName(), jcacheConfiguration);
            cm.createCache(HistorianSource.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".monthsHighSeasons", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".monthsOffPeaks", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".highHours", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".lowHours", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".peakHours", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".fluxTopologies", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyProvider.class.getName() + ".fluxNodes", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergySite.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergySite.class.getName() + ".fluxTopologies", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyElement.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyElement.class.getName() + ".measurementSources", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyElement.class.getName() + ".parameters", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyManagementSystem.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.EnergyManagementSystem.class.getName() + ".energySites", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxGroup.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxGroup.class.getName() + ".children", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxTopology.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxTopology.class.getName() + ".optimizations", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxTopology.class.getName() + ".children", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.Parameter.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.MeasuredData.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BatteryManager.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BatteryManagementInstruction.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BatteryManagementRun.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BatteryManager.class.getName() + ".jobs", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.BatteryManagementRun.class.getName() + ".instructions", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.SourceDescriptor.class.getName(), jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.FluxTopology.class.getName() + ".invoices", jcacheConfiguration);
            cm.createCache(fr.ifpen.synergreen.domain.Invoice.class.getName(), jcacheConfiguration);


            // jhipster-needle-ehcache-add-entry
        };
    }
}
