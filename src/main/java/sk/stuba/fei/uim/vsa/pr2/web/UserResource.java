package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.*;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserIdFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sk.stuba.fei.uim.vsa.pr2.BasicAuth.getAuth;

@Path("/users")
public class UserResource {

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final UserFactory factory = new UserFactory();
    private final UserIdFactory userIdFactory = new UserIdFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @QueryParam("email") String email){
        if(!getAuth(authorization)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        try {
                if (email != null) {
                    User user = carParkService.getUser(email);
                    if (user == null) {
                        return Response
                                .status(Response.Status.NOT_FOUND)
                                .build();
                    }
                    List<UserIdDto> userDtos = new ArrayList<>();
                    userDtos.add(userIdFactory.transformToDto(user));
                    return Response
                            .status(Response.Status.OK)
                            .entity(userDtos)
                            .build();
                }
                List<User> users = carParkService.getUsers();
                if (users == null) {
                    return Response
                            .status(Response.Status.NOT_FOUND)
                            .build();
                }
                List<UserIdDto> userDtos = users.stream().map(userIdFactory::transformToDto).collect(Collectors.toList());
                return Response
                        .status(Response.Status.OK)
                        .entity(userDtos)
                        .build();
            } catch (Exception e){
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
        try {
            User user = carParkService.getUser(id);
            if (user == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            UserIdDto userDto = userIdFactory.transformToDto(user);
            return Response
                    .status(Response.Status.OK)
                    .entity(userDto)
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
        try{
            if(id==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            User user = carParkService.deleteUser(id);
            if(user==null){
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
            UserDto userDto = json.readValue(body, UserDto.class);
            User user = carParkService.createUser(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());
            if(user==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            if(userDto.getCars()!=null){
                for(CarDto carDto : userDto.getCars()){
                    if(carDto.getType()!=null) {
                        CarType carType = null;
                        if (carDto.getType().getId() != null) {
                            carType = carParkService.getCarType(carDto.getType().getId());
                            if (carType == null) {
                                carParkService.deleteUser(userDto.getId());
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .build();
                            }
                        } else {
                            carType = carParkService.createCarType(carDto.getType().getName());
                            if (carType == null) {
                                carParkService.deleteUser(userDto.getId());
                                return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .build();
                            }
                        }
                        Car car = carParkService.createCar(user.getUserId(), carDto.getBrand(), carDto.getModel(), carDto.getColour(), carDto.getVrp(), carType.getCarTypeId());
                        if (car == null) {
                            carParkService.deleteUser(user.getUserId());
                            if (carDto.getType() != null) {
                                carParkService.deleteCarType(carParkService.getCarType(carDto.getType().getName()).getCarTypeId());
                            }
                            return Response
                                    .status(Response.Status.BAD_REQUEST)
                                    .build();
                        }
                    }
                }
            }
            User userResponse = carParkService.getUser(user.getUserId());

            UserIdDto userDto1 = userIdFactory.transformToDto(userResponse);
            return Response
                    .status(Response.Status.CREATED)
                    .entity(userDto1)
                    .build();
        } catch (JsonProcessingException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
    }
}
