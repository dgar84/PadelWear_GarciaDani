package com.example.padelwear;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.DismissOverlayView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.comun.DireccionesGestureDetector;
import com.example.comun.Partida;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by daniel on 18/05/2017.
 */

public class Contador extends WearableActivity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

    public Partida partida;
    private TextView misPuntos,misJuegos,misSets,
                        susPuntos,susJuegos,susSets;

    private TextView hora;

    private Vibrator vibrador;
    private long[] vibrEntrada={01,500};
    private long[] vibrDeshacer={01,500,500,500};

    private Typeface fuenteNormal= Typeface.create("sans-serif",0);
    private Typeface fuenteFina= Typeface.create("sans-serif-thin",0);

    private DismissOverlayView dismissOverlay;

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
        setAmbientEnabled();
        setContentView(R.layout.contador);

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

        // para la salida con pulsación larga
        dismissOverlay=(DismissOverlayView) findViewById(R.id.dismiss_overlay);
        dismissOverlay.setIntroText("Para salir de la aplicación, haz una pulsación larga.");
        dismissOverlay.showIntroIfNecessary();

        View fondo=findViewById(R.id.fondo);
        fondo.setOnTouchListener(new View.OnTouchListener(){
            GestureDetector detector=new DireccionesGestureDetector(Contador.this, new DireccionesGestureDetector.SimpleOnDireccionesGestureListener() {
                @Override
                public boolean onArriba(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.rehacerPunto();
                    vibrador.vibrate(vibrDeshacer,-1);
                    sincronizaDatos();
                    actualizaNumeros();
                    return true;
                }

                @Override
                public boolean onAbajo(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.deshacerPunto();
                    vibrador.vibrate(vibrDeshacer,-1);
                    sincronizaDatos();
                    actualizaNumeros();
                    return true;
                }

                public void onLongPress(MotionEvent event){
                    dismissOverlay.show();
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
                    actualizaNumeros();
                    sincronizaDatos();
                    return true;
                }

                public void onLongPress(MotionEvent event){
                    dismissOverlay.show();
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }

            public void onLongPress(MotionEvent event){
                dismissOverlay.show();
            }
        });

        susPuntos.setOnTouchListener(new View.OnTouchListener(){

            GestureDetector detector=new DireccionesGestureDetector(Contador.this,
                    new DireccionesGestureDetector.SimpleOnDireccionesGestureListener(){
                @Override
                public boolean onDerecha(MotionEvent e1, MotionEvent e2, float distX, float distY) {
                    partida.puntoPara(false);
                    vibrador.vibrate(vibrEntrada,-1);
                    actualizaNumeros();
                    sincronizaDatos();
                    return true;
                }
                public void onLongPress(MotionEvent event){
                    dismissOverlay.show();
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        apiClient=new GoogleApiClient.Builder(this)
                        .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        if (partida.getFlagOpen()!=true) {
            mandarMensajeAMovil(WEAR_ARRANCAR_ACTIVIDAD, "");
        }

        PendingResult<DataItemBuffer> resultado=Wearable.DataApi.getDataItems(apiClient);
        resultado.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItems) {
                for (DataItem dataItem:dataItems){
                    if(dataItem.getUri().getPath().equals(WEAR_PUNTUACION)){
                        DataMapItem dataMapItem=DataMapItem.fromDataItem(dataItem);
                        final String mi_punto=dataMapItem.getDataMap().getString(KEY_MIS_PUNTOS);
                        final byte mi_juego=dataMapItem.getDataMap().getByte(KEY_MIS_JUEGOS);
                        final byte mi_set=dataMapItem.getDataMap().getByte(KEY_MIS_SETS);

                        final String su_punto=dataMapItem.getDataMap().getString(KEY_SUS_PUNTOS);
                        final byte su_juego=dataMapItem.getDataMap().getByte(KEY_SUS_JUEGOS);
                        final byte su_set=dataMapItem.getDataMap().getByte(KEY_SUS_SETS);

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
                }
            }
        });

    }


    void actualizaNumeros(){
        misPuntos.setText(partida.getMisPuntos());
        susPuntos.setText(partida.getSusPuntos());
        misJuegos.setText(partida.getMisJuegos());
        susJuegos.setText(partida.getSusJuegos());
        misSets.setText(partida.getMisSets());
        susSets.setText(partida.getSusSets());
    }

    // métodos para controlar el modo Ambiente
    @Override
    public void onEnterAmbient(Bundle ambientDetails){
        super.onEnterAmbient(ambientDetails);

        hora.setVisibility(View.VISIBLE);

        misPuntos.setTypeface(fuenteFina);
        misPuntos.getPaint().setAntiAlias(false);
        susPuntos.setTypeface(fuenteFina);
        susPuntos.getPaint().setAntiAlias(false);

        misJuegos.setTypeface(fuenteFina);
        misJuegos.getPaint().setAntiAlias(false);
        susJuegos.setTypeface(fuenteFina);
        susJuegos.getPaint().setAntiAlias(false);

        misSets.setTypeface(fuenteFina);
        misSets.getPaint().setAntiAlias(false);
        susSets.setTypeface(fuenteFina);
        susSets.getPaint().setAntiAlias(false);
    }


    @Override
    public void onExitAmbient(){
        super.onExitAmbient();

        hora.setVisibility(View.INVISIBLE);

        misPuntos.setTypeface(fuenteNormal);
        misPuntos.getPaint().setAntiAlias(false);
        susPuntos.setTypeface(fuenteNormal);
        susPuntos.getPaint().setAntiAlias(false);

        misJuegos.setTypeface(fuenteNormal);
        misJuegos.getPaint().setAntiAlias(false);
        susJuegos.setTypeface(fuenteNormal);
        susJuegos.getPaint().setAntiAlias(false);

        misSets.setTypeface(fuenteNormal);
        misSets.getPaint().setAntiAlias(false);
        susSets.setTypeface(fuenteNormal);
        susSets.getPaint().setAntiAlias(false);
    }


    @Override
    public void onUpdateAmbient(){
        super.onUpdateAmbient();
        Calendar c=Calendar.getInstance();
        c.setTime(new Date());
        hora.setText(c.get(Calendar.HOUR_OF_DAY)+ ":"+c.get(Calendar.MINUTE));
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
    protected void onStart(){
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop(){
        Wearable.MessageApi.removeListener(apiClient,this);
        Wearable.DataApi.removeListener(apiClient,this);
        if (apiClient!=null && apiClient.isConnected()){
            apiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        if(messageEvent.getPath().equalsIgnoreCase(WEAR_ARRANCAR_ACTIVIDAD)){
            Log.d("WEAR","RECIBIDO EN RELOJ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                 //   textView.setText(textView.getText()+"\n"+ new  String(messageEvent.getData()));
                }
            });
        }
    }

    private void mandarMensajeAMovil(final String path, final String texto){
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


    private void sincronizaDatos(){
        Log.d("Padel Wear","Sincronizado");
        PutDataMapRequest putDataMapRequest=PutDataMapRequest.create(WEAR_PUNTUACION);

        // Mis datos
        putDataMapRequest.getDataMap().putString(KEY_MIS_PUNTOS,partida.getMisPuntosByte());
        putDataMapRequest.getDataMap().putByte(KEY_MIS_JUEGOS,partida.getMisJuegosByte());
        putDataMapRequest.getDataMap().putByte(KEY_MIS_SETS,partida.getMisSetsByte());

        // Sus datos
        putDataMapRequest.getDataMap().putString(KEY_SUS_PUNTOS,partida.getSuPuntosByte());
        putDataMapRequest.getDataMap().putByte(KEY_SUS_JUEGOS,partida.getSusJuegosByte());
        putDataMapRequest.getDataMap().putByte(KEY_SUS_SETS,partida.getSusSetsByte());


        Log.d("PADEL_WEAR","SINCR misPuntos="+partida.getMisPuntosByte());
        Log.d("PADEL_WEAR","SINCR misJuegos="+partida.getMisJuegosByte());
        Log.d("PADEL_WEAR","SINCR misSets="+partida.getMisSetsByte());

        Log.d("PADEL_WEAR","SINCR susPuntos="+partida.getSuPuntosByte());
        Log.d("PADEL_WEAR","SINCR susJuegos="+partida.getSusJuegosByte());
        Log.d("PADEL_WEAR","SINCR susSets="+partida.getSusSetsByte());


        PutDataRequest putDataRequest=putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(apiClient,putDataRequest);
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

                    Log.d("CAMBIO WEAR","mi_punto:"+mi_punto);
                    Log.d("CAMBIO WEAR","mi_juego:"+mi_juego);
                    Log.d("CAMBIO WEAR","mi_set:"+mi_set);
                    Log.d("CAMBIO WEAR","su_punto:"+su_punto);
                    Log.d("CAMBIO WEAR","su_juego:"+su_juego);
                    Log.d("CAMBIO WEAR","su_set:"+su_set);

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
}
