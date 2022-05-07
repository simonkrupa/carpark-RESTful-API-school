package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFloorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Path("/carparks")
public class CarParkResource {
    private static final String EMPTY_RESPONSE = "{}";

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("name") String carParkName) {
        List<CarParkDto> carParkDtos = new ArrayList<>();
        if(carParkName==null) {
            try {
                List<CarPark> carParks = carParkService.getCarParks();
                carParks.forEach(carPark -> {
                    CarParkDto carParkDto = factory.transformToDto(carPark);
                    carParkDtos.add(carParkDto);
                });
//            return json.writeValueAsString(carParks);
                return Response
                        .status(Response.Status.OK)
                        .entity(carParkDtos)
                        .build();
            } catch (Exception e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
        }else {
            try{
                CarPark carPark = carParkService.getCarPark(carParkName);
                if(carPark==null){
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                CarParkDto carParkDto = factory.transformToDto(carPark);
                carParkDtos.add(carParkDto);
                return Response
                        .status(Response.Status.OK)
                        .entity(carParkDtos)
                        .build();
            }catch (Exception e){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
        }
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        if(id == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();}
        try{
            CarPark carPark = carParkService.getCarPark(id);
            if(carPark==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }else{
                CarParkDto carParkDto = factory.transformToDto(carPark);
                return Response
                        .status(Response.Status.OK)
                        .entity(carParkDto)
                        .build();
            }

        } catch (Exception e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String body) {
        try {
            CarParkDto dto = json.readValue(body, CarParkDto.class);
            CarPark carPark = factory.transformToEntity(dto);
            CarPark cp = carParkService.createCarPark(carPark.getName(), carPark.getAddress(), carPark.getPricePerHour());
            if(cp==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            if(dto.getFloors()!=null) {
                for (CarParkFloorDto cpf : dto.getFloors()) {
                    CarParkFloor carParkFloor = carParkService.createCarParkFloor(cp.getCarParkId(), cpf.getIdentifier());
                    if(carParkFloor == null){
                        carParkService.deleteCarPark(cp.getCarParkId());
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .build();
                    }
                    if (cpf.getSpots() != null) {
//                        cpf.getSpots().forEach(spot -> carParkService.createParkingSpot(cp.getCarParkId(), cpf.getIdentifier(), spot.getIdentifier()));
                        for(ParkingSpotDto parkingSpot : cpf.getSpots()){
                            ParkingSpot ps = carParkService.createParkingSpot(cp.getCarParkId(), cpf.getIdentifier(), parkingSpot.getIdentifier());
                            if(ps==null){
                                carParkService.deleteCarPark(cp.getCarParkId());
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .build();
                            }
                        }
                    }
                }
            }
            CarPark carParkResponse = carParkService.getCarPark(cp.getCarParkId());
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(factory.transformToDto(carParkResponse)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        if(id==null){return Response.noContent().build();}
        CarPark carPark = carParkService.deleteCarPark(id);
        if(carPark==null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    @GET
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFloorsByCarParkId(@PathParam("id") Long id){
        if(id==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        try {
            CarPark carPark = carParkService.getCarPark(id);
            if(carPark==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            List<CarParkFloor> floors = carParkService.getCarParkFloors(id);
            List<CarParkFloorDto> floorsDto = new ArrayList<>();
            for(CarParkFloor f: floors){
                floorsDto.add(floorFactory.transformToDto(f));
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(floorsDto)
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

}
