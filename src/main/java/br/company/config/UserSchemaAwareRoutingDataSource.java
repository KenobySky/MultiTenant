package br.company.config;

import br.company.models.login.User;
import br.company.tools.MySettings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.AbstractDataSource;

public class UserSchemaAwareRoutingDataSource extends AbstractDataSource {

    @Autowired
    private UserProvider usuarioProvider;

    /**
     * This is the initial data source.
     */
    @Autowired
    @Qualifier(value = "loginDataSource")
    private DataSource loginDataSource;

    /**
     * Variable representing the environment in which the current application is
     * running.
     */
    @Autowired
    private Environment env;

    /**
     * A semi-persistent mapping from schemas to dataSources.
     */
    private LoadingCache<String, DataSource> dataSources = createCache();

    public UserSchemaAwareRoutingDataSource() {

    }

    /**
     * Creates the cache.
     *
     * @return
     */
    private LoadingCache<String, DataSource> createCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(20)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, DataSource>() {
                    public DataSource load(String key) throws Exception {
                        return buildDataSourceForSchema(key);
                    }
                });
    }

    private DataSource buildDataSourceForSchema(String schema) {
        String url = MySettings.DEFAULT_SCHEMA + schema;
        //url = url + "?zeroDateTimeBehavior=convertToNull";

        String username = env.getRequiredProperty("spring.datasource.username");
        String password = env.getRequiredProperty("spring.datasource.password");
        String driverClassName = env.getRequiredProperty("spring.datasource.driver-class-name");

        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setJdbcUrl(url);
        ds.setMaximumPoolSize(6);
        ds.setPoolName("Hikari-Schema-" + schema);

        //
        return ds;
    }

    /**
     * Gets the Schema from the Cache, or build one if it doesnt exist.
     *
     * @return
     */
    protected DataSource determineTargetDataSource() {
        try {
            //logger.info("------> determineTargetDataSource........ ");

            String db_schema = determineTargetSchema();
            if (db_schema.equalsIgnoreCase(MySettings.DEFAULT_SCHEMA) == false) {
                logger.info("------> using schema " + db_schema);
                DataSource ds = dataSources.get(db_schema);

                return ds;
            } else {
                return loginDataSource;
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Determine the schema based on the logger-in User.
     *
     * @return
     */
    private String determineTargetSchema() {
        try {
            User usuario = usuarioProvider.customUserDetails(); // request scoped answer!
            return usuario.getTunnel().getDb_schema();
        } catch (RuntimeException e) {
            // This shouldn't be necessary, since we are planning to use a pre-initialized database.
            // And there should only be usages of this DataSource in a logged-in situation
            //logger.info("------> user not present, falling back to default schema", e);

            System.out.println("------> user not present, falling back to default schema\n");

            return MySettings.DEFAULT_SCHEMA;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return super.createConnectionBuilder();
    }

}
