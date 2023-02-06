package com.example.batallanaval;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


import java.io.File;
import java.util.Random;

public class Bala extends Thread{

    final String IMG_PATH = "imgs/bala.png";

    ControlJuego juego;
    ImageView image;
    double xi;
    double yi;
    double xf;
    double yf;

    private TranslateTransition transition;
    private Barco obetivo;
    private Barco origen;

    public Bala(ControlJuego juego, Barco origen, Barco objetivo) {
        this.juego = juego;
        this.xi = origen.getX() + origen.barcoUI.getBoundsInLocal().getCenterX();
        this.yi = origen.getY() + origen.barcoUI.getBoundsInLocal().getCenterY();
        this.xf = objetivo.getX() + objetivo.barcoUI.getBoundsInLocal().getCenterX();
        this.yf = objetivo.getY() + objetivo.barcoUI.getBoundsInLocal().getCenterY();
        this.obetivo = objetivo;
        this.origen = origen;

        image = new ImageView(new Image(ControlJuego.class.getResource(IMG_PATH).toExternalForm()));
        image.setFitWidth(15);
        image.setPreserveRatio(true);
        image.setTranslateX(xi);
        image.setTranslateY(yi);

    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                juego.getRoot().getChildren().addAll(image);
                moveLabel();
            }
        });
    }

    private void moveLabel() {
        transition = new TranslateTransition(Duration.millis(450), image);

        transition.setByX(xf-xi);
        transition.setByY(yf-yi);
        transition.setInterpolator(Interpolator.EASE_IN);
        transition.setAutoReverse(false);
        transition.play();

        transition.setOnFinished(event -> {
            juego.getRoot().getChildren().remove(image);
            double tramoA = 25 * origen.precision/100;
            double tramoB = 50 * origen.precision/100;
            double tramoC = 75 * origen.precision/100;

            int randDisparo = getRandomNumber(1,100);
            if(randDisparo < tramoA){
                obetivo.recibir(origen,origen.potencia);
            }else if(randDisparo < tramoB){
                obetivo.recibir(origen,origen.potencia*0.5);
            }else if(randDisparo < tramoC) {
                obetivo.recibir(origen,origen.potencia*0.25);
            }
        });
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }



}
