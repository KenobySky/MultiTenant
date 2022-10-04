package br.company.config;

import br.company.repository.login.TunnelRepository;
import br.company.tools.MySettings;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    
    @Bean
    public Flyway flyway(@Qualifier("loginDataSource") DataSource dataSource) {
        
        Flyway flyway = Flyway.configure()
                .cleanOnValidationError(true)
                .locations("db/migration/default")
                .dataSource(dataSource)
                .schemas(MySettings.DEFAULT_SCHEMA)
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    CommandLineRunner 
        commandLineRunner(TunnelRepository repository,@Qualifier("loginDataSource") DataSource dataSource) {
        return args -> {
            repository.findAll().forEach(tunnel -> {

                try {
                    if (tunnel != null && tunnel.getDb_schema() != null) {

                        String schema = tunnel.getDb_schema();

                        Flyway flyway = Flyway.configure()
                                .cleanOnValidationError(true)
                                .locations("db/migration/tenants")
                                .dataSource(dataSource)
                                .schemas(schema)
                                .load();
                        flyway.migrate();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        };
    }
}
