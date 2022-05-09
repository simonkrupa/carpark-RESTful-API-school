package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ParkingSpotDto;

public class ParkingSpotFactory implements ResponseFactory<ParkingSpot, ParkingSpotDto> {
    @Override
    public ParkingSpotDto transformToDto(ParkingSpot entity) {
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(entity.getParkingSpotId());
        parkingSpotDto.setCarParkFloor(entity.getFloor().getFloorIdentifier());
        parkingSpotDto.setCarPark(entity.getFloor().getCarPark().getCarParkId());
        parkingSpotDto.setIdentifier(entity.getSpotIdentifier());
        CarTypeFactory factory = new CarTypeFactory();
        parkingSpotDto.setType(factory.transformToDto(entity.getCarType()));
        //free
        if(entity.isAvailable()) {
            parkingSpotDto.setFree(true);
        }else{
            parkingSpotDto.setFree(false);
        }
        //reservations
        return parkingSpotDto;
    }

    @Override
    public ParkingSpot transformToEntity(ParkingSpotDto dto) {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setParkingSpotId(dto.getId());
        parkingSpot.setSpotIdentifier(dto.getIdentifier());
//        parkingSpot.setFloor();
        return parkingSpot;
    }
}
