package model;

public class Tempo {

	Cidade cidadeA;
	Cidade cidadeB;
	int tempo;
	
	
	public Tempo(Cidade cidadeA, Cidade cidadeB, int tempo) {
		this.cidadeA = cidadeA;
		this.cidadeB = cidadeB;
		this.tempo = tempo;
	}
	
	@Override
	public String toString() {
		return "Distancia [cidadeA=" + cidadeA.getCidade() + ", cidadeB=" + cidadeB.getCidade() + ", Tempo=" + tempo + "min.]";
	}

	
	public Cidade getCidadeA() {
		return cidadeA;
	}


	public void setCidadeA(Cidade cidadeA) {
		this.cidadeA = cidadeA;
	}


	public Cidade getCidadeB() {
		return cidadeB;
	}


	public void setCidadeB(Cidade cidadeB) {
		this.cidadeB = cidadeB;
	}


	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	
}
