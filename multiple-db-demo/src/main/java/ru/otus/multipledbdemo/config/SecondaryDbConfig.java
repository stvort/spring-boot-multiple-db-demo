package ru.otus.multipledbdemo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import ru.otus.multipledbdemo.domain.secondary.ExternalUser;
import ru.otus.multipledbdemo.repositories.secondary.UserRepository;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager",
        basePackageClasses = UserRepository.class
)
public class SecondaryDbConfig {

    @Value("${secondary.datasource.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${secondary.flyway.locations}")
    private String flywayLocations;

    @Bean
    @ConfigurationProperties(prefix = "secondary.datasource")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource secondaryDataSource() {
        return secondaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = builder
                .dataSource(secondaryDataSource())
                .packages(ExternalUser.class.getPackageName())
                .persistenceUnit("secondary")
                .build();
        entityManagerFactoryBean.getJpaPropertyMap().put("hibernate.dialect", hibernateDialect);
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }

    @Bean
    public TransactionTemplate secondaryTransactionTemplate(
            @Qualifier("secondaryTransactionManager") PlatformTransactionManager secondaryTransactionManager) {
        return new TransactionTemplate(secondaryTransactionManager);
    }

    @Bean
    public Flyway secondaryFlyWay(){
        return Flyway.configure()
                .dataSource(secondaryDataSource())
                .locations(flywayLocations)
                .load();
    }

    @Bean
    public FlywayMigrationInitializer secondaryFlywayMigrationInitializer() {
        return new FlywayMigrationInitializer(secondaryFlyWay());
    }
}
