package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.businessLogic.SimulationManager;

public class Controller {
    ObservableList<String> strategyType = FXCollections.observableArrayList("Shortest Time","Shortest Length");

    @FXML Button startButton;
    @FXML TextField nrOfClientsTextField;
    @FXML TextField nrOfQueuesTextField;
    @FXML TextField simulationTimeTextField;
    @FXML TextField minArrivalTimeTextField;
    @FXML TextField maxArrivalTimeTextField;
    @FXML TextField minServiceTimeTextField;
    @FXML TextField maxServiceTextField;
    @FXML ChoiceBox<String> strategyChoiceBox = new ChoiceBox<>();
    @FXML Label errorLabel;

    @FXML
    private void startSimulation() {
        SimulationManager simulationManager = new SimulationManager(
                simulationTimeTextField.getText(),
                minServiceTimeTextField.getText(),
                maxServiceTextField.getText(),
                minArrivalTimeTextField.getText(),
                maxArrivalTimeTextField.getText(),
                nrOfClientsTextField.getText(),
                nrOfQueuesTextField.getText(),
                strategyChoiceBox.getValue());
        if(simulationManager.getDataError().equals("")){
            errorLabel.setText("");
        } else {
            errorLabel.setText(simulationManager.getDataError());
        }
    }

    @FXML
    private void initialize(){
        strategyChoiceBox.setItems(strategyType);
        errorLabel.setTextFill(Color.color(1, 0, 0));
    }

}
