package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkFloorDto;

import java.util.List;
import java.util.stream.Collectors;

public class CarParkFloorFactory implements ResponseFactory<CarParkFloor, CarParkFloorDto>{
    @Override
    public CarParkFloorDto transformToDto(CarParkFloor entity) {
        CarParkFloorDto carParkFloorDto = new CarParkFloorDto();
        carParkFloorDto.setId(entity.getCarParkFloorId());
        carParkFloorDto.setIdentifier(entity.getFloorIdentifier());
        carParkFloorDto.setCarPark(entity.getCarParkFloorId());
        ParkingSpotFactory factory = new ParkingSpotFactory();
        carParkFloorDto.setSpots(entity.getParkingSpots().stream().map(factory::transformToDto).collect(Collectors.toList()));
        return carParkFloorDto;
    }

    @Override
    public CarParkFloor transformToEntity(CarParkFloorDto dto) {
        CarParkFloor carParkFloor = new CarParkFloor();
        carParkFloor.setCarParkFloorId(dto.getId());
        carParkFloor.setFloorIdentifier(dto.getIdentifier());
//        carParkFloor.setCarPark(dto.getCarPark());
        ParkingSpotFactory parkingSpotFactory = new ParkingSpotFactory();
        List<ParkingSpot> spots = dto.getSpots().stream().map(parkingSpotFactory::transformToEntity).collect(Collectors.toList());
        carParkFloor.setParkingSpots(spots);
        return carParkFloor;
    }
}
