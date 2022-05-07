package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQuery(name = "findByPlate", query = "SELECT c from Car c WHERE c.vehicleRegistrationPlate=:plate")
@Table(name = "CAR")
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;
    private String brand;
    private String model;
    private String colour;
    @Column(nullable = false, unique = true)
    private String vehicleRegistrationPlate;
    @ManyToOne
    private CarType carType;

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    @OneToMany
    private List<Reservation> reservations;

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation){
        if(reservations == null)
        {
            reservations = new ArrayList<>();
        }
        reservations.add(reservation);
    }

    public Reservation getActiveReservation(){
        if(reservations!=null){
            if(reservations.size()>0) {
                if (reservations.get(reservations.size() - 1).getEndDate() == null) {
                    return reservations.get(reservations.size() - 1);
                }
            }
        }
        return null;
    }

    @ManyToOne
    private User user;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getVehicleRegistrationPlate() {
        return vehicleRegistrationPlate;
    }

    public void setVehicleRegistrationPlate(String vehicleRegistrationPlate) {
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car(String brand, String model, String colour, String vehicleRegistrationPlate) {
        this.brand = brand;
        this.model = model;
        this.colour = colour;
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
    }

    public Car() {
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        List<String> reservationsToString = new ArrayList<>();
        reservations.forEach(reservation -> reservationsToString.add(""+reservation.getReservationId()));
        return "Car{" +
                "carId=" + carId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", vehicleRegistrationPlate='" + vehicleRegistrationPlate + '\'' +
                ", carType=" + carType.getName() +
                ", reservations=" + reservationsToString +
                ", user=" + user.getEmail() +
                '}';
    }
}
