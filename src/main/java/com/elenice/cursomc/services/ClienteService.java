package com.elenice.cursomc.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elenice.cursomc.domain.Cliente;
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

}