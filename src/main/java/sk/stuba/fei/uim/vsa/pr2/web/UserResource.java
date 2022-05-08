package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final UserFactory factory = new UserFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("email") String email){
            try {
                if (email != null) {
                    User user = carParkService.getUser(email);
                    if (user == null) {
                        return Response
                                .status(Response.Status.NOT_FOUND)
                                .build();
                    }
                    List<UserDto> userDtos = new ArrayList<>();
                    userDtos.add(factory.transformToDto(user));
                    return Response
                            .status(Response.Status.OK)
                            .entity(userDtos)
                            .build();
                }
                List<User> users = carParkService.getUsers();
                if (users == null) {
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .build();
                }
                List<UserDto> userDtos = users.stream().map(factory::transformToDto).collect(Collectors.toList());
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
    public Response getById(@PathParam("id") Long id){
        try {
            User user = carParkService.getUser(id);
            if (user == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            UserDto userDto = factory.transformToDto(user);
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
    public Response delete(@PathParam("id") Long id){
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
    public Response create(String body){
        try{
            UserDto userDto = json.readValue(body, UserDto.class);
            User user = carParkService.createUser(userDto.getFirstname(), userDto.getLastname(), userDto.getEmail());
            if(user==null){
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .build();
            }
            if(userDto.getCars()!=null){
                for(CarDto carDto : userDto.getCars()){
                    Car car = carParkService.createCar(user.getUserId(), carDto.getBrand(), carDto.getModel(), carDto.getColour(), carDto.getVrp());
                    if(car==null){
                        carParkService.deleteUser(user.getUserId());
                        return Response
                                .status(Response.Status.BAD_REQUEST)
                                .build();
                    }
                }
            }
            User userResponse = carParkService.getUser(user.getUserId());
            UserDto userDto1 = factory.transformToDto(userResponse);
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
