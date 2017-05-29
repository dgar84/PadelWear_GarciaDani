package com.example.padelwear;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by daniel on 23/05/2017.
 */

public class ServicioEscuchadorMovil  extends WearableListenerService {
    private static final String WEAR_ARRANCAR_ACTIVIDAD="/arrancar_actividad";

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        if(messageEvent.getPath().equalsIgnoreCase(WEAR_ARRANCAR_ACTIVIDAD)){
            Intent intent=new Intent(this,Contador.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
