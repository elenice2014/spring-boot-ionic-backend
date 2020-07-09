package com.elenice.cursomc.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.elenice.cursomc.domain.Cliente;
import com.elenice.cursomc.dto.ClienteDTO;
import com.elenice.cursomc.repositories.ClienteRepository;
import com.elenice.cursomc.services.exceptions.ObjectNotFoundException;

//classe que faz a consulta no repositório
@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	// Busca produto por id, se não existir, lança uma exception
	public Cliente find(Integer id) {
		 Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		} 
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); // instancia um cliente partir do bd
		updateData(newObj, obj); //atualizar os dados do novo objeto criado com base no objeto cm argumento
		return repo.save(newObj);
	}

	public void deleteById(Integer id) {
		find(id);
		try {
			repo.deleteById(id);

		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Não é possível excluir porque há entidades relacionadas!");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
		
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
	
	//método auxiliar da classe, por isso não precisa sar público
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}

}