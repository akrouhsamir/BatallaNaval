package com.example.batallanaval;

public class Destructor extends Barco{
    public Destructor(ControlJuego juego, String nombre, Equipo equipo, double x, double y) {
        super(juego, nombre, equipo,"imgs/destructor" + (equipo.equals(Equipo.ROJO) ?"rojo" : "azul") + ".png" ,100, 4, x, y,
                800, 90, 120,90,1000,"disparoAcorazado.mp3");
    }
}
