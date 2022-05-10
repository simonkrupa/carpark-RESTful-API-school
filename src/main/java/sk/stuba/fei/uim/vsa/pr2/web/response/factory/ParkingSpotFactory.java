package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationIdDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        ReservationIdFactory reservationFactory = new ReservationIdFactory();
        List<ReservationIdDto> reservationIdDtos = new ArrayList<>();
        entity.getReservations().forEach(reservation -> reservationIdDtos.add(reservationFactory.transformToDto(reservation)));
        parkingSpotDto.setReservations(reservationIdDtos);
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
