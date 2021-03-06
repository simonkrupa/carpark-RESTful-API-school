package sk.stuba.fei.uim.vsa.pr2.web.response.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CarTypeDto extends Dto{
    private String name;

    public CarTypeDto() {
    }

    public CarTypeDto(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
