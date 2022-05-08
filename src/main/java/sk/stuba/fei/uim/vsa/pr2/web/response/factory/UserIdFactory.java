package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.UserIdDto;

import java.util.stream.Collectors;

public class UserIdFactory implements ResponseFactory<User, UserIdDto> {

    @Override
    public UserIdDto transformToDto(User entity) {
        UserIdDto userDto = new UserIdDto();
        userDto.setId(entity.getUserId());
        userDto.setFirstName(entity.getFirstname());
        userDto.setLastName(entity.getLastname());
        userDto.setEmail(entity.getEmail());
        CarIdFactory carFactory = new CarIdFactory();
        if(entity.getCars()!=null) {
            userDto.setCars(entity.getCars().stream().map(carFactory::transformToDto).collect(Collectors.toList()));
        }
        return userDto;
    }

    @Override
    public User transformToEntity(UserIdDto dto) {
        return null;
    }
}
