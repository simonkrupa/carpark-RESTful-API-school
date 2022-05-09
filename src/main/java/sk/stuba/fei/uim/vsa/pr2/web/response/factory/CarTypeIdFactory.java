package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarTypeIdDto;

import java.util.ArrayList;
import java.util.List;

public class CarTypeIdFactory implements ResponseFactory<CarType, CarTypeIdDto> {
    @Override
    public CarTypeIdDto transformToDto(CarType entity) {
        CarTypeIdDto carTypeIdDto = new CarTypeIdDto();
        carTypeIdDto.setId(entity.getCarTypeId());
        carTypeIdDto.setName(entity.getName());

        return carTypeIdDto;
    }

    @Override
    public CarType transformToEntity(CarTypeIdDto dto) {
        return null;
    }
}
