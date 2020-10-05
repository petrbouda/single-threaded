package pbouda.singlethreaded;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pbouda.singlethreaded.model.*;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class PersonsHandler {

    private final Repository repository;

    public PersonsHandler(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    public Mono<ServerResponse> createPerson(ServerRequest request) {
        return request.bodyToMono(CreatePersonRequest.class)
                .flatMap(payload -> {
                    if (Validators.validate(payload)) {
                        return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .build();
                    } else {
                        return repository.insert(toPerson(payload))
                                .map(CreatePersonResponse::new)
                                .flatMap(response -> created(response, CreatePersonResponse.class));
                    }
                });
    }

    @NonNull
    public Mono<ServerResponse> getPerson(ServerRequest request) {
        String personId = request.pathVariable("id");
        return repository.getSingle(personId)
                .flatMap(person -> ok(toPersonResponse(person), SinglePersonResponse.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @NonNull
    public Mono<ServerResponse> getPersonsWithLimit(ServerRequest request) {
        int limit = request.queryParam("limit")
                .map(Integer::parseInt)
                .orElse(10);

        return repository.getPersons(limit)
                .flatMap(person -> ok(toPersonsResponse(person), SinglePersonResponse.class));
    }

    private static List<SinglePersonResponse> toPersonsResponse(List<Person> persons) {
        return persons.stream()
                .map(PersonsHandler::toPersonResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Person toPerson(CreatePersonRequest request) {
        return new Person(
                request.getFirstname(),
                request.getLastname(),
                request.getAddress(),
                request.getBirthday(),
                request.getGender().toString(),
                request.getComment()
        );
    }

    private static SinglePersonResponse toPersonResponse(Person person) {
        return new SinglePersonResponse(
                person.getId(),
                person.getFirstname(),
                person.getLastname(),
                person.getAddress(),
                person.getBirthday(),
                Gender.valueOf(person.getGender()),
                person.getComment()
        );
    }

    private static Mono<ServerResponse> ok(Object response, Class<?> clazz) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(response), clazz);
    }

    private static Mono<ServerResponse> created(Object response, Class<?> clazz) {
        return ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(response), clazz);
    }
}
