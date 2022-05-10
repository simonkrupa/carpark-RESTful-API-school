package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDtoId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarIdFactory implements ResponseFactory<Car, CarDtoId> {
    @Override
    public CarDtoId transformToDto(Car entity) {
        CarDtoId carDtoId = new CarDtoId();
        carDtoId.setId(entity.getCarId());
        carDtoId.setOwner(entity.getUser().getUserId());
        carDtoId.setBrand(entity.getBrand());
        carDtoId.setColour(entity.getColour());
        carDtoId.setModel(entity.getModel());
        carDtoId.setVrp(entity.getVehicleRegistrationPlate());
        carDtoId.setType(entity.getCarType().getCarTypeId());
        List<Long> reservations = new ArrayList<>();
        entity.getReservations().forEach(reservation -> reservations.add(reservation.getReservationId()));
        carDtoId.setReservations(reservations);
        return carDtoId;
    }

    @Override
    public Car transformToEntity(CarDtoId dto) {
        return null;
    }
}

