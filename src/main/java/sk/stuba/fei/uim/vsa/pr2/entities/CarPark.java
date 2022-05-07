package sk.stuba.fei.uim.vsa.pr2.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@NamedQuery(name = "findByName", query = "select cp from CarPark cp where cp.name =:name")
@NamedQuery(name = "findAllCarParks", query = "SELECT cp from CarPark cp")
@Table(name = "CAR_PARK")
public class CarPark implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carParkId;
    @Column(unique = true, nullable = false)
    private String name;
    private String address;
    @Column(nullable = false)
    private Integer pricePerHour;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarParkFloor> floors;

    public List<CarParkFloor> getFloors() {
        return floors;
    }

    public void setFloors(List<CarParkFloor> floors) {
        this.floors = floors;
    }

    public void addFloor(CarParkFloor carParkFloor){
        if(floors==null)
            floors = new ArrayList<>();
        floors.add(carParkFloor);
    }

    public CarParkFloor getByFloorIdentifier(String floorIdentifier) {
        if (floors != null){
            for (CarParkFloor floor: floors) {
                System.out.println(floor.getFloorIdentifier());
                if (floor.getFloorIdentifier().equals(floorIdentifier)){
                    return floor;
                }
            }
        }
        return null;
    }

    public CarPark(String name, String address, Integer pricePerHour) {
        this.name = name;
        this.address = address;
        this.pricePerHour = pricePerHour;
    }

    public CarPark() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Integer pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Long getCarParkId() {
        return carParkId;
    }

    public void setCarParkId(Long carParkId) {
        this.carParkId = carParkId;
    }

    @Override
    public String toString() {
        List<String> floorsToString = new ArrayList<>();
        floors.forEach(floor -> floorsToString.add(floor.getFloorIdentifier()));
        return "CarPark{" +
                "carParkId=" + carParkId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", pricePerHour=" + pricePerHour +
                ", floors=" + floorsToString +
                '}';
    }
}
