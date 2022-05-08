package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDtoId;

public class CarIdFactory implements ResponseFactory<Car, CarDtoId> {
    @Override
    public CarDtoId transformToDto(Car entity) {
        CarDtoId carDtoId = new CarDtoId();
        carDtoId.setOwner(entity.getUser().getUserId());
        carDtoId.setBrand(entity.getBrand());
        carDtoId.setColour(entity.getColour());
        carDtoId.setModel(entity.getModel());
        carDtoId.setVrp(entity.getVehicleRegistrationPlate());
        return carDtoId;
    }

    @Override
    public Car transformToEntity(CarDtoId dto) {
        return null;
    }
}

