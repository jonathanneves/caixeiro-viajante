package model;

public class Cidade {

	private String letra;
	private String cidade;
	
	
	public Cidade(String letra, String cidade) {
		this.letra = letra;
		this.cidade = cidade;
	}
		
	@Override
	public String toString() {
		return "(" + letra + ") " + cidade;
	}


	public String getLetra() {
		return letra;
	}
	public void setLetra(String letra) {
		this.letra = letra;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	
}
