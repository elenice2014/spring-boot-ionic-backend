package com.elenice.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.elenice.cursomc.domain.Cidade;
import com.elenice.cursomc.domain.Cliente;
import com.elenice.cursomc.domain.Endereco;
import com.elenice.cursomc.domain.enums.Perfil;
import com.elenice.cursomc.domain.enums.TipoCliente;
import com.elenice.cursomc.dto.ClienteDTO;
import com.elenice.cursomc.dto.ClienteNewDTO;
import com.elenice.cursomc.repositories.ClienteRepository;
import com.elenice.cursomc.repositories.EnderecoRepository;
import com.elenice.cursomc.security.UserSS;
import com.elenice.cursomc.services.exceptions.AuthorizationException;
import com.elenice.cursomc.services.exceptions.ObjectNotFoundException;

//classe que faz a consulta no repositório
@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;

	
	// Busca produto por id, se não existir, lança uma exception
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {  
			throw new AuthorizationException("Acesso negado");
		}
		
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
			throw new DataIntegrityViolationException("Não é possível excluir porque há pedidos relacionadas!");
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
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), pe.encode(objDTO.getSenha())); //senha sendo encodada
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if (objDTO.getTelefone2()!= null) {
			cli.getTelefones().add(objDTO.getTelefone2());

		}
		
		if (objDTO.getTelefone3()!= null) {
			cli.getTelefones().add(objDTO.getTelefone3());

		}
		
		return cli;
	}
	
	//método auxiliar da classe, por isso não precisa sar público
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

}