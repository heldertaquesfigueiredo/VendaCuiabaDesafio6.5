package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.TipoPessoa;
import com.algaworks.pedidovenda.service.CadastroClienteService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroClienteService cadastroClienteService;

	private Cliente cliente;
	private Endereco endereco;

	private String labelTipo;
	private String inputMask;

	public CadastroClienteBean() {
		limpar();
	}

	public void inicializar() {

		if (this.cliente == null) {
			limpar();
		}

	}

	public void verificaTipo() {
		if (this.cliente.getTipo().getDescricao().equals(TipoPessoa.FISICA.getDescricao())) {
			labelTipo = "CPF:";
			inputMask = "999.999.999-99";
		} else {
			labelTipo = "CNPJ:";
			inputMask = "99.999.999/9999-99";
		}
	}

	public void salvar() {
		try {
			this.cliente = cadastroClienteService.salvar(this.cliente);
			limpar();
			FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}

	}

	public void adicionarEndereco() {

		endereco.setCliente(cliente);
		cliente.getEnderecos().add(endereco);
		endereco = new Endereco();

	}

	public void excluirEndereco() {
		this.cliente.getEnderecos().remove(endereco);
	}

	public Cliente getCliente() {
		if (cliente == null) {
			cliente = new Cliente();
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;

	}

	private void limpar() {
		cliente = new Cliente();
		endereco = new Endereco();
	}

	public TipoPessoa[] getTipoPessoa() {
		return TipoPessoa.values();
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getLabelTipo() {
		return labelTipo;
	}

	public String getInputMask() {
		return inputMask;
	}

	public boolean isEditando() {
		return this.cliente.getId() != null;
	}
}