package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.UserDto;

import java.util.stream.Collectors;

public class UserFactory implements ResponseFactory<User, UserDto> {
    @Override
    public UserDto transformToDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setFirstname(entity.getFirstname());
        userDto.setLastname(entity.getLastname());
        userDto.setEmail(entity.getEmail());
        CarFactory carFactory = new CarFactory();
        if(entity.getCars()!=null) {
            userDto.setCars(entity.getCars().stream().map(carFactory::transformToDto).collect(Collectors.toList()));
        }
        return userDto;
    }

    @Override
    public User transformToEntity(UserDto dto) {
        return null;
    }
}
