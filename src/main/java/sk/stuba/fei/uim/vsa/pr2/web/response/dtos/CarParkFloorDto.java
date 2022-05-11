package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CarParkFloorDto extends Dto{
    private String identifier;
    private Long carPark;
    private List<ParkingSpotDto> spots;

    public CarParkFloorDto() {
    }

    public CarParkFloorDto(@JsonProperty(value = "identifier", required = true) String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public List<ParkingSpotDto> getSpots() {
        return spots;
    }

    public void setSpots(List<ParkingSpotDto> spots) {
        this.spots = spots;
    }
}
