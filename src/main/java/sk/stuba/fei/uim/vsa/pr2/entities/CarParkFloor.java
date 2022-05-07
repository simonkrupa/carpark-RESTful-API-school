package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NamedQuery(name = "findByFloorIdentifier", query = "select cpf from CarParkFloor cpf where cpf.floorIdentifier =:identifier and cpf.carParkFloorId=:floorId")
@Table(name = "CAR_PARK_FLOOR")
public class CarParkFloor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carParkFloorId;
    private String floorIdentifier;

    @ManyToOne
    private CarPark carPark;

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSpot> parkingSpots;

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

    public void setParkingSpots(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public void addParkingSpot(ParkingSpot parkingSpot){
        if(parkingSpots==null)
            parkingSpots = new ArrayList<>();
        parkingSpots.add(parkingSpot);
    }

    public CarParkFloor(String floorIdentifier) {
        this.floorIdentifier = floorIdentifier;
    }

    public CarParkFloor() {
    }

    public Long getCarParkFloorId() {
        return carParkFloorId;
    }

    public void setCarParkFloorId(Long carParkFloorId) {
        this.carParkFloorId = carParkFloorId;
    }


    public String getFloorIdentifier() {
        return floorIdentifier;
    }

    public void setFloorIdentifier(String floorIdentifier) {
        this.floorIdentifier = floorIdentifier;
    }

    @Override
    public String toString() {
        String carParkToString = carPark.getName();
        List<String> parkingSpotsToString = new ArrayList<>();
        parkingSpots.forEach(parkingSpot -> parkingSpotsToString.add(parkingSpot.getSpotIdentifier()));
        return "CarParkFloor{" +
                "carParkFloorId=" + carParkFloorId +
                ", floorIdentifier='" + floorIdentifier + '\'' +
                ", carPark=" + carParkToString +
                ", parkingSpots=" + parkingSpotsToString +
                '}';
    }
}
