package com.example.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableRecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {

    // Elementos a mostrar en la lista
    String[] elementos={"Partida","Terminar partida","Historial","Notificación","Pasos","Pulsaciones"};

    private TextView mTextView;
    private MyChildLayoutManager childLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WearableRecyclerView lista=(WearableRecyclerView) findViewById(R.id.lista);

        Adaptador adaptador=new Adaptador(this,elementos);
        adaptador.setOnItemClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer tag=(Integer) v.getTag();
                switch (tag){
                    case 0:
                        startActivity(new Intent(MainActivity.this,Contador.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this,Confirmacion.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,Historial.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this,Pasos.class));
                        break;
                }
            }
        });
       lista.setAdapter(adaptador);
        lista.setCenterEdgeItems(true);
        childLayout=new MyChildLayoutManager(this);
        lista.setCircularScrollingGestureEnabled(true);
        lista.setBezelWidth(0.5f);
        lista.setScrollDegreesPerScreen(180);
        lista.setLayoutManager(childLayout);

    }
}
