package pbouda.singlethreaded;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

public class RoutesInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

    private static RouterFunction<ServerResponse> persons(GenericApplicationContext ctx) {
        DatabaseClient databaseClient = ctx.getBean(DatabaseClient.class);
        var repository = new Repository(databaseClient);
        var personsHandler = new PersonsHandler(repository);

        return RouterFunctions.nest(path("/persons"), RouterFunctions.route()
                .POST("/", personsHandler::createPerson)
                .GET("/{id}", personsHandler::getPerson)
                .build()
        );
    }

    @Override
    public void initialize(GenericApplicationContext ctx) {
        ctx.registerBean("personHandler", RouterFunction.class, () -> persons(ctx));
    }
}
