package com.elenice.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.elenice.cursomc.domain.Cliente;
import com.elenice.cursomc.dto.ClienteDTO;
import com.elenice.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {

		Cliente obj = service.find(id);
		return ResponseEntity.ok().body(obj);

	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
		service.deleteById(id);		
		return ResponseEntity.noContent().build();  //quando deletar, devolve o código que foi deletado, porem sem conteúdo

	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity <List<ClienteDTO>> findAll() {

		List<Cliente> list = service.findAll();
		List<ClienteDTO> listDTO = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());  //converter uma lista para outra lista
		
		return ResponseEntity.ok().body(listDTO);

	}
	
	
	/**
	 * Método para responsável pela paginação com parametros opcionais na requisição.
	 * 
	 */
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity <Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC")String direction) {

		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listDTO = list.map(obj -> new ClienteDTO(obj));  //converter uma lista para outra lista
		
		return ResponseEntity.ok().body(listDTO);

	}
}
