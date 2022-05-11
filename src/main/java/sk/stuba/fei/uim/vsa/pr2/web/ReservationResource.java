package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.BasicAuth;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ReservationIdDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ReservationFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ReservationIdFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static sk.stuba.fei.uim.vsa.pr2.BasicAuth.compareId;
import static sk.stuba.fei.uim.vsa.pr2.BasicAuth.getAuth;

@Path("/reservations")
public class ReservationResource {

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final ReservationFactory factory = new ReservationFactory();
    private final ReservationIdFactory reservationIdFactory = new ReservationIdFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @QueryParam("user") Long user, @QueryParam("spot") Long spot, @QueryParam("date") String date){
        if(!getAuth(authorization)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        //TODO DATE
        if((spot!=null && date==null) || (spot==null && date!=null)){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        if(user!=null && spot!=null) {
            User user1 = carParkService.getUser(user);
            if(user1==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            Date date1= null;
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            List<Reservation> reservations = carParkService.getReservations(spot, date1);
            if(reservations==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            List<Reservation> reservationList = new ArrayList<>();
            for (Reservation r: reservations){
                if(r.getCar().getUser().getUserId().equals(user1.getUserId())){
                    reservationList.add(r);
                }
            }
            List<ReservationIdDto> reservationDtos = reservationList.stream().map(reservationIdFactory::transformToDto).collect(Collectors.toList());
            return Response
                    .status(Response.Status.OK)
                    .entity(reservationDtos)
                    .build();
        }
        if(user!=null){
            //TODO check if valid user
            List<Reservation> reservations = carParkService.getAllMyReservations(user);
            if(reservations==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            List<ReservationIdDto> reservationDtos = reservations.stream().map(reservationIdFactory::transformToDto).collect(Collectors.toList());
            return Response
                    .status(Response.Status.OK)
                    .entity(reservationDtos)
                    .build();
        }
        if(spot!=null){
            SimpleDateFormat
                    parser = new SimpleDateFormat("yyyy-MM-dd");
            Date date1= null;
            try {
                date1 = parser.parse(date);
            } catch (ParseException e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            List<Reservation> reservations = carParkService.getReservations(spot, date1);
            if(reservations==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            List<ReservationIdDto> reservationDtos = reservations.stream().map(reservationIdFactory::transformToDto).collect(Collectors.toList());
            return Response
                    .status(Response.Status.OK)
                    .entity(reservationDtos)
                    .build();
        }
        List<Reservation> reservationList = carParkService.getAllReservations();
        if(reservationList==null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        List<ReservationIdDto> reservationDtos = reservationList.stream().map(reservationIdFactory::transformToDto).collect(Collectors.toList());
        return Response
                .status(Response.Status.OK)
                .entity(reservationDtos)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id){
        if(!getAuth(authorization)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        if(id==null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        Reservation reservation = carParkService.getReservationById(id);
        if(reservation==null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        ReservationIdDto reservationDto = reservationIdFactory.transformToDto(reservation);
        return Response
                .status(Response.Status.OK)
                .entity(reservationDto)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, String body){
        if(!getAuth(authorization)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        try{
            ReservationDto reservationDto = json.readValue(body, ReservationDto.class);
            Reservation reservation = carParkService.createReservation(reservationDto.getSpot().getId(), reservationDto.getCar().getId());
            if(reservation==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            return Response
                    .status(Response.Status.CREATED)
                    .entity(reservationIdFactory.transformToDto(reservation))
                    .build();
        } catch (JsonProcessingException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    @POST
    @Path("/{id}/end")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response endReservation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id){
        if(!compareId(authorization, id)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        try {
            Reservation r = carParkService.getReservationById(id);
            if(r==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            Reservation reservation = carParkService.endReservation(id);
            if (reservation == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(reservationIdFactory.transformToDto(reservation))
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

}
