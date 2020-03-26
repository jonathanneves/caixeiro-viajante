package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import core.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import model.Tempo;

public class TelaController implements Initializable {

	List<String> cidades = new ArrayList<String>();
	List<Tempo> tempos = new ArrayList<Tempo>();
	
	/* TABELA COM CIDADES */
	@FXML
	private TableView<String> tableCidade;
	@FXML
	private TableColumn<String, String> columnCidade;
	
	/* MAPA DE DISTÂNCIA */
	@FXML
	private TableView<Tempo> tableTempo;
	@FXML
	private TableColumn<Tempo, String> columnCidadeA;
	@FXML
	private TableColumn<Tempo, String> columnCidadeB;
	@FXML
	private TableColumn<Tempo, Integer> columnTempo;
	
	@FXML
	private TextField inputCidade;
	@FXML
	private RadioButton rbGeracao;
	@FXML
	private RadioButton rbAuto;
	@FXML
	private TabPane tabPane;
	@FXML
	private Label lblResultado;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnCidade.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
		columnCidadeA.setCellValueFactory(new PropertyValueFactory<>("cidadeA"));
		columnCidadeB.setCellValueFactory(new PropertyValueFactory<>("cidadeB"));
		columnTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
	}

	@FXML
	private void adicionarNovaCidade() {
		if(cidades.size() < 10) {
			String novaCidade = inputCidade.getText();
			
			if(!novaCidade.contains("->") || !novaCidade.isEmpty()) {
				if(!cidades.contains(novaCidade)){
					populaListaETabelaCidade();
				} else {
					exibeMsg("Cidade Duplicada!", 
							"Esta cidade já existe na lista tente outro nome", 
							"Não foi possível salvar a cidade.", 
							AlertType.WARNING);
				}
			} else {
				exibeMsg("Nome de Cidade Inválido!", 
						"Nome da cidade não pode ter '->' ou não pode ser vazio.", 
						"Não foi possível salvar a cidade.", 
						AlertType.WARNING);
			}
		}
	}
	
	@FXML
	private void iniciarAlgoritmoGenetico() {
		
		if(cidades.size() > 2) {
			
			preencherListaTempo();	
			populaTabela();
	
			int geracao = 0;
			if(rbGeracao.isSelected()) {	
				while(geracao == 0) 
					geracao = Integer.parseInt(inputDialog("Quantas gerações você deseja? (Maior que zero)"));
			}
			
			Application app = new Application();
			app.iniciar(geracao, tempos, cidades);
			
			lblResultado.setText(app.melhorIndividuo()+"\n Nrº de gerações: "+app.getGeracao());
				
		} else {
			exibeMsg("Número de Cidade Inválido!", 
					"Deve ter mais que duas cidades para executar o algoritmo.", 
					"Não foi possível executar o algoritmo.", 
					AlertType.WARNING);
		}
	}
	
	private void populaTabela() {
		tableTempo.getItems().clear();
		tableTempo.setItems(FXCollections.observableArrayList(tempos));
		tabPane.getSelectionModel().selectNext();
	}
	
	private void populaListaETabelaCidade() {
		cidades.add(inputCidade.getText());
        tableCidade.setItems(FXCollections.observableArrayList(cidades));
        inputCidade.clear();
	}

	private void preencherListaTempo() {
		tempos.clear();
		for(int i = 0; i<cidades.size(); i++) {
			for(int j = i; j<cidades.size(); j++) {
				if(!cidades.get(i).equals(cidades.get(j))) {
					int tempo = Integer.parseInt(inputDialog("Tempo entre a Cidade '"+cidades.get(i)+"' e a Cidade '"+cidades.get(j)+"'"));
					tempos.add(new Tempo(cidades.get(i),cidades.get(j),tempo));
					tempos.add(new Tempo(cidades.get(j),cidades.get(i),tempo));
				} else {
					tempos.add(new Tempo(cidades.get(i),cidades.get(i),0));
				}
			}
		}
	}
	
	@FXML
	private void deletarLinha() {
		String cidadeSelecionada = tableCidade.getSelectionModel().getSelectedItem();
		cidades.remove(cidadeSelecionada);
		tableCidade.setItems(FXCollections.observableArrayList(cidades));
	}
	
	private void exibeMsg(String titulo, String cabecalho, String msg, AlertType tipo) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(cabecalho);
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	private String inputDialog(String header) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Cadastro");
        dialog.setHeaderText(header);

        return dialog.showAndWait().get();
    }

}
