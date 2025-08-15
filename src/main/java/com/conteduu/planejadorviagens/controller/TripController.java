package com.conteduu.planejadorviagens.controller;

import com.conteduu.planejadorviagens.model.Viagem;
import com.conteduu.planejadorviagens.service.PlanejamentoService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

public class TripController {

    Viagem viagemHaAtualizar = new Viagem();

    // Elements FXML
    @FXML
    private DatePicker inicioDatePicker;
    @FXML
    private DatePicker fimDatePicker;
    @FXML
    private TextField destinoField;
    @FXML
    private TextField custoField;
    @FXML
    private Button adicionarViagemButton;
    @FXML
    private Button editarViagemButton;
    @FXML
    private Button salvarMudancasButton;
    @FXML
    private TableView<Viagem> tabelaViagens;
    @FXML
    private TableColumn<Viagem, String> colunaCidade;
    @FXML
    private TableColumn<Viagem, String> colunaDataInicio;
    @FXML
    private TableColumn<Viagem, String> colunaDataFim;
    @FXML
    private TableColumn<Viagem, Number> colunaCustoViagem;

    private final PlanejamentoService planejamentoService = new PlanejamentoService();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {

        // Define como cada coluna extrai informação da entidade

        // Se só devolver uma String simples, o JFX não consegue saber se o valor mudou depois.
        // Por isso se devolver uma SimpleStringProperty ele pode observar aquela propriedade
        // e atualizar a interface automaticamente.
        colunaCidade.setCellValueFactory(colCity ->
                new SimpleStringProperty(colCity.getValue().getDestino())
        );

        colunaDataInicio.setCellValueFactory(start -> {
            System.out.println(start.getValue().getDataInicio());
            return new SimpleStringProperty(start.getValue().getDataInicio().format(formatter));
        });

        colunaDataFim.setCellValueFactory(end ->
                new SimpleStringProperty(end.getValue().getDataFim().format(formatter))
        );

        colunaCustoViagem.setCellValueFactory(cost ->
                new SimpleDoubleProperty(cost.getValue().getCusto())
        );

        // Preencher a tabela com dados já gravados.

        tabelaViagens.setItems(FXCollections.observableArrayList(planejamentoService.listAll()));

        editarViagemButton.setDisable(true);
        salvarMudancasButton.setVisible(false);
    }

    @FXML
    public void adicionarViagem() {
        try {
            double custo = Double.parseDouble(custoField.getText().replace(",", "."));

            planejamentoService.addTrip(
                    destinoField.getText(),
                    inicioDatePicker.getValue(),
                    fimDatePicker.getValue(),
                    custo
            );

            tabelaViagens.getItems().setAll(planejamentoService.listAll());
            atualizarCustoViagem();
            limparCamposFormulario();

        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    public void ativarBotaoEditarViagem(){
        editarViagemButton.setDisable(false);
    }

    @FXML
    public void editarViagem(){
        viagemHaAtualizar = tabelaViagens.getSelectionModel().getSelectedItem();
        carregarDadosFormulario(viagemHaAtualizar);
        salvarMudancasButton.setVisible(true);
    }

    @FXML
    public void salvarMudancaViagem(){
        planejamentoService.salvarAlteracoes(viagemHaAtualizar);
        tabelaViagens.setItems(FXCollections.observableArrayList(planejamentoService.listAll()));
        atualizarCustoViagem();
        limparCamposFormulario();
        editarViagemButton.setDisable(true);
        salvarMudancasButton.setVisible(false);
    }

    private void mostrarErro(String errorMessage) {
        new Alert(Alert.AlertType.ERROR, errorMessage).showAndWait();
    }

    private void atualizarCustoViagem() {
        custoField.setText("Total: R$ " + String.format("%.2f", planejamentoService.totalExpense()));
    }

    private void limparCamposFormulario(){
        destinoField.clear();
        inicioDatePicker.setValue(null);
        fimDatePicker.setValue(null);
        custoField.clear();
    }

    private void carregarDadosFormulario(Viagem trip){
        destinoField.setText(trip.getDestino());
        custoField.setText(String.valueOf(trip.getCusto()));
        inicioDatePicker.setValue(trip.getDataInicio());
        fimDatePicker.setValue(trip.getDataFim());
    }
}
