package pbouda.singlethreaded;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.CompositeDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.core.DatabaseClient;

import java.time.Duration;

public class DatabaseInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

    @Override
    public void initialize(GenericApplicationContext ctx) {
        ConnectionPool connectionPool = connectionPool(ctx.getEnvironment());

        ctx.registerBean(ConnectionFactory.class,
                () -> connectionPool,
                definition -> definition.setDestroyMethodName("dispose"));

        ctx.registerBean(ConnectionFactoryInitializer.class,
                () -> initializer(connectionPool));

        ctx.registerBean(DatabaseClient.class,
                () -> databaseClient(connectionPool));
    }

    private static ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    private static ConnectionPool connectionPool(ConfigurableEnvironment env) {
        PostgresqlConnectionConfiguration configuration =
                PostgresqlConnectionConfiguration.builder()
                        .sslMode(SSLMode.DISABLE)
                        .host("localhost")
                        .port(26257)
                        .username("root")
                        .password("")
                        .database("postgres")
                        .build();

        ConnectionPoolConfiguration poolConfiguration =
                ConnectionPoolConfiguration.builder(new PostgresqlConnectionFactory(configuration))
                        .maxIdleTime(Duration.ofMinutes(1))
                        .initialSize(5)
                        .maxSize(20)
                        .registerJmx(true)
                        .name("single-threaded")
                        .build();

        return new ConnectionPool(poolConfiguration);
    }

    private static DatabaseClient databaseClient(ConnectionPool connectionPool) {
        return DatabaseClient.builder()
                .connectionFactory(connectionPool)
                .build();
    }
}
