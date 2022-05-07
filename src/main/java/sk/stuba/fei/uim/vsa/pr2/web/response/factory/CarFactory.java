package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDto;

public class CarFactory implements ResponseFactory<Car, CarDto> {
    @Override
    public CarDto transformToDto(Car entity) {
        return null;
    }

    @Override
    public Car transformToEntity(CarDto dto) {
        return null;
    }
}
