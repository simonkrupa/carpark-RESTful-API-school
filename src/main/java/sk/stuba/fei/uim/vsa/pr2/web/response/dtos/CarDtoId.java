package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import sk.stuba.fei.uim.vsa.pr2.entities.User;

import java.util.List;

public class CarDtoId extends Dto{
    private String brand;
    private String model;
    private String vrp;
    private String colour;
    private Long type;
    private Long owner;
//    private List<ReservationDto> reservations;


    public Long getType() {
        return type;
    }

    public void setType(Long type) {
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

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }
}
