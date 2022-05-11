package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDto extends Dto{
    private String firstName;
    private String lastName;
    private String email;
    private List<CarDto> cars;

    public UserDto() {
    }

    @JsonCreator
    public UserDto(@JsonProperty(value = "firstName", required = true) String firstName, @JsonProperty(value = "lastName", required = true) String lastName, @JsonProperty(value = "email", required = true) String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CarDto> getCars() {
        return cars;
    }

    public void setCars(List<CarDto> cars) {
        this.cars = cars;
    }
}
