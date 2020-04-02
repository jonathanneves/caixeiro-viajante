package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import core.AlgoritmoGenetico;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import model.Cidade;
import model.Tempo;

public class TelaController implements Initializable {

	List<Cidade> cidades = new ArrayList<Cidade>();
	List<Tempo> tempos = new ArrayList<Tempo>();
	
	/* TABELA COM CIDADES */
	@FXML
	private TableView<Cidade> tableCidade;
	@FXML
	private TableColumn<Cidade, String> columnCidade;
	@FXML
	private TableColumn<Cidade, String> columnLetra;
	
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
		columnCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
		columnLetra.setCellValueFactory(new PropertyValueFactory<>("letra"));
		columnCidadeA.setCellValueFactory(new PropertyValueFactory<>("cidadeA"));
		columnCidadeB.setCellValueFactory(new PropertyValueFactory<>("cidadeB"));
		columnTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
		
		columnCidade.setCellFactory(TextFieldTableCell.<Cidade>forTableColumn());
		columnCidade.setOnEditCommit(new EventHandler<CellEditEvent<Cidade, String>>() 
		{
		    @Override
		    public void handle(CellEditEvent<Cidade, String> t) 
		    {
		        ((Cidade) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setCidade(t.getNewValue());
		        cidades.sort(Comparator.comparing(Cidade::getLetra));
		    	tableCidade.setItems(FXCollections.observableArrayList(cidades));
		    }
		});
		
		columnTempo.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		columnTempo.setOnEditCommit(new EventHandler<CellEditEvent<Tempo, Integer>>() 
		{
		    @Override
		    public void handle(CellEditEvent<Tempo, Integer> t) 
		    {
		        ((Tempo) t.getTableView().getItems().get(
		                t.getTablePosition().getRow())
		                ).setTempo(t.getNewValue());
		    	tableTempo.setItems(FXCollections.observableArrayList(tempos));
		    }
		});
		
	}

	@FXML
	private void adicionarNovaCidade() {
		if(cidades.size() < 10) {
			String novaCidade = inputCidade.getText();
			
			if(!novaCidade.contains("->") && !novaCidade.isEmpty()) {
					populaListaETabelaCidade(novaCidade);
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
			
			int geracao = 0;
			if(rbGeracao.isSelected()) {	
				while(geracao == 0) 
					geracao = Integer.parseInt(inputDialog("Quantas gerações você deseja? (Maior que zero)"));
			}
			
			cidades.sort(Comparator.comparing(Cidade::getLetra));
			preencherListaTempo();	
			populaTabela();
	
			AlgoritmoGenetico app = new AlgoritmoGenetico();
			app.iniciar(geracao, tempos, cidades);
			
			lblResultado.setText(app.melhorIndividuo());
				
		} else {
			exibeMsg("Número de Cidade Inválido!", 
					"Deve ter mais que duas cidades para executar o algoritmo.", 
					"Não foi possível executar o algoritmo.", 
					AlertType.WARNING);
		}
	}
	
	@FXML
	private void limparTudo() {
		tempos.clear();
		cidades.clear();
        tableCidade.setItems(FXCollections.observableArrayList(cidades));
		tableTempo.setItems(FXCollections.observableArrayList(tempos));
		lblResultado.setText("");
	}
	
	private void populaTabela() {
		tableTempo.getItems().clear();
		tableTempo.setItems(FXCollections.observableArrayList(tempos));
		tabPane.getSelectionModel().selectNext();
	}
	
	private void populaListaETabelaCidade(String novaCidade) {
		cidades.add(new Cidade(Character.toString((char)(65+cidades.size())),novaCidade));
		Collections.sort(cidades, Comparator.comparing(Cidade::getLetra));
        tableCidade.setItems(FXCollections.observableArrayList(cidades));
        inputCidade.clear();
		tableTempo.getItems().clear();
		tempos.clear();
	}
	
	private void preencherListaTempo() {

		if(tempos.isEmpty()) {
			for(int i = 0; i<cidades.size(); i++) {
				for(int j = i; j<cidades.size(); j++) {
					if(!cidades.get(i).equals(cidades.get(j))) {
						int tempo = Integer.parseInt(inputDialog("Tempo em minutos entre a Cidade '"+cidades.get(i).getCidade()+"' e a Cidade '"+cidades.get(j).getCidade()+"'"));
						tempos.add(new Tempo(cidades.get(i),cidades.get(j),tempo));				
					} else {
						tempos.add(new Tempo(cidades.get(i),cidades.get(i),0));
					}
				}
			}
		}
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
