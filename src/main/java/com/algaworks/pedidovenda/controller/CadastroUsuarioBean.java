package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.service.CadastroUsuarioService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Grupos grupos;

	@Inject
	private CadastroUsuarioService cadastroUsuarioService;

	private Usuario usuario;
	private Grupo grupo;

	private List<Grupo> gruposRaizes;

	public CadastroUsuarioBean() {
		limpar();
	}

	public void inicializar() {

		if (this.usuario == null) {
			limpar();
		}

		gruposRaizes = grupos.raizes();

	}

	private void limpar() {
		usuario = new Usuario();
		grupo = null;
	}

	public void adicionarGrupo() {
		// this.usuario.getGrupos().add(grupo);
		// usuario.getGrupos().add(grupo);
		try {

			if (grupo != null) {
				for (Grupo grupo : usuario.getGrupos()) {

					if (grupo.equals(grupo)) {
						throw new NegocioException("Este grupo já foi adicionado");
					}
				}
				this.usuario.getGrupos().add(grupo);
			}
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}

	}

	public void removerGrupo() {
		this.usuario.getGrupos().remove(grupo);
	}

	public void salvar() {
		try {
			this.usuario = cadastroUsuarioService.salvar(this.usuario);
			limpar();

			FacesUtil.addInfoMessage("Usuário salvo com sucesso");
		} catch (NegocioException ne) {
			FacesUtil.addErrorMessage(ne.getMessage());
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public CadastroUsuarioService getCadastroUsuarioService() {
		return cadastroUsuarioService;
	}

	public void setCadastroUsuarioService(CadastroUsuarioService cadastroUsuarioService) {
		this.cadastroUsuarioService = cadastroUsuarioService;
	}

	public List<Grupo> getGruposRaizes() {
		return gruposRaizes;
	}

	public void setGruposRaizes(List<Grupo> gruposRaizes) {
		this.gruposRaizes = gruposRaizes;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public boolean isEditando() {
		return this.usuario.getId() != null;
	}
}