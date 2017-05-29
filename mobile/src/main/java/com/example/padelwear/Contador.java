package com.example.padelwear;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.comun.DireccionesGestureDetector;
import com.example.comun.Partida;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by daniel on 22/05/2017.
 */
public class Contador extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {
public Partida partida;
private TextView misPuntos,misJuegos,misSets,
        susPuntos,susJuegos,susSets;

private TextView hora;

private Vibrator vibrador;
private long[] vibrEntrada={01,500};
private long[] vibrDeshacer={01,500,500,500};

private Typeface fuenteNormal= Typeface.create("sans-serif",0);
private Typeface fuenteFina= Typeface.create("sans-serif-thin",0);


    private static final String WEAR_ARRANCAR_ACTIVIDAD="/arrancar_actividad";

    private GoogleApiClient apiClient;

    // Variables de datos de sincronización
    private static final String WEAR_PUNTUACION="/puntuacion";
    private static final String KEY_MIS_PUNTOS="com.example.padel.key.mis_puntos";
    private static final String KEY_MIS_JUEGOS="com.example.padel.key.mis_juegos";
    private static final String KEY_MIS_SETS="com.example.padel.key.mis_sets";
    private static final String KEY_SUS_PUNTOS="com.example.padel.key.sus_puntos";
    private static final String KEY_SUS_JUEGOS="com.example.padel.key.sus_juegos";
    private static final String KEY_SUS_SETS="com.example.padel.key.sus_sets";




