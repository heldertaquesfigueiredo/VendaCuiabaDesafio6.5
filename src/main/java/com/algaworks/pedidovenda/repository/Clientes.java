package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;


public class Clientes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Cliente guardar(Cliente cliente) {	
		return manager.merge(cliente);
	}
	
	@Transactional
	public void remover(Cliente cliente) throws NegocioException {
		try {
			cliente = porId(cliente.getId());
			manager.remove(cliente);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Cliente não pode ser excluído.");
		}
	}
	
	
	
	public Cliente porDocumentoReceitaFederal(String documentoReceitaFederal) {
		try {
			return manager.createQuery("from Cliente where upper(documentoReceitaFederal) = :documentoReceitaFederal", Cliente.class)
					.setParameter("documentoReceitaFederal", documentoReceitaFederal.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Cliente porEmail(String email) {
		try {
			return manager.createQuery("from Cliente where upper(email) = :email", Cliente.class)
				.setParameter("email", email.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);
		
		
		if (StringUtils.isNotBlank(filtro.getDocumentoReceitaFederal())) {
			criteria.add(Restrictions.eq("documentoReceitaFederal", filtro.getDocumentoReceitaFederal()));
		}
		
		if (StringUtils.isNotBlank(filtro.getEmail())) {
			criteria.add(Restrictions.eq("email", filtro.getEmail()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNumero())) {
			criteria.add(Restrictions.eq("numero", filtro.getNumero()));
		}
		
		if (StringUtils.isNotBlank(filtro.getTipo())) {
			criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
		}
			
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		return criteria.addOrder(Order.asc("nome")).list();
	}
	
	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);
	}
	
	public List<Cliente> porNome(String nome) {
		return this.manager.createQuery("from Cliente " +
				"where upper(nome) like :nome", Cliente.class)
				.setParameter("nome", nome.toUpperCase() + "%")
				.getResultList();
	}
	
}
