package it.mds.sac.bdvet.bdvetanagrafica.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mds.sac.bdvet.bdvetanagrafica.dto.UserDTO;
import it.mds.sac.bdvet.bdvetanagrafica.entity.UserEntity;
import it.mds.sac.bdvet.bdvetanagrafica.mapper.UserMapper;
import it.mds.sac.bdvet.bdvetanagrafica.service.UserService;
import it.mds.sac.comsvc.abs.controller.AbstractRestController;
import it.mds.sac.comsvc.abs.dto.GenericDTO;
import it.mds.sac.comsvc.abs.mapper.GenericMapper;
import it.mds.sac.comsvc.abs.service.ServiceFacade;

import java.io.Serializable;



// TODO controller tipizzato con i generics, che chiamerà un @Service

/**
 * The type User controller.
 */
@RestController
@RequestMapping(value="/api/v1/bdvet", produces = { MediaType.APPLICATION_JSON_VALUE },headers = {"Authorization"})
public class UserController <ID extends Serializable> extends AbstractRestController<UserDTO, UserEntity, ID>{

	
	
	@Autowired
	public UserController(UserService service) { 
		super((ServiceFacade<UserEntity, ID>) service); 
	}

	@Override
	public GenericMapper<UserDTO, UserEntity> getMapper() {
		return UserMapper.MAPPER;
	}
	
	
	 @GetMapping(value = "/all", produces = {"application/json"}, headers = "Authorization")
	    @Operation(summary = "Retrieve all documents in the Collection", description = "")
	    @ApiResponses(
	            value = {
	                @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
	                            array = @ArraySchema( schema = @Schema(implementation = GenericDTO.class)))),
	                @ApiResponse(responseCode = "200", description = "OK"),
	                @ApiResponse(responseCode = "204", description = "No Content"),
	                @ApiResponse(responseCode = "400", description = "Bad Request"),
	                @ApiResponse(responseCode = "500", description = "Internal Server Error"),
	                @ApiResponse(responseCode = "404", description = "Not Found")
	            }
	    )
	 public ResponseEntity<List<UserDTO>> findAll() {
		 return new ResponseEntity<List<UserDTO>> (((UserService) getService()).getAll(),HttpStatus.OK);
	 }

}
