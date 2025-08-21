package com.conteduu.planejadorviagens.controller;

import com.conteduu.planejadorviagens.model.Viagem;
import com.conteduu.planejadorviagens.service.PlanejamentoService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

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
    private TableView<Viagem> tripTable;
    @FXML
    private TableColumn<Viagem, String> colCity;
    @FXML
    private TableColumn<Viagem, String> colStart;
    @FXML
    private TableColumn<Viagem, String> colEnd;
    @FXML
    private TableColumn<Viagem, String> colCost;

    private final PlanejamentoService planejamentoService = new PlanejamentoService();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialized() {
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
                new SimpleStringProperty(cost.getValue().getDataFim().format(formatter))
        );

        // Preencher a tabela com dados já gravados.

        tripTable.setItems(FXCollections.observableArrayList(planejamentoService.listAll()));

    }

    @FXML
    public void addTrip() {
        try {
            double cost = Double.parseDouble(
                    costField.getText().replace(",", ".")
            );
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

}
