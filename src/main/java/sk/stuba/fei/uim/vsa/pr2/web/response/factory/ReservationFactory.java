package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationDto;

public class ReservationFactory implements ResponseFactory<Reservation, ReservationDto> {
    @Override
    public ReservationDto transformToDto(Reservation entity) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(entity.getReservationId());
        reservationDto.setStart(entity.getStartDate());
        reservationDto.setEnd(entity.getEndDate());
        reservationDto.setPrices(entity.getCost());
        CarFactory carFactory = new CarFactory();
        reservationDto.setCar(carFactory.transformToDto(entity.getCar()));
        ParkingSpotFactory parkingSpotFactory = new ParkingSpotFactory();
        reservationDto.setSpot(parkingSpotFactory.transformToDto(entity.getParkingSpot()));
        return reservationDto;
    }

    @Override
    public Reservation transformToEntity(ReservationDto dto) {
        return null;
    }
}
