package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.Cidade;
import model.Cromossomo;
import model.Tempo;

public class Application {

	public int geracao = 0;

	public List<Cromossomo> cromossomos = new ArrayList<Cromossomo>(); //Permuta��o
	public List<Cidade> cidades = new ArrayList<Cidade>();
	public List<Tempo> tempos = new ArrayList<Tempo>();
	
	//INICIANDO TODO O PROCESSO DO ALGORITMO GENETICO POR GERA��ES
	public void iniciar(int geracaoMax, List<Tempo> tempos, List<Cidade> cidades) {	
		this.tempos = tempos;
		this.cidades = cidades;
	
		gerarPopulacao();
		//INICIANDO MUTA��O e CROSSOVER
		if(geracaoMax > 0) {	//POR NUMERO DE GERA��ES
			while(geracao < geracaoMax) {
				System.out.println("--------------------GERA��O: "+(geracao+1)+"----------------------");
				iniciarAlgoritmoGenetico();
			}
		}
		else {	//AUTOM�TICO: ELE S� FINALIZA SE NAO ACHAR MELHOR RESULTADO EM 50 GERA��ES
			int menorFitness = Integer.MAX_VALUE;
			int contador = 0;
			while(contador<=50) {
				System.out.println("--------------------GERA��O: "+(geracao+1)+"----------------------");
				iniciarAlgoritmoGenetico();
				contador++;
				Cromossomo c = Collections.min(cromossomos, Comparator.comparing(s -> s.getFitness()));
				if(c.getFitness() < menorFitness) {
					menorFitness = c.getFitness();
					contador = 0;
					System.out.println(">>>"+menorFitness);
				}
			}
		}
	}
	
	public void iniciarAlgoritmoGenetico() {
		crossover();
		mutacao();
		manterMaisApto();
		geracao++;
	}
	
	//GERAR 2 SEQU�NCIAS ALEATORIA DE CAMINHOS INICIAIS
	public void gerarPopulacao() {
		System.out.println("-----------------GERANCO POPULA��O---------------------");
		for(int i=0; i<2; i++) {
			Collections.shuffle(cidades);
			String caminho = "";		
			
			//SEPARA CIDADES COM ->
			for(Cidade c : cidades) {
				caminho += c.getLetra()+"->";
			}
			
			cromossomos.add(new Cromossomo(caminho, tempos));
		}	
		cromossomos.forEach(System.out::println);
	}
	
	//GERA 2 INDIVIDUOS NOVOS ATRAV�S DO CROSSOVER
	public void crossover() {	
	
		System.out.println("----------------------CROSSOVER----------------------");

		String[] parent1 = cromossomos.get(0).getCaminho().split("->");
		String[] parent2 = cromossomos.get(1).getCaminho().split("->");
		
		//Gerar 2 filhos atrav�s do crossover NWOX
		String[] child1 = crossoverNWOX(parent1, parent2);
		String[] child2 = crossoverNWOX(parent2, parent1);
		
		//Gerando a String caminho atrav�s do vetor
		String crossover1 = "";
		String crossover2 = "";
		for(int k=0; k<cidades.size(); k++) {
			crossover1 += child1[k]+"->";
			crossover2 += child2[k]+"->";
		}

		cromossomos.add(new Cromossomo(crossover1, tempos));
		cromossomos.add(new Cromossomo(crossover2, tempos));

		cromossomos.forEach(System.out::println);
	}
	
	//Non-Wrapping Ordered Crossover (NWOX)
	private String[] crossoverNWOX(String[] parent1, String[] parent2) {
		String[] child = new String[cidades.size()];
		
		//Pega 2 posi��es aleatorios parar criar um novo crossover
		Random gerador = new Random();		
		int start = 0, end = 0;
		start = gerador.nextInt(cidades.size()-1);
		end = gerador.nextInt(((cidades.size()-1)-start)+1)+start; //Numero entre start e o m�ximo
		
		//Gera 1 crossover pegando as cidades entre o ponto inicial at� o ponto final
		int index=0;
		for(int i=0; i<cidades.size(); i++) {
			if(i>=start && i<=end) {
				child[index] = parent1[i];
				index++;
			}
		}

		//Verifica se o crossover contem a cidade do outro pai, sen�o completa a lista adicionando-o
		for(int i=0; i<cidades.size(); i++) {
			if(!Arrays.asList(child).contains(parent2[i])) {
				child[index] = parent2[i];
				index++;
			}
		}
		
		return child;
	}

	//REALIZA MUTA��O NO PIOR INDIVIDUO
	public void mutacao() {
		System.out.println("----------------------MUTA��O----------------------");

		Cromossomo c = Collections.max(cromossomos, Comparator.comparing(s -> s.getFitness()));
		Random gerador = new Random();
		
		//Pegando duas posi��es aleat�rias diferentes para trocar de lugar durante a muta��o
		int posA = 0; 
		int posB = 0;
		while(posA == posB) {
			posA = gerador.nextInt(cidades.size()-1);
			posB = gerador.nextInt(cidades.size()-1);
		}
		
		String[] mutacao = c.getCaminho().split("->");
		
		//Realizando a troca de duas posi��es em apenas um cromossomo.
		String cidadeAux = mutacao[posA];
		mutacao[posA] = mutacao[posB];
		mutacao[posB] = cidadeAux;
		
		//Gerando a String caminho atrav�s do vetor
		String novaMutacao = "";
		for(int j=0; j<mutacao.length; j++) {
			novaMutacao += mutacao[j]+"->";
		}

		cromossomos.set(cromossomos.indexOf(c),new Cromossomo(novaMutacao, tempos));

		cromossomos.forEach(System.out::println);
	}
	
	//REMOVE OS DOIS CROMOSSOMOS COM MAIOR DIST�NCIA
	public void manterMaisApto() {
		System.out.println("------CROMOSSOMOS SELECIONADOS COMO MELHORES-------");
		for(int i=0; i<2; i++) {
			Cromossomo c = Collections.max(cromossomos, Comparator.comparing(s -> s.getFitness()));
			cromossomos.remove(c);
		}
		cromossomos.forEach(System.out::println);
	}
			
	public String melhorIndividuo() {
		System.out.println("-----------MELHOR CAMINHO-------------");
		Cromossomo result = Collections.min(cromossomos, Comparator.comparing(s -> s.getFitness()));
		System.out.println("O Menor "+result+" Nr� de gera��es: \"+geracao");
		
		String caminho = "";
		String[] mutacao = result.getCaminho().split("->");
		for(Cidade cid : cidades) {
			for(int i = 0; i<cidades.size(); i++) {
				if(mutacao[i].equals(cid.getLetra()))
					caminho += cid.getCidade() +"->";
			}
		}
		caminho += cidades.get(0).getCidade();
		return "Melhor Caminho: "+caminho+"\nDist�ncia Total: "+result.getFitness()+"\nNr� de gera��es: "+geracao;
	}
}
