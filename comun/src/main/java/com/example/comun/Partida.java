package com.example.comun;

import java.util.Vector;

public class Partida {
    private Vector<Puntuacion> puntos;
    private Vector<Puntuacion> partidaRehacer;

    private boolean flagOpen; // false: se abre desde el móvil o desde el reloj
                          // true: se han abierto los dos


    private static Partida instancia;

    public static Partida getInstancia(){
        if (instancia==null){
            instancia=new Partida();
        }
        return instancia;
    }

    // Aplicar Singleton
    private Partida() {
        puntos = new Vector<Puntuacion>();
        puntos.add(new Puntuacion());
        partidaRehacer = new Vector<Puntuacion>();
    }

    public void puntoPara(boolean miEquipo) {
        //vibr.vibrate(vibrEntrada, -1);
        puntos.add(new Puntuacion(puntos.lastElement(), miEquipo));

        //Log.e("Tag", historial.toString());
        partidaRehacer = new Vector<Puntuacion>();
        //actualizaNumeros();
    }

    public void deshacerPunto() {
        if (puntos.size() > 1) {
            //vibr.vibrate(vibrDeshacer, -1);
            partidaRehacer.add(puntos.lastElement());
            puntos.remove(puntos.size() - 1);
            //actualizaNumeros();
        } else {
            // vibr.vibrate(vibrError, -1);
        }
    }

    public void rehacerPunto() {

        if (partidaRehacer.size() > 0) {
            //vibr.vibrate(vibrDeshacer, -1);
            puntos.add(partidaRehacer.lastElement());
            partidaRehacer.remove(partidaRehacer.size() - 1);
            //actualizaNumeros();
        } else {
            //vibr.vibrate(vibrError, -1);
        }
    }

    public String getMisPuntos() {
        return Puntuacion.puntos[puntos.lastElement().getMiPuntuacion()];
    }

    public String getSusPuntos() {
        return Puntuacion.puntos[puntos.lastElement().getSuPuntuacion()];
    }

   /* public int getMisJuegos(){
        return partida.lastElement().getMisJuegos();
    }*/

    public String getMisJuegos() {
        return String.valueOf(puntos.lastElement().getMisJuegos());
    }

    public String getSusJuegos() {
        return String.valueOf(puntos.lastElement().getSusJuegos());
    }

    public String getMisSets(){
        return String.valueOf(puntos.lastElement().getMisSets());
    }

    public String getSusSets() {
        return String.valueOf(puntos.lastElement().getSusSets());
    }

    public boolean getFlagOpen() {
        return flagOpen;
    }

    public void setFlagOpen(boolean flagOpen) {
        this.flagOpen = flagOpen;
    }


    public String getMisPuntosByte(){
        return Puntuacion.puntos[puntos.lastElement().getMiPuntuacion()];
    }

    public Byte getMisJuegosByte(){
        return puntos.lastElement().getMisJuegos();
    }

    public Byte getMisSetsByte(){
        return puntos.lastElement().getMisSets();
    }

    public String getSuPuntosByte(){
        return Puntuacion.puntos[puntos.lastElement().getSuPuntuacion()];
    }

    public Byte getSusJuegosByte(){
        return puntos.lastElement().getSusJuegos();
    }

    public Byte getSusSetsByte(){
        return puntos.lastElement().getSusSets();
    }


}
