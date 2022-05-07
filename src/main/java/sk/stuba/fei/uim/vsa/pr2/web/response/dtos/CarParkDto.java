package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import java.util.List;

public class CarParkDto extends Dto {
    private String name;
    private String address;
    private Integer prices;
    private List<CarParkFloorDto> floors;

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

    public Integer getPrices() {
        return prices;
    }

    public void setPrices(Integer prices) {
        this.prices = prices;
    }

    public List<CarParkFloorDto> getFloors() {
        return floors;
    }

    public void setFloors(List<CarParkFloorDto> floors) {
        this.floors = floors;
    }
}

