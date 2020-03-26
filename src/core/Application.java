package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import model.Cromossomo;
import model.Tempo;

public class Application {

	public int geracao = 0;

	public List<Cromossomo> cromossomos = new ArrayList<Cromossomo>(); //Permutação
	public List<String> cidades = new ArrayList<String>();
	public List<Tempo> tempos = new ArrayList<Tempo>();
	
	//INICIANDO TODO O PROCESSO DO ALGORITMO GENETICO POR GERAÇÕES
	public void iniciar(int geracaoMax, List<Tempo> tempos, List<String> cidades) {	
		this.tempos = tempos;
		this.cidades = cidades;
	
		gerarPopulacao();
		//INICIANDO MUTAÇÂO e CROSSOVER
		if(geracaoMax > 0) {
			while(geracao < geracaoMax) {
				System.out.println("--------------------GERAÇÃO: "+(geracao+1)+"----------------------");
				iniciarAlgoritmoGenetico();
			}
		}
		else {
			int menorFitness = 999;
			int contador = 0;
			while(contador<15) {
				System.out.println("--------------------GERAÇÃO: "+(geracao+1)+"----------------------");
				iniciarAlgoritmoGenetico();
				contador++;
				Cromossomo c = Collections.min(cromossomos, Comparator.comparing(s -> s.getFitness()));
				if(c.getFitness() < menorFitness) {
					menorFitness = c.getFitness();
					contador = 0;
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
	
	//GERAR 2 SEQUÊNCIAS ALEATORIA DE CAMINHOS INICIAIS
	public void gerarPopulacao() {
		System.out.println("-----------------GERANCO POPULAÇÃO---------------------");
		for(int i=0; i<2; i++) {
			Collections.shuffle(cidades);
			String caminho = "";			
			//SEPARA CAMINHOS COM ;
			for(String c : cidades) {
				caminho += c+"->";
			}
			
			cromossomos.add(new Cromossomo(caminho, tempos));
		}	
		cromossomos.forEach(System.out::println);
	}
	
	//GERA 2 INDIVIDUOS NOVOS ATRAVÉS DO CROSSOVER
	public void crossover() {	
	
		System.out.println("----------------------CROSSOVER----------------------");
		
		Random gerador = new Random();
		int corte = gerador.nextInt(cidades.size()); 
		System.out.println("Corte: "+corte);
		
		String[] cidades1 = cromossomos.get(0).getCaminho().split("->");
		String[] cidades2 = cromossomos.get(1).getCaminho().split("->");
				
		//REALIZANDO O CROSSOVER ENTRE OS 2 CROMOSSOMOS (OPERADOR PMX)
		String crossover1 = "";
		String crossover2 = "";
		
		crossover1 = "";
		crossover2 = "";
			
		for(int i=0; i<cidades.size(); i++) {
			if(i<=corte) {
				crossover1 += cidades1[i]+"->";
				crossover2 += cidades2[i]+"->";
			}else{
				crossover2 += cidades1[i]+"->";
				crossover1 += cidades2[i]+"->";
			}
		}
		
		String[] crossovers1 = crossover1.split("->");
		String[] crossovers2 = crossover2.split("->");
		
		int posInvalido1 = -1;
		int posInvalido2 = -1;
		//VERIFICANDO SE É CAMINHO VÁLIDO
		for(int i=0; i<cidades.size(); i++) {
			for(int j=i+1; j<cidades.size(); j++) {
				if(i != j) {
					if(crossovers1[i].equals(crossovers1[j])) 
						posInvalido1 = j;
					if(crossovers2[i].equals(crossovers2[j])) 
						posInvalido2 = j;
				}
			}
		}
		System.out.println(posInvalido1 + "---"+posInvalido2);

		//FAZENDO A TROCA DE CAMINHOS INVALIDOS (REPETINDO CIDADE)
		for(String c : cidades) {
			Boolean jaExiste1 = false;
			Boolean jaExiste2 = false;
			for(int i=0; i<cidades.size(); i++) {
				if(c.equals(crossovers1[i]))
					jaExiste1=true;
				if(c.equals(crossovers2[i]))
					jaExiste2=true;
			}
			if(!jaExiste1 && posInvalido1 != -1) {
				crossovers1[posInvalido1] = c;
				posInvalido1 = -1;
			}
			if(!jaExiste2 && posInvalido2 != -1) {
				crossovers2[posInvalido2] = c;	
				posInvalido2 = -1;
			}

		}
		
		crossover1 = "";
		crossover2 = "";
		for(int i=0; i<cidades.size(); i++) {
			crossover1 += crossovers1[i]+"->";
			crossover2 += crossovers2[i]+"->";
		}
		
		//Adicinando o novo Crossover 1 a lista de Cromossomos
		cromossomos.add(new Cromossomo(crossover1, tempos));
			
		//Adicinando o novo Crossover 2 a lista de Cromossomos
		cromossomos.add(new Cromossomo(crossover2, tempos));

		cromossomos.forEach(System.out::println);
	}

	public void mutacao() {
		System.out.println("----------------------MUTAÇÃO----------------------");
		Random gerador = new Random();
		Collections.shuffle(cromossomos);
		
		int posA = 0; 
		int posB = 0;
		while(posA == posB) {
			posA = gerador.nextInt(cidades.size());
			posB = gerador.nextInt(cidades.size());
		}
		
		String[] mutacao =  cromossomos.get(0).getCaminho().split("->");
		//Realizando a troca de duas posições em apenas um cromossomo.
		String cidadeAux = mutacao[posA];
		mutacao[posA] = mutacao[posB];
		mutacao[posB] = cidadeAux;
		
		String novaMutacao = "";
		for(int j=0; j<mutacao.length; j++) {
			novaMutacao += mutacao[j]+"->";
		}
		
		cromossomos.set(0, new Cromossomo(novaMutacao, tempos));

		cromossomos.forEach(System.out::println);
	}
	
	//REMOVE OS DOIS CROMOSSOMOS COM MAIOR DISTÂNCIA
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
		Cromossomo c = Collections.min(cromossomos, Comparator.comparing(s -> s.getFitness()));
		System.out.println("O Menor "+c);
		return "O Menor "+c;
	}
	
	public int getGeracao() {
		return geracao;
	}
}
