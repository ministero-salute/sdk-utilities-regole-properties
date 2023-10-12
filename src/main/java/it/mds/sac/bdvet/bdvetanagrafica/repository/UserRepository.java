package it.mds.sac.bdvet.bdvetanagrafica.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import it.mds.sac.bdvet.bdvetanagrafica.entity.UserEntity;

public interface UserRepository  <ID extends Serializable> extends JpaRepository<UserEntity, ID>, JpaSpecificationExecutor<UserEntity> {

	
}
