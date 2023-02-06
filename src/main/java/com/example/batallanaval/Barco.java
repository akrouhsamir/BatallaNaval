package com.example.batallanaval;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

public class Barco  extends Thread{
    private final static String SEP = System.getProperty("file.separator");
    private final static String AUDIOS = new File(System.getProperty("user.dir") + SEP +"audio" +SEP).toURI().toString();
    VBox barcoUI;
    private ProgressBar progressVida;

    private Label nombreVida;
    private ControlJuego juego;
    private String nombre;

    Equipo equipo;
    ImageView image;
    private double sonar;

    private double x;
    private double y;
    private double dx;
    private double dy;
    private double angle = 45;
    private TranslateTransition transition;
    double potencia;
    double precision;
    double vida,vidaMaxima;
    double tamanno;

    long cadencia;

    String disparo;



    public Barco(ControlJuego juego, String nombre, Equipo equipo,String imgPath, int sonar, double velocidad, double x, double y,double vida, double potencia,
                 double precision, int tamanno, long cadencia, String disparo) {
        this.juego = juego;
        this.nombre = nombre;
        this.equipo = equipo;
        this.sonar = sonar;
        this.x = x;
        this.y = y;
        this.potencia = potencia;
        this.precision = precision;
        this.vida = vida;
        this.vidaMaxima = vida;
        dx = -velocidad;
        dy = -velocidad;
        this.tamanno = tamanno;
        this.cadencia = cadencia;
        this.disparo = disparo;

        image = new ImageView(new Image(ControlJuego.class.getResource(imgPath).toExternalForm()));
        image.setPreserveRatio(true);
        image.setFitWidth(tamanno);


        barcoUI = new VBox();
        progressVida = new ProgressBar();
        progressVida.setStyle("-fx-accent: #3eed46; -fx-control-inner-background: red;");
        progressVida.setProgress(1);
        progressVida.setPrefHeight(10);
        nombreVida = new Label(nombre +" (" +(int)vida+")");
        String color = equipo.equals(Equipo.ROJO) ? "RED": "BLUE";
        nombreVida.setStyle("-fx-background-color: rgba(228,228,228,0.7); -fx-text-fill: "+color+"; -fx-font-size: 14;");
        progressVida.setPrefWidth(tamanno);
        barcoUI.getChildren().addAll(image,progressVida,nombreVida);
        barcoUI.setTranslateX(x);
        barcoUI.setTranslateY(y);
        barcoUI.setRotate(angle);
    }

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                juego.getRoot().getChildren().addAll(barcoUI);
                moverBarco();
                detectar().start();
            }
        });
    }

    private Thread detectar(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (sigueAFlote() && juego.ganador() == null){
                    for(Barco b : juego.getBarcos()){
                        if(getDistancia(getX(),getY(),b.getX(),b.getY()) < sonar &&
                                nombre != b.nombre &&
                                !equipo.equals(b.equipo) &&
                                b.sigueAFlote()){
                            Media media = new Media(AUDIOS+disparo);
                            MediaPlayer mediaPlayer = new MediaPlayer(media);
                            mediaPlayer.play();
                            disparar(b);
                            mediaPlayer.play();
                            Thread recargar = recargar();
                            recargar.start();
                            try {
                                recargar.join();
                                mediaPlayer.play();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                Media media = new Media(AUDIOS+"muerteBarco.mp3");
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        image.setImage(new Image(ControlJuego.class.getResource("imgs/fuego.png").toExternalForm()));
                    }
                });
                mediaPlayer.play();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mediaPlayer.play();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        juego.getRoot().getChildren().remove(barcoUI);
                    }
                });
            }
        });
    }



    private Thread recargar(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(cadencia);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    public synchronized boolean sigueAFlote(){
        return vida > 0;
    }



    public static double getDistancia(double x1,double y1,double x2,double y2){
        double xDis = x2 -x1;
        double yDis = y2 - y1;
        return Math.sqrt(Math.pow(xDis,2) + Math.pow(yDis,2));
    }

    private void moverBarco() {
        transition = new TranslateTransition(Duration.millis(100), barcoUI);
        setX(x+dx);
        setY(y+dy);
        transition.setByX(dx);
        transition.setByY(dy);
        transition.setAutoReverse(false);
        transition.play();

        transition.setOnFinished(event -> {
            Bounds bounds = barcoUI.getBoundsInParent();
            if (bounds.getMaxX() >= 1024 || bounds.getMinX() <= 0) {
                dx = -dx;
                if(getRandomNumber(0,1) == 1){
                    dy = -dy;
                    angle = 180 + angle;
                }else{
                    angle = 180 - angle;
                }
                barcoUI.setRotate(angle);
            }
            if (bounds.getMaxY() >= 768 || bounds.getMinY() <= 0) {
                dy = -dy;
                if(getRandomNumber(0,1) == 1){
                    dx = -dx;
                    angle = 180 + angle;
                }else{
                    angle = 360 - angle;
                }
                barcoUI.setRotate(angle);
            }
            if(sigueAFlote()){
                moverBarco();
            }
        });
    }

    public synchronized double getX(){
        return x;
    }

    public synchronized double getY(){
        return y;
    }

    public synchronized void setX(double x){
        this.x = x;
    }

    public synchronized void setY(double y){
        this.y = y;
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private synchronized void quitarVida(double danno){
        vida = vida - danno;
        if(vida < 1){
            vida = 0;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nombreVida.setText(nombre + " (" + (int)vida + ")");
            }
        });
    }



    private void disparar(Barco enemigo){

        Bala bala = new Bala(juego,this,enemigo);
        bala.start();


    }


    public synchronized void recibir(Barco agresor, double danno){
        quitarVida(danno);
        System.out.println(nombre + " ha recibido un disparo de " + agresor.nombre + " --> Vida actual " + vida);
        progressVida.setProgress(vida/vidaMaxima);
    }
}
