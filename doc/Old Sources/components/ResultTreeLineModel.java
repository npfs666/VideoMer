package modele;

import components.model.ResultComboBoxModel;

public class ResultTreeLineModel {
	
	private boolean actif;
	private String nom, code;
	private ResultComboBoxModel choix;
	private int type;
	
	public ResultTreeLineModel() {
		actif = false;
		nom = "";
		code = "";
		choix = new ResultComboBoxModel();
		type = 0;
	}
	
	public ResultTreeLineModel(String nom) {
		actif = false;
		this.nom = nom;
		code = "";
		choix = new ResultComboBoxModel();
		type = 0;
	}
	
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ResultComboBoxModel getChoix() {
		return choix;
	}
	public void setChoix(ResultComboBoxModel choix) {
		this.choix = choix;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