    @Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contador_movil);

        // partida=new Partida(0);
        partida=Partida.getInstancia();
        vibrador=(Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        misPuntos=(TextView) findViewById(R.id.misPuntos);
        susPuntos=(TextView) findViewById(R.id.susPuntos);
        misJuegos=(TextView) findViewById(R.id.misJuegos);
        susJuegos=(TextView) findViewById(R.id.susJuegos);
        misSets=(TextView) findViewById(R.id.misSets);
        susSets=(TextView) findViewById(R.id.susSets);

        hora=(TextView) findViewById(R.id.hora);

        // actualizaNumeros();

        View fondo=findViewById(R.id.fondo);
        fondo.setOnTouchListener(new View.OnTouchListener(){
        GestureDetector detector=new DireccionesGestureDetector(Contador.this, new DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                @Override
                public boolean onArriba(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.rehacerPunto();
                    vibrador.vibrate(vibrDeshacer,-1);
                    sincronizaDatosMovil();
                    actualizaNumeros();
                    return true;
                }

                @Override
                public boolean onAbajo(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.deshacerPunto();
                    vibrador.vibrate(vibrDeshacer,-1);
                    sincronizaDatosMovil();
                    actualizaNumeros();
                    return true;
                }

                public void onLongPress(MotionEvent event){
                   // dismissOverlay.show();
                }
        });

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            detector.onTouchEvent(event);
            return true;
            }
        });

        misPuntos.setOnTouchListener(new View.OnTouchListener(){
        GestureDetector detector=new DireccionesGestureDetector(Contador.this,
            new DireccionesGestureDetector.SimpleOnDireccionesGestureListener(){
                @Override
                public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.puntoPara(true);
                    vibrador.vibrate(vibrEntrada,-1);
                    sincronizaDatosMovil();
                    actualizaNumeros();
                    return true;
                }

                public void onLongPress(MotionEvent event){
                  //  dismissOverlay.show();
                }
            });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    detector.onTouchEvent(event);
                    return true;
                }

                public void onLongPress(MotionEvent event){
                  //  dismissOverlay.show();
                }
            });

            susPuntos.setOnTouchListener(new View.OnTouchListener(){

            GestureDetector detector=new DireccionesGestureDetector(Contador.this,
                new DireccionesGestureDetector.SimpleOnDireccionesGestureListener(){
                @Override
                public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.puntoPara(false);
                    vibrador.vibrate(vibrEntrada,-1);
                    sincronizaDatosMovil();
                    actualizaNumeros();
                return true;
                }
                public void onLongPress(MotionEvent event){
                  //  dismissOverlay.show();
                }
            });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    detector.onTouchEvent(event);
                    return true;
                }
            });

        /* PARA ARRANCAR UNA ACTIVIDAD EN EL WERABLE */
        apiClient=new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

            if (partida.getFlagOpen()!=true) {
                mandarMensaje(WEAR_ARRANCAR_ACTIVIDAD, "");
            }

        }


        void actualizaNumeros(){
            misPuntos.setText(partida.getMisPuntos());
            susPuntos.setText(partida.getSusPuntos());
            misJuegos.setText(partida.getMisJuegos());
            susJuegos.setText(partida.getSusJuegos());
            misSets.setText(partida.getMisSets());
            susSets.setText(partida.getSusSets());
        }


    @Override
    protected void onStart(){
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop(){
        if(apiClient!=null && apiClient.isConnected()){
            apiClient.disconnect();
        }
        Wearable.DataApi.removeListener(apiClient,this);
        super.onStop();
    }

    private void mandarMensaje(final String path, final String texto){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodos=Wearable.NodeApi.getConnectedNodes(apiClient).await();

                for (Node nodo:nodos.getNodes()){
                    Wearable.MessageApi.sendMessage(apiClient,nodo.getId(),path,texto.getBytes())
                            .setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(@NonNull MessageApi.SendMessageResult resultado) {
                                    if (!resultado.getStatus().isSuccess()){
                                        Log.e("sincronizacion","Error al mandar mensaje. Código:"+resultado.getStatus().getStatusCode());
                                    } else {
                                        partida.setFlagOpen(true);
                                        Log.d("ABRIR_WEAR","APP OPEN WEAR");
                                    }
                                }
                            });
                }

            }
        }).start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.MessageApi.addListener(apiClient, this);
        Wearable.DataApi.addListener(apiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onDataChanged(DataEventBuffer eventos) {
        for (DataEvent evento:eventos){
            if(evento.getType()==DataEvent.TYPE_CHANGED){
                DataItem item=evento.getDataItem();
                if(item.getUri().getPath().equals(WEAR_PUNTUACION)){
                    DataMap dataMap= DataMapItem.fromDataItem(item).getDataMap();
                    final String mi_punto=dataMap.getString(KEY_MIS_PUNTOS);
                    final byte mi_juego=dataMap.getByte(KEY_MIS_JUEGOS);
                    final byte mi_set=dataMap.getByte(KEY_MIS_SETS);

                    final String su_punto=dataMap.getString(KEY_SUS_PUNTOS);
                    final byte su_juego=dataMap.getByte(KEY_SUS_JUEGOS);
                    final byte su_set=dataMap.getByte(KEY_SUS_SETS);

                    Log.d("CAMBIO MOVIL","mi_punto:"+mi_punto);
                    Log.d("CAMBIO MOVIL","mi_juego:"+mi_juego);
                    Log.d("CAMBIO MOVIL","mi_set:"+mi_set);
                    Log.d("CAMBIO MOVIL","su_punto:"+su_punto);
                    Log.d("CAMBIO MOVIL","su_juego:"+su_juego);
                    Log.d("CAMBIO MOVIL","su_set:"+su_set);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            misPuntos.setText(""+mi_punto);
                            susPuntos.setText(""+su_punto);
                            misJuegos.setText(""+mi_juego);
                            susJuegos.setText(""+su_juego);
                            misSets.setText(""+mi_set);
                            susSets.setText(""+su_set);
                        }
                    });
                }
            } else if (evento.getType()==DataEvent.TYPE_DELETED){
                // En el caso de que un item haya sido borrado
                Log.d("PARAMETRO","ITEM BORRADO");
            }
        }
    }


    // Sincronizar datos desde el móvil al reloj
    private void sincronizaDatosMovil(){
        Log.d("Padel Wear movil","Sincronizado");
        PutDataMapRequest putDataMapRequest=PutDataMapRequest.create(WEAR_PUNTUACION);

        // Mis datos
        putDataMapRequest.getDataMap().putString(KEY_MIS_PUNTOS,partida.getMisPuntosByte());
        putDataMapRequest.getDataMap().putByte(KEY_MIS_JUEGOS,partida.getMisJuegosByte());
        putDataMapRequest.getDataMap().putByte(KEY_MIS_SETS,partida.getMisSetsByte());

        // Sus datos
        putDataMapRequest.getDataMap().putString(KEY_SUS_PUNTOS,partida.getSuPuntosByte());
        putDataMapRequest.getDataMap().putByte(KEY_SUS_JUEGOS,partida.getSusJuegosByte());
        putDataMapRequest.getDataMap().putByte(KEY_SUS_SETS,partida.getSusSetsByte());


        Log.d("PADEL_MOBILE","SINCR misPuntos="+partida.getMisPuntosByte());
        Log.d("PADEL_MOBILE","SINCR misJuegos="+partida.getMisJuegosByte());
        Log.d("PADEL_MOBILE","SINCR misSets="+partida.getMisSetsByte());

        Log.d("PADEL_MOBILE","SINCR susPuntos="+partida.getSuPuntosByte());
        Log.d("PADEL_MOBILE","SINCR susJuegos="+partida.getSusJuegosByte());
        Log.d("PADEL_MOBILE","SINCR susSets="+partida.getSusSetsByte());

        PutDataRequest putDataRequest=putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(apiClient,putDataRequest);
    }
}