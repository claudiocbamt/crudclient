package com.devsuperior.crudcliente.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.crudcliente.dto.ClientDTO;
import com.devsuperior.crudcliente.entities.Client;
import com.devsuperior.crudcliente.repositories.ClientRepository;
import com.devsuperior.crudcliente.services.exception.DataBaseException;
import com.devsuperior.crudcliente.services.exception.ResourceNotFoundException;

@Service
public class ClientService {
	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> page = repository.findAll(pageRequest);
		return page.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO Insert(ClientDTO dto) {
		
		Client entity = new Client();
		
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity = repository.save(entity);
		
		return new ClientDTO(entity);
		
	}
	
	@Transactional
	public ClientDTO Update(Long id, ClientDTO dto) {
		try {
		
		Client entity = repository.getReferenceById(id);
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity = repository.save(entity);
		
		return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado " + id);
		}
		
	}
	
	public void Delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado " + id);
		}
		
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Violação de Integridade ");
		}
		
	}


}
