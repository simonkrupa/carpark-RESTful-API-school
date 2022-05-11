package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ReservationDto extends Dto{
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date start;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date end;
    private Integer prices;
    private CarDto car;
    private ParkingSpotDto spot;

    public ReservationDto() {
    }

    public ReservationDto(@JsonProperty(value = "car", required = true) CarDto car, @JsonProperty(value = "spot", required = true) ParkingSpotDto spot) {
        this.car = car;
        this.spot = spot;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getPrices() {
        return prices;
    }

    public void setPrices(Integer prices) {
        this.prices = prices;
    }

//    public CarDtoId getCar() {
//        return car;
//    }
//
//    public void setCar(CarDtoId car) {
//        this.car = car;
//    }


    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }

    public ParkingSpotDto getSpot() {
        return spot;
    }

    public void setSpot(ParkingSpotDto spot) {
        this.spot = spot;
    }
}
