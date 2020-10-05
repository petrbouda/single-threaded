package pbouda.singlethreaded;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import reactor.netty.http.HttpResources;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpResources;

@EnableAutoConfiguration(exclude = {
        R2dbcAutoConfiguration.class,
        R2dbcRepositoriesAutoConfiguration.class,
        R2dbcTransactionManagerAutoConfiguration.class,
        WebClientAutoConfiguration.class,
        ValidationAutoConfiguration.class
})
public class Application {

    public static void main(String[] args) {
        TcpResources tcpResources = TcpResources.get();
        HttpResources.set((LoopResources) tcpResources);

        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.REACTIVE)
                .bannerMode(Banner.Mode.OFF)
                .initializers(
                        new DatabaseInitializer(),
                        new RoutesInitializer()
                ).run(args);
    }
}
