package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarFactory;


import java.util.List;

@Path("/cars")
public class CarResource {
    private static final String EMPTY_RESPONSE = "{}";

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarFactory factory = new CarFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        List<Car> cars = carParkService.getCars();
        try {
            return json.writeValueAsString(cars);
        } catch (JsonProcessingException e) {
            try {
                return json.writeValueAsString(MessageDto.buildError(e.getMessage()));
            } catch (JsonProcessingException jsonProcessingException) {
                return EMPTY_RESPONSE;
            }
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {
        if(id == null){return EMPTY_RESPONSE;}
        try{
            Object car = carParkService.getCar(id);
            return json.writeValueAsString(car);
        } catch (JsonProcessingException e) {
            return EMPTY_RESPONSE;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String body) {
        try {
            CarDto dto = json.readValue(body, CarDto.class);
            Car car = factory.transformToEntity(dto);
            Object carResponse = carParkService.createCar(1L, dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp());
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(carResponse))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        if(id==null){return Response.noContent().build();}
        Object carPark = carParkService.deleteCarPark(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
