package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.*;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarIdFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/cars")
public class CarResource {
    private static final String EMPTY_RESPONSE = "{}";

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarFactory factory = new CarFactory();
    private final CarIdFactory carIdFactory = new CarIdFactory();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("user") Long id, @QueryParam("vrp") String vrp) {
        List<CarDto> carDtos = new ArrayList<>();
        if (id == null && vrp == null) {
            try {
                List<Car> cars = carParkService.getCars();
                cars.forEach(car -> {
                    CarDto carDto = factory.transformToDto(car);
                    carDtos.add(carDto);
                });
                return Response
                        .status(Response.Status.OK)
                        .entity(carDtos)
                        .build();
            } catch (Exception e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
        } else if (id != null && vrp != null) {
            try {
                Car car = carParkService.getCar(vrp);
                if (car == null) {
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                List<Car> cars = carParkService.getCars(id);
                if (cars == null) {
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                for (Car c : cars) {
                    if (c.getVehicleRegistrationPlate().equals(car.getVehicleRegistrationPlate())) {
                        CarDto carDto1 = factory.transformToDto(car);
                        List<CarDto> carDtos1 = new ArrayList<>();
                        carDtos1.add(carDto1);
                        return Response
                                .status(Response.Status.OK)
                                .entity(carDtos1)
                                .build();
                    }
                }
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            } catch (Exception e) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
        } else if(id==null && vrp!=null){
            try {
                Car car = carParkService.getCar(vrp);
                if (car == null) {
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                carDtos.add(factory.transformToDto(car));
                return Response
                        .status(Response.Status.OK)
                        .entity(carDtos)
                        .build();
            } catch (Exception e){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } else if(id!=null && vrp ==null){
            try {
                List<Car> cars = carParkService.getCars(id);
                if(cars==null){
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                List<CarDto> carDtos1 = cars.stream().map(factory::transformToDto).collect(Collectors.toList());
                return Response
                        .status(Response.Status.OK)
                        .entity(carDtos1)
                        .build();
            }catch (Exception e){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            }
        return Response
                .status(Response.Status.NOT_FOUND)
                .build();
        }



    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        if(id == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        try {
            Car car = carParkService.getCar(id);
            if (car == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            return Response
                    .status(Response.Status.OK)
                    .entity(factory.transformToDto(car))
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String body) {
        try {
            CarDto dto = json.readValue(body, CarDto.class);
            if(dto.getOwner()!=null) {
                User user = carParkService.getUser(dto.getOwner().getEmail());
                if(user==null){
                    user = carParkService.createUser(dto.getOwner().getFirstname(), dto.getOwner().getLastname(), dto.getOwner().getEmail());
                    if(user==null){
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .build();
                    }
                }
                Car car = carParkService.createCar(user.getUserId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp());
                if(car==null){
                    carParkService.deleteUser(user.getUserId());
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .build();
                }
                CarDtoId carResponse = carIdFactory.transformToDto(car);
                return Response
                        .status(Response.Status.CREATED)
                        .entity(carResponse)
                        .build();
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

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        if(id==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        Car car = carParkService.deleteCar(id);
        if(car==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
