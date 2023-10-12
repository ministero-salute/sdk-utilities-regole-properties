package it.mds.sac.bdvet.bdvetanagrafica.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import it.mds.sac.bdvet.bdvetanagrafica.dto.UserDTO;
import it.mds.sac.bdvet.bdvetanagrafica.entity.UserEntity;
import it.mds.sac.bdvet.bdvetanagrafica.repository.UserRepository;
import it.mds.sac.comsvc.abs.service.AbstractServiceFacade;
import lombok.extern.log4j.Log4j2;
import it.mds.sac.bdvet.bdvetanagrafica.mapper.UserMapper;
@Service
@Log4j2
public class UserService<ID extends Serializable> extends AbstractServiceFacade<UserEntity, ID> {

	@Autowired
	UserRepository repo;

	public List<UserDTO> getAll(){
		List<UserEntity> le= getRepository().findAll();
		return (List<UserDTO>) UserMapper.MAPPER.collectionEbToCollectionDto(le);
	}

	@Override
	protected JpaRepository<UserEntity, ID> getRepository() {
		return repo;
	}

}
