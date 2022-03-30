package org.example;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.businessLogic.SimulationManager;

public class SimulationView extends Stage{
    private GridPane gridPane = new GridPane();
    ScrollPane scrollPane = new ScrollPane();
    Scene scene = new Scene(scrollPane);

    public SimulationView(){
        this.setHeight(600);
        this.setWidth(600);
        this.setTitle("Real time queue");
        this.setScene(scene);
        this.show();
    }

    public void updateWaitingClients(String waitingClients){
        Label waitingClientsLabel = new Label();
        waitingClientsLabel.setText("Waiting Clients:");
        VBox vbox = new VBox();
        vbox.getChildren().add(waitingClientsLabel);
        gridPane.add(vbox, 0, 1);
        //String waitingClients = SimulationManager.getWaitingClients();
    }

}
