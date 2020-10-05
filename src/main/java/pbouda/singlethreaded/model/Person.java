package pbouda.singlethreaded.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("persons")
public class Person {

    @Id
    private final String id;
    private final String firstname;
    private final String lastname;
    private final String address;
    private final LocalDate birthday;
    private final String gender;
    private final String comment;

    public Person(
            String firstname,
            String lastname,
            String address,
            LocalDate birthday,
            String gender,
            String comment) {

        this(null, firstname, lastname, address, birthday, gender, comment);
    }

    public Person(
            String id,
            String firstname,
            String lastname,
            String address,
            LocalDate birthday,
            String gender,
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

    public String getGender() {
        return gender;
    }

    public String getString() {
        return gender;
    }

    public String getComment() {
        return comment;
    }
}
