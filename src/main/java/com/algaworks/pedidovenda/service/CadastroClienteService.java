package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.Clientes;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroClienteService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;
	
	@Transactional
	public Cliente salvar(Cliente cliente) throws NegocioException {	
		Cliente clienteExistenteEmail = clientes.porEmail(cliente.getEmail());
		Cliente clienteExistenteDoc = clientes.porDocumentoReceitaFederal(cliente.getDocumentoReceitaFederal());
 		
		if (clienteExistenteEmail != null && !clienteExistenteEmail.equals(cliente)) {
			throw new NegocioException("Ja existe um usuario com o Email informado.");
		}
		
		if (clienteExistenteDoc != null && !clienteExistenteDoc.equals(cliente)) {
			throw new NegocioException("Ja existe um usuario com o Documento informado.");
		}
		
		return clientes.guardar(cliente);
	}
	
}
