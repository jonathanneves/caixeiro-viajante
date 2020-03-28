package model;

import java.util.List;

public class Cromossomo {

	private String caminho;
	private int fitness;
	
	public Cromossomo(String caminho, List<Tempo> tempos) {
		this.caminho = caminho;
		this.fitness = calcularFitnessTotal(tempos);
	}
		
	public Cromossomo(String caminho, int fitness) {
		this.caminho = caminho;
		this.fitness = fitness;
	}
		
	@Override
	public String toString() {
		String[] cidades = caminho.split("->");
		return "Caminho= "+ caminho +""+cidades[0]+ " Distância Total= " + fitness;
	}

	public int calcularFitnessTotal(List<Tempo> tempos) {
		int fitness = 0;
		String[] cidades = caminho.split("->");
		for(Tempo t : tempos){
			for(int j=0; j<cidades.length-1; j++) {
				if(t.getCidadeA().getLetra().equals(cidades[j]) 
						&& t.getCidadeB().getLetra().equals(cidades[j+1])) {
					fitness += t.getTempo();
				}
			}
			//Adicionar o tempo da ultima cidade com a primeira
			if(t.getCidadeA().getLetra().equals(cidades[cidades.length-1]) 
					&& t.getCidadeB().getLetra().equals(cidades[0])) {
				fitness += t.getTempo();
			}
		}
		return fitness;
	}
	
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	public int getFitness() {
		return fitness;
	}
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	
}
