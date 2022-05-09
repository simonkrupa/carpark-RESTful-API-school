package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarTypeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarTypeFactory implements ResponseFactory<CarType, CarTypeDto> {
    @Override
    public CarTypeDto transformToDto(CarType entity) {
        CarTypeDto carTypeDto = new CarTypeDto();
        carTypeDto.setId(entity.getCarTypeId());
        carTypeDto.setName(entity.getName());

        return carTypeDto;
    }

    @Override
    public CarType transformToEntity(CarTypeDto dto) {

        return null;
    }
}
