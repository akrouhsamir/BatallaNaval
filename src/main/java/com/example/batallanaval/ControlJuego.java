package com.example.batallanaval;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


public class ControlJuego extends Application {


    public ArrayList<Barco> barcos;

    private Equipo ganador;
    private AnchorPane root;

    public static void main(String[] args) {
        launch(args);
    }

    public synchronized ArrayList<Barco> getBarcos(){
        return barcos;
    }

    public synchronized Equipo ganador(){
        return ganador;
    }
    public synchronized void  setGanador(Equipo equipo){
        ganador = equipo;
    }


    @Override
    public void start(Stage primaryStage) {
        root = new AnchorPane();
        Image image = new Image(ControlJuego.class.getResource("imgs/fondoverde.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(0, 0, true, true, true, true));
        root.setBackground(new Background(backgroundImage));
        Button button = new Button("Empezar batalla");
        button.setPrefWidth(150);
        button.setPrefHeight(50);
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        button.setFont(Font.font("Verdana", 20));
        AnchorPane.setBottomAnchor(button, 10.0);
        AnchorPane.setLeftAnchor(button, (root.getWidth() - button.getWidth()) / 2);
        AnchorPane.setRightAnchor(button, (root.getWidth() - button.getWidth()) / 2);
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTSTEELBLUE), new Stop(1, Color.BLUE));
        button.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(20), Insets.EMPTY)));
        AnchorPane.setLeftAnchor(button,100d);
        AnchorPane.setRightAnchor(button,100d);
        AnchorPane.setBottomAnchor(button, 30d);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setCursor(Cursor.HAND));

        barcos = new ArrayList<>();
        ganador = null;

        Destructor destructorRojo = new Destructor(this,"Pablo", Equipo.ROJO, 900,250);
        Destructor destructorAzul = new Destructor(this,"Tricola", Equipo.AZUL, 100,100);

        Lancha lanchaRoja = new Lancha(this,"Lucho", Equipo.ROJO, 900,100);
        Lancha lanchaAzul = new Lancha(this,"Dani", Equipo.AZUL, 100,250);

        Lancha lanchaRoja2 = new Lancha(this,"Marcos", Equipo.ROJO, 900,600);
        Lancha lanchaAzul2 = new Lancha(this,"Mirbis", Equipo.AZUL, 100,680);


        Acorazado acorazadoRojo = new Acorazado(this,"Samir",Equipo.ROJO,600,400);
        Acorazado acorazadoAzul = new Acorazado(this,"Juan",Equipo.AZUL,400,400);



        Submarino submarinoRojo = new Submarino(this,"Josema", Equipo.ROJO, 900,680);
        Submarino submarinoAzul = new Submarino(this,"Fran", Equipo.AZUL, 100,600);

        barcos.add(acorazadoRojo);
        barcos.add(acorazadoAzul);

        barcos.add(lanchaRoja);
        barcos.add(lanchaAzul);
        barcos.add(lanchaRoja2);
        barcos.add(lanchaAzul2);

        barcos.add(destructorAzul);
        barcos.add(destructorRojo);

        barcos.add(submarinoAzul);
        barcos.add(submarinoRojo);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(Barco b : barcos){
                    b.start();
                }
                root.getChildren().remove(button);
            }
        });


        root.getChildren().addAll(button);
        primaryStage.setTitle("Batalla naval");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
        new Thread(new Runnable() {
            int barcosRojos, barcosAzules;
            @Override
            public void run() {

                while(ganador() == null){
                    barcosRojos= 0;
                    barcosAzules = 0;
                    for(Barco b: getBarcos()){
                        if(b.equipo.equals(Equipo.ROJO) && b.sigueAFlote()){
                            barcosRojos++;
                        }
                        if(b.equipo.equals(Equipo.AZUL) && b.sigueAFlote()){
                            barcosAzules++;
                        }
                    }
                    if(barcosRojos ==0){
                        setGanador(Equipo.AZUL);
                    }
                    if(barcosAzules == 0){
                        setGanador(Equipo.ROJO);
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Felicitaciones");
                        alert.setHeaderText("El equipo ganador es:");
                        alert.setContentText(ganador().toString());
                        alert.showAndWait();
                    }
                });

            }
        }).start();
    }


    public AnchorPane getRoot(){
        return root;
    }

}