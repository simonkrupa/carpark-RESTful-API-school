package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "findAllCarTypes", query = "SELECT ct from CarType ct")
@NamedQuery(name = "findCarTypeByName", query = "select ct from CarType ct where ct.name =:name")
@Table(name = "CAR_TYPE")
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carTypeId;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany
    private List<Car> cars;
    public void addCar(Car car){
        if(cars == null){
            cars = new ArrayList<>();
        }
        cars.add(car);
    }

    @OneToMany
    private List<ParkingSpot> parkingSpots;

    public void addParkingSpot(ParkingSpot parkingSpot){
        if(parkingSpots==null){
            parkingSpots = new ArrayList<>();
        }
        parkingSpots.add(parkingSpot);
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public void createDefaultCarType(){
        name = "Benzin";
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public CarType(String name) {
        this.name = name;
    }

    public CarType() {
    }

    public Long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        List<String> parkingSpotsToString = new ArrayList<>();
        parkingSpots.forEach(parkingSpot -> parkingSpotsToString.add(parkingSpot.getSpotIdentifier()));
        List<String> carsToString = new ArrayList<>();
        cars.forEach(car -> carsToString.add(car.getVehicleRegistrationPlate()));
        return "CarType{" +
                "carTypeId=" + carTypeId +
                ", name='" + name + '\'' +
                ", cars=" + carsToString +
                ", parkingSpots=" + parkingSpotsToString +
                '}';
    }
}
