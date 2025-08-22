package com.conteduu.planejadorviagens.controller;

import com.conteduu.planejadorviagens.model.Viagem;
import com.conteduu.planejadorviagens.service.PlanejamentoService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TripController {
    // Elements FXML
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField costField;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button btnEditarViagem;
    @FXML
    private TableView<Viagem> tripTable;
    @FXML
    private TableColumn<Viagem, String> colCity;
    @FXML
    private TableColumn<Viagem, String> colStart;
    @FXML
    private TableColumn<Viagem, String> colEnd;
    @FXML
    private TableColumn<Viagem, Number> colCost;

    private final PlanejamentoService planejamentoService = new PlanejamentoService();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Viagem viagemEmEdicao = null;
    private boolean modoEdicao = false;

    @FXML
    private void initialize() {
        // Define como cada coluna extrai informação da entidade

        // Se só devolver uma String simples, o JFX não consegue saber se o valor mudou depois.
        // Por isso se devolver uma SimpleStringProperty ele pode observar aquela propriedade
        // e atualizar a interface automaticamente.
        colCity.setCellValueFactory( colCity ->
                new SimpleStringProperty(colCity.getValue().getDestino())
        );

        colStart.setCellValueFactory( start ->
                        new SimpleStringProperty(start.getValue().getDataInicio().format(formatter))
        );

        colEnd.setCellValueFactory(end ->
                new SimpleStringProperty(end.getValue().getDataFim().format(formatter))
        );

        colCost.setCellValueFactory(cost ->
                new SimpleDoubleProperty(cost.getValue().getCusto())
        );

        tripTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldViagem, novaViagem) -> {
            if (novaViagem != null){
                destinationField.setText(novaViagem.getDestino());
                startDatePicker.setValue(novaViagem.getDataInicio());
                endDatePicker.setValue(novaViagem.getDataFim());
                costField.setText(String.format(Locale.US, "%.2f", novaViagem.getCusto()));
            }
        });

        // Preencher a tabela com dados já gravados.

        tripTable.setItems(FXCollections.observableArrayList(planejamentoService.listAll()));
        updateCost();
    }

    @FXML
    public void editarViagem(){
        Viagem viagemSelecionada = tripTable.getSelectionModel().getSelectedItem();
        if (viagemSelecionada == null){
            mostrarErro("Selecione uma viagem na tabela para editar");
            return;
        }
        try {
            double custo = converteCustoStringParaDouble(costField.getText());

            planejamentoService.atualizarViagem(
                    viagemSelecionada,
                    destinationField.getText(),
                    startDatePicker.getValue(),
                    endDatePicker.getValue(),
                    custo
            );
        }catch (Exception e){
            mostrarErro(e.getMessage());
        }

        tripTable.getItems().setAll(planejamentoService.listAll());
        updateCost();
        limparCamposFormulario();
        tripTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void addTrip() {
        try {
            double cost = converteCustoStringParaDouble(costField.getText());
            planejamentoService.addTrip(
                    destinationField.getText(),
                    startDatePicker.getValue(),
                    endDatePicker.getValue(),
                    cost
            );

            tripTable.getItems().setAll(planejamentoService.listAll());
            updateCost();
            limparCamposFormulario();
        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    private void mostrarErro(String errorMessage) {
        new Alert(Alert.AlertType.ERROR, errorMessage).showAndWait();
    }

    private void updateCost() {
        costField.setText("Total: R$ " + String.format("%.2f", planejamentoService.totalExpense()));
    }

    private double converteCustoStringParaDouble(String custoString) {
        return Double.parseDouble(
                custoString.replace(",", ".")
        );
    }

    private void limparCamposFormulario(){
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        destinationField.clear();
        costField.clear();
    }

}
