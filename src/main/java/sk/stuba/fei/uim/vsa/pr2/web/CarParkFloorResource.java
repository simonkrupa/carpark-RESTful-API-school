package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFloorFactory;

@Path("/carparkfloors")
public class CarParkFloorResource {
    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarParkFloorFactory factory = new CarParkFloorFactory();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFloorById(@PathParam("id") Long id){
        if(id==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        try{
            CarParkFloor carParkFloor = carParkService.getCarParkFloor(id);
            if(carParkFloor==null){
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            CarParkFloorDto carParkFloorDto = factory.transformToDto(carParkFloor);
            return Response
                    .status(Response.Status.OK)
                    .entity(carParkFloorDto)
                    .build();
        }catch (Exception e){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

    }
}
