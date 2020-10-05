package pbouda.singlethreaded.model;

public class CreatePersonResponse {

    private final String id;

    public CreatePersonResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
