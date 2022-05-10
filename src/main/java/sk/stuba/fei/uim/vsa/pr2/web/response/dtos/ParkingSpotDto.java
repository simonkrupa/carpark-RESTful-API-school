package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import java.util.List;

public class ParkingSpotDto extends Dto{
    private String identifier;
    private String carParkFloor;
    private Long carPark;
    private CarTypeDto type;
    private Boolean free;
    private List<ReservationIdDto> reservations;

    public List<ReservationIdDto> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationIdDto> reservations) {
        this.reservations = reservations;
    }

    public CarTypeDto getType() {
        return type;
    }

    public void setType(CarTypeDto type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCarParkFloor() {
        return carParkFloor;
    }

    public void setCarParkFloor(String carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public Boolean getFree() {
        return free;
    }
    public void setFree(Boolean free) {
        this.free = free;
    }
//
//    public List<ReservationDto> getReservations() {
//        return reservations;
//    }
//
//    public void setReservations(List<ReservationDto> reservations) {
//        this.reservations = reservations;
//    }
}
