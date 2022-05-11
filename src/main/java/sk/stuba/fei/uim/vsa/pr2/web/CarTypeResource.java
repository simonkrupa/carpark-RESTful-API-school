package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarTypeDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarTypeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sk.stuba.fei.uim.vsa.pr2.BasicAuth.getAuth;

@Path("/cartypes")
public class CarTypeResource {
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarTypeFactory factory = new CarTypeFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @QueryParam("name") String name){
        if(!getAuth(authorization)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        if(name!=null){
            try {
                CarType carType = carParkService.getCarType(name);
                if (carType == null) {
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                CarTypeDto carTypeDto = factory.transformToDto(carType);
                List<CarTypeDto> carTypeResponse = new ArrayList<>();
                carTypeResponse.add(carTypeDto);
                return Response
                        .status(Response.Status.OK)
                        .entity(carTypeResponse)
                        .build();
            }catch (Exception e){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
        }
        try {
            List<CarType> carTypes = carParkService.getCarTypes();
            if (carTypes == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            List<CarTypeDto> carTypeDtos = carTypes.stream().map(factory::transformToDto).collect(Collectors.toList());
            return Response
                    .status(Response.Status.OK)
                    .entity(carTypeDtos)
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
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
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        try {
            CarType carType = carParkService.getCarType(id);
            if(carType==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            CarTypeDto carTypeDto = factory.transformToDto(carType);
            return Response
                    .status(Response.Status.OK)
                    .entity(carTypeDto)
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id){
        if(!getAuth(authorization)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        if(id==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        try{
            CarType carType = carParkService.deleteCarType(id);
            if(carType==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            return Response
                    .status(Response.Status.NO_CONTENT)
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
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
            CarTypeDto carTypeDto = json.readValue(body, CarTypeDto.class);
            CarType carType = carParkService.createCarType(carTypeDto.getName());
            if(carType==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            CarTypeDto carTypeResponse = factory.transformToDto(carType);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(carTypeResponse)
                    .build();
        } catch (JsonProcessingException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }

}
