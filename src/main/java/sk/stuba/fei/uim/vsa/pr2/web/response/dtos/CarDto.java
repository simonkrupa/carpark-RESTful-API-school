package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import sk.stuba.fei.uim.vsa.pr2.entities.User;

import java.util.List;

public class CarDto extends Dto{
    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private CarTypeDto type;
    private UserDto owner;
//    private List<ReservationDto> reservations;


    public CarDto() {
    }

    public CarDto(@JsonProperty(value = "brand", required = true) String brand, @JsonProperty(value = "model", required = true) String model, @JsonProperty(value = "vrp", required = true) String vrp, @JsonProperty(value = "colour", required = true) String colour, @JsonProperty(value = "type", required = true) CarTypeDto type, @JsonProperty(value = "owner", required = true) UserDto owner) {
        this.brand = brand;
        this.model = model;
        this.vrp = vrp;
        this.colour = colour;
        this.type = type;
        this.owner = owner;
    }

    public CarTypeDto getType() {
        return type;
    }

    public void setType(CarTypeDto type) {
        this.type = type;
    }

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

    public String getVrp() {
        return vrp;
    }

    public void setVrp(String vrp) {
        this.vrp = vrp;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

//    public List<ReservationDto> getReservations() {
//        return reservations;
//    }
//
//    public void setReservations(List<ReservationDto> reservations) {
//        this.reservations = reservations;
//    }
}
