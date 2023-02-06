package com.example.batallanaval;

public class Acorazado extends Barco{
    public Acorazado(ControlJuego juego, String nombre, Equipo equipo, double x, double y) {
        super(juego, nombre, equipo,"imgs/acorazado" + (equipo.equals(Equipo.ROJO) ?"rojo" : "azul") + ".png" ,150, 3, x, y,
                1000, 180, 80,130,1500,"disparoAcorazado.mp3");
    }
}

