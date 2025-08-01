package com.conteduu.planejadorviagens.controller;

import com.conteduu.planejadorviagens.model.Viagem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.swing.table.TableColumn;

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
    private TableColumn colCity;
    @FXML
    private TableColumn colStart;
    @FXML
    private TableColumn colEnd;
    @FXML
    private TableColumn colCost;


}
