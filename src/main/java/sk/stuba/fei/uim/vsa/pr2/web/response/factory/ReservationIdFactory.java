package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationIdDto;

public class ReservationIdFactory implements ResponseFactory<Reservation, ReservationIdDto> {
    @Override
    public ReservationIdDto transformToDto(Reservation entity) {
        ReservationIdDto reservationDto = new ReservationIdDto();
        reservationDto.setId(entity.getReservationId());
        reservationDto.setStart(entity.getStartDate());
        reservationDto.setEnd(entity.getEndDate());
        reservationDto.setPrices(entity.getCost());
        CarIdFactory carFactory = new CarIdFactory();
        reservationDto.setCar(carFactory.transformToDto(entity.getCar()));
        reservationDto.setSpot(entity.getParkingSpot().getParkingSpotId());
        return reservationDto;
    }

    @Override
    public Reservation transformToEntity(ReservationIdDto dto) {
        return null;
    }
}
