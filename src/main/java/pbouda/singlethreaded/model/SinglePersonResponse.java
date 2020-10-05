package pbouda.singlethreaded.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SinglePersonResponse {
    private final String id;
    private final String firstname;
    private final String lastname;
    private final String address;
    private final LocalDate birthday;
    private final Gender gender;
    private final String comment;

    public SinglePersonResponse(
            String id,
            String firstname,
            String lastname,
            String address,
            LocalDate birthday,
            Gender gender,
            String comment) {

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public String getComment() {
        return comment;
    }
}
