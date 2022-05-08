package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDto;

public class CarFactory implements ResponseFactory<Car, CarDto> {
    @Override
    public CarDto transformToDto(Car entity) {
        CarDto carDto = new CarDto();
//        UserFactory factory = new UserFactory();
//        carDto.setOwner(factory.transformToDto(entity.getUser()));
        carDto.setBrand(entity.getBrand());
        carDto.setColour(entity.getColour());
        carDto.setModel(entity.getModel());
        carDto.setVrp(entity.getVehicleRegistrationPlate());
        return carDto;
    }

    @Override
    public Car transformToEntity(CarDto dto) {
        return null;
    }
}
