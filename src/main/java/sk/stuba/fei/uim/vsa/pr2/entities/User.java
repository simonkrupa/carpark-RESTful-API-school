package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "findByEmail", query = "SELECT u from User u WHERE u.email=:email")
@NamedQuery(name = "findAllUsers", query = "SELECT u from User u")
@Table(name = "USER")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstname;
    private String lastname;
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void addCar(Car car){
        if(cars == null){
            cars = new ArrayList<>();
        }
        cars.add(car);
    }

    public User() {
    }

    public User(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        List<String> carsToString = new ArrayList<>();
        cars.forEach(car -> carsToString.add(car.getVehicleRegistrationPlate()));
        return "User{" +
                "userId=" + userId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", cars=" + carsToString +
                '}';
    }
}
