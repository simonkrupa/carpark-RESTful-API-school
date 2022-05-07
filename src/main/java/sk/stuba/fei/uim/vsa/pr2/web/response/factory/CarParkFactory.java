package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkDto;

import java.util.List;
import java.util.stream.Collectors;

public class CarParkFactory implements ResponseFactory<CarPark, CarParkDto> {
    @Override
    public CarParkDto transformToDto(CarPark entity) {
        CarParkDto carParkDto = new CarParkDto();
        carParkDto.setId(entity.getCarParkId());
        carParkDto.setName(entity.getName());
        carParkDto.setAddress(entity.getAddress());
        carParkDto.setPrices(entity.getPricePerHour());
        CarParkFloorFactory factory = new CarParkFloorFactory();
        carParkDto.setFloors(entity.getFloors().stream().map(factory::transformToDto).collect(Collectors.toList()));
        return carParkDto;
    }

    @Override
    public CarPark transformToEntity(CarParkDto dto) {
        CarPark carPark = new CarPark();
        if(dto.getId()!=null) {
            carPark.setCarParkId(dto.getId());
        }
        carPark.setAddress(dto.getAddress());
        carPark.setName(dto.getName());
        carPark.setPricePerHour(dto.getPrices());
//        if(dto.getFloors()!=null) {
//            CarParkFloorFactory carParkFloorFactory = new CarParkFloorFactory();
//            List<CarParkFloor> floors = dto.getFloors().stream().map(carParkFloorFactory::transformToEntity).collect(Collectors.toList());
//            carPark.setFloors(floors);
//        }
        return carPark;
    }
}
