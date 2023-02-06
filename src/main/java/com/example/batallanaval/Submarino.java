package com.example.batallanaval;

public class Submarino extends Barco{
    public Submarino(ControlJuego juego, String nombre, Equipo equipo, double x, double y) {
        super(juego, nombre, equipo,"imgs/submarino" + (equipo.equals(Equipo.ROJO) ?"rojo" : "azul") + ".png" ,700, 2, x, y,
                300, 300, 120,100,5000,"disparoSubmarino.mp3");
    }
}
