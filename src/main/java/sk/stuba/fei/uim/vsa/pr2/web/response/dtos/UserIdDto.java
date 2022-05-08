package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import java.util.List;

public class UserIdDto extends Dto {
    private String firstName;
    private String lastName;
    private String email;
    private List<CarDtoId> cars;

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

    public List<CarDtoId> getCars() {
        return cars;
    }

    public void setCars(List<CarDtoId> cars) {
        this.cars = cars;
    }
}

