package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import java.util.Date;

public class ReservationDto extends Dto{
    private Date start;
    private Date end;
    private Integer prices;
    private CarDto car;
    private ParkingSpotDto spot;

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
