package sk.stuba.fei.uim.vsa.pr2;

import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;

import java.util.Base64;

public class BasicAuth {
    public static Boolean getAuth(String authHeader){
        try{
            CarParkService carParkService = new CarParkService();
            String base64Encoded = authHeader.substring("Basic ".length());
            String decoded = new String(Base64.getDecoder().decode(base64Encoded));
            User user = carParkService.getUser(Long.parseLong(decoded.split(":")[1]));
            if(user!=null){
                if(user.getEmail().equals(decoded.split(":")[0])){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public static Boolean compareId(String authHeader, Long id){
        try{
            CarParkService carParkService = new CarParkService();
            String base64Encoded = authHeader.substring("Basic ".length());
            String decoded = new String(Base64.getDecoder().decode(base64Encoded));
            User user = carParkService.getUser(Long.parseLong(decoded.split(":")[1]));
            if(user!=null){
                if(user.getEmail().equals(decoded.split(":")[0])){
                    User userCompare = carParkService.getReservationById(id).getCar().getUser();
                    if(userCompare!=null) {
                        if(user.getEmail().equals(userCompare.getEmail())) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
