package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotFactory;

import static sk.stuba.fei.uim.vsa.pr2.BasicAuth.getAuth;

@Path("/parkingspots")
public class ParkingSpotResource {

    private static final String EMPTY_RESPONSE = "{}";

    private final CarParkService carParkService = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final ParkingSpotFactory factory = new ParkingSpotFactory();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpotById(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id){
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
            ParkingSpot parkingSpot = carParkService.getParkingSpot(id);
            if (parkingSpot == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
            ParkingSpotDto parkingSpotDto = factory.transformToDto(parkingSpot);
            return Response
                    .status(Response.Status.OK)
                    .entity(parkingSpotDto)
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
        ParkingSpot parkingSpot = carParkService.deleteParkingSpot(id);
        if(parkingSpot==null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
