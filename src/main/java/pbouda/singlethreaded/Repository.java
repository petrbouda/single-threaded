package pbouda.singlethreaded;

import io.r2dbc.spi.Row;
import org.springframework.data.r2dbc.core.DatabaseClient;
import pbouda.singlethreaded.model.Person;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Repository {

    private static final Function<Row, Person> PERSON_MAPPER = row ->
            new Person(
                    row.get("id", String.class),
                    row.get("firstname", String.class),
                    row.get("lastname", String.class),
                    row.get("address", String.class),
                    row.get("birthday", LocalDate.class),
                    row.get("gender", String.class),
                    row.get("comment", String.class)
            );

    public static final Function<Mono<Map<String, Object>>, Mono<String>> CREATED_MAPPER =
            monoParams -> monoParams.map(params -> (String) params.get("id"));

    private static final String SINGLE_QUERY = "SELECT * FROM persons WHERE id >= :id";
    private static final String MULTI_QUERY = "SELECT * FROM persons LIMIT :limit";

    private final DatabaseClient databaseClient;

    public Repository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<String> insert(Person persons) {
        return databaseClient.insert()
                .into(Person.class)
                .using(persons)
                .fetch()
                .one()
                .as(CREATED_MAPPER);
    }

    public Mono<Person> getSingle(String id) {
        return databaseClient.execute(SINGLE_QUERY)
                .bind("id", id)
                .map(PERSON_MAPPER)
                .first();
    }

    public Mono<List<Person>> getPersons(int limit) {
        return databaseClient.execute(MULTI_QUERY)
                .bind("limit", limit)
                .map(PERSON_MAPPER)
                .all()
                .collectList();
    }
}
