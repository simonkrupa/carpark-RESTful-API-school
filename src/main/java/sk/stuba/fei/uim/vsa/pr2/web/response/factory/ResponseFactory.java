package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.web.response.dtos.Dto;

public interface ResponseFactory<R, T extends Dto> {

    T transformToDto(R entity);

    R transformToEntity(T dto);
}
