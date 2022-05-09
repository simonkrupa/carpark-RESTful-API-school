package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.*;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFloorFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Path("/carparks")
public class CarParkResource {
    private static final String EMPTY_RESPONSE = "{}";

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarParkFactory factory = new CarParkFactory();
    private final CarParkFloorFactory floorFactory = new CarParkFloorFactory();
    private final ParkingSpotFactory spotFactory = new ParkingSpotFactory();


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
            Boolean createdCarType = false;
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
                        ParkingSpot ps = new ParkingSpot();
                        for(ParkingSpotDto parkingSpot : cpf.getSpots()){
                            if(parkingSpot.getType()!=null){
                                CarType carType = carParkService.getCarType(parkingSpot.getType().getName());
                                if(carType==null){
                                    carType = carParkService.createCarType(parkingSpot.getType().getName());
                                    createdCarType = true;
                                    if(carType==null){
                                        carParkService.deleteCarPark(cp.getCarParkId());
                                        return Response
                                                .status(Response.Status.BAD_REQUEST)
                                                .build();
                                    }
                                }
                                ps = carParkService.createParkingSpot(cp.getCarParkId(), cpf.getIdentifier(), parkingSpot.getIdentifier(), carType.getCarTypeId());
                            }else {
                                ps = carParkService.createParkingSpot(cp.getCarParkId(), cpf.getIdentifier(), parkingSpot.getIdentifier());
                            }
                            if(ps==null){
                                carParkService.deleteCarPark(cp.getCarParkId());
                                if(createdCarType){
                                    carParkService.deleteCarType(carParkService.getCarType(parkingSpot.getType().getName()).getCarTypeId());
                                }
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
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
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

    @POST
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFloors(@PathParam("id") Long id, String body){
        if(id==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        try {
            Boolean createdCarType = false;
            CarParkFloorDto dto = json.readValue(body, CarParkFloorDto.class);
            CarParkFloor carParkFloor = carParkService.createCarParkFloor(id, dto.getIdentifier());
            if(carParkFloor==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            if(dto.getSpots()!=null) {
                for (ParkingSpotDto spot : dto.getSpots()) {
                    ParkingSpot parkingSpot = new ParkingSpot();
                    if(spot.getType()!=null){
                        CarType carType = carParkService.getCarType(spot.getType().getName());
                        if(carType==null){
                            carType = carParkService.createCarType(spot.getType().getName());
                            createdCarType = true;
                            if(carType==null){
                                carParkService.deleteCarParkFloor(carParkFloor.getCarParkFloorId());
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .build();
                            }
                        }
                        parkingSpot = carParkService.createParkingSpot(id, dto.getIdentifier(), spot.getIdentifier(), carType.getCarTypeId());
                    }else {
                        parkingSpot = carParkService.createParkingSpot(id, dto.getIdentifier(), spot.getIdentifier());
                    }
                    if(parkingSpot == null) {
                        carParkService.deleteCarParkFloor(carParkFloor.getCarParkFloorId());
                        if(createdCarType){
                            carParkService.deleteCarType(carParkService.getCarType(spot.getType().getName()).getCarTypeId());
                        }
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .build();
                    }
                }
            }
            CarParkFloor carParkFloorResponse = carParkService.getCarParkFloor(carParkFloor.getCarParkFloorId());
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(floorFactory.transformToDto(carParkFloorResponse)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("/{id}/floors/{identifier}")
    public Response deleteFloor(@PathParam("id") Long id, @PathParam("identifier") String identifier){
        if(id==null){return Response.noContent().build();}
        List<CarParkFloor> floors = carParkService.getCarParkFloors(id);
        if(floors==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        for(CarParkFloor floor: floors){
            if(floor.getFloorIdentifier().equals(identifier)){
                carParkService.deleteCarParkFloor(floor.getCarParkFloorId());
                return Response
                        .status(Response.Status.NO_CONTENT)
                        .build();
            }
        }
        return Response
                .status(Response.Status.BAD_REQUEST)
                .build();
    }

    @GET
    @Path("/{id}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpots(@PathParam("id") Long id, @QueryParam("free") Boolean free){
        CarPark carPark = carParkService.getCarPark(id);
        if(carPark==null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        List<ParkingSpot> spots = new ArrayList<>();
        if(free!=null) {
            //ak je free nieco divne
            if (free) {
                spots.addAll(carParkService.getAvailableSpots(carPark.getCarParkId()));
            } else {
                spots.addAll(carParkService.getOccupiedSpots(carPark.getCarParkId()));
            }
        }else {
            //test ked je null floors
            for (CarParkFloor carParkFloor : carPark.getFloors()) {
                List<ParkingSpot> parkingSpots = carParkService.getParkingSpots(carPark.getCarParkId(), carParkFloor.getFloorIdentifier());
                spots.addAll(parkingSpots);
            }
        }
        List<ParkingSpotDto> parkingSpotDtos = new ArrayList<>();
        for(ParkingSpot ps : spots){
            parkingSpotDtos.add(spotFactory.transformToDto(ps));
        }
        return Response
                .status(Response.Status.OK)
                .entity(parkingSpotDtos)
                .build();

    }

    @GET
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpots(@PathParam("id")Long id, @PathParam("identifier") String identifier){
        if(id==null || identifier == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        CarPark carPark = carParkService.getCarPark(id);
        if(carPark==null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        for(CarParkFloor cpf: carPark.getFloors()){
            if(cpf.getFloorIdentifier().equals(identifier)){
                List<ParkingSpot> spots = carParkService.getParkingSpots(id, identifier);
                if(spots==null){
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                List<ParkingSpotDto> spotDtos = spots.stream().map(spotFactory::transformToDto).collect(Collectors.toList());
                return Response
                        .status(Response.Status.OK)
                        .entity(spotDtos)
                        .build();
            }
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .build();
    }

    @POST
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSpot(@PathParam("id") Long id, @PathParam("identifier") String identifier, String body){
        if(id == null && identifier == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        try {
            Boolean createdCarType = false;
            ParkingSpotDto dto = json.readValue(body, ParkingSpotDto.class);
            CarPark carPark = carParkService.getCarPark(id);
            if(carPark==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            for(CarParkFloor carParkFloor: carPark.getFloors()){
                if(carParkFloor.getFloorIdentifier().equals(identifier)){
                    ParkingSpot parkingSpot = new ParkingSpot();
                    if(dto.getType()!=null){
                        CarType carType = carParkService.getCarType(dto.getType().getName());
                        if(carType==null){
                            carType = carParkService.createCarType(dto.getType().getName());
                            createdCarType = true;
                            if(carType==null){
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .build();
                            }
                        }
                        parkingSpot = carParkService.createParkingSpot(id, identifier, dto.getIdentifier(), carType.getCarTypeId());
                    }else {
                        parkingSpot = carParkService.createParkingSpot(id, identifier, dto.getIdentifier());
                    }
                    if(parkingSpot==null){
                        if(createdCarType){
                            carParkService.deleteCarType(carParkService.getCarType(dto.getType().getName()).getCarTypeId());
                        }
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .build();
                    }
                    ParkingSpotDto spotDto = spotFactory.transformToDto(parkingSpot);
                    return Response
                            .status(Response.Status.CREATED)
                            .entity(spotDto)
                            .build();
                }
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        } catch (JsonProcessingException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

}
