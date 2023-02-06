package com.example.batallanaval;

public class Lancha extends Barco{
    public Lancha(ControlJuego juego, String nombre, Equipo equipo, double x, double y) {
        super(juego, nombre, equipo,"imgs/lancha" + (equipo.equals(Equipo.ROJO) ?"roja" : "azul") + ".png" ,80, 6, x, y,
                500, 50, 100,70,200,"disparoLancha.mp3");
    }
}
