package it.mds.sac.bdvet.bdvetanagrafica.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import it.mds.sac.bdvet.bdvetanagrafica.dto.UserDTO;
import it.mds.sac.bdvet.bdvetanagrafica.entity.UserEntity;
import it.mds.sac.comsvc.abs.mapper.GenericMapper;
import it.mds.sac.comsvc.abs.mapper.GenericMapperFunctions;

@Component
@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {GenericMapperFunctions.class})
public interface UserMapper extends GenericMapper<UserDTO, UserEntity> {

	UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
	
}
