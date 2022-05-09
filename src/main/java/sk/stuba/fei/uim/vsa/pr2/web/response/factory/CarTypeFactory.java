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
//        List<Long> ids = new ArrayList<>();
//        entity.getCars().forEach(car -> ids.add(car.getCarId()));
        CarIdFactory factory = new CarIdFactory();
        carTypeDto.setCars(entity.getCars().stream().map(factory::transformToDto).collect(Collectors.toList()));
        return carTypeDto;
    }

    @Override
    public CarType transformToEntity(CarTypeDto dto) {

        return null;
    }
}
