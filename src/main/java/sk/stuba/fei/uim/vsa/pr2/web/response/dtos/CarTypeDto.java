package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import java.util.List;

public class CarTypeDto extends Dto{
    private String name;
    private List<CarDtoId> cars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CarDtoId> getCars() {
        return cars;
    }

    public void setCars(List<CarDtoId> cars) {
        this.cars = cars;
    }
}
