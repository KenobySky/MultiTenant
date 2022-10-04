package br.company.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DataSourceConfiguration {

    private static HikariDataSource loginDataSource;

    public DataSourceConfiguration() {

    }

    @Bean(name = "loginDataSource")
    public DataSource loginDataSource(Environment env) {
        String url = env.getRequiredProperty("spring.datasource.url");
        String classname = env.getRequiredProperty("spring.datasource.driver-class-name");

        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        if (loginDataSource == null) {
            loginDataSource = new HikariDataSource();
            //

            //
            loginDataSource.setDriverClassName(classname);
            loginDataSource.setUsername(username);
            loginDataSource.setPassword(password);
            loginDataSource.setJdbcUrl(url);
            loginDataSource.setMaximumPoolSize(6);
            loginDataSource.setPoolName("Hikari-Pool-Login");
        }

        return loginDataSource;
    }

    @Bean(name = "companyDependentDataSource")
    public DataSource companyDependentDataSource(Environment env) {
        UserSchemaAwareRoutingDataSource ds = new UserSchemaAwareRoutingDataSource();
        if (ds.determineTargetDataSource() != null) {
            System.out.println("\nCurrent datasource:" + ds);
            System.out.println("\nCurrent datasource:" + ds.determineTargetDataSource());
        }
        return ds; // Autowiring is done afterwards by Spring
    }
}
