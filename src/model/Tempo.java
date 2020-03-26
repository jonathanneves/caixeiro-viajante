package model;

public class Tempo {

	String cidadeA;
	String cidadeB;
	int tempo;
	
	
	public Tempo(String cidadeA, String cidadeB, int tempo) {
		this.cidadeA = cidadeA;
		this.cidadeB = cidadeB;
		this.tempo = tempo;
	}
	
	
	@Override
	public String toString() {
		return "Distancia [cidadeA=" + cidadeA + ", cidadeB=" + cidadeB + ", Tempo=" + tempo + "min.]";
	}

	public String getCidadeA() {
		return cidadeA;
	}
	public void setCidadeA(String cidadeA) {
		this.cidadeA = cidadeA;
	}
	public String getCidadeB() {
		return cidadeB;
	}
	public void setCidadeB(String cidadeB) {
		this.cidadeB = cidadeB;
	}

	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	
}
