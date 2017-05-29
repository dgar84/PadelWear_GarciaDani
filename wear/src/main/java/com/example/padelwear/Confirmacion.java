package com.example.padelwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by daniel on 17/05/2017.
 */

public class Confirmacion extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmacion);


        ImageButton aceptar = (ImageButton) findViewById(R.id.aceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Confirmacion.this,CuentaAtras.class),9);
            }
        });

        ImageButton cancelar = (ImageButton) findViewById(R.id.cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode==RESULT_CANCELED){
            Toast.makeText(this,"Acción cancelada",Toast.LENGTH_SHORT).show();
        } else if(resultCode==RESULT_OK){ // si se deja pasar el timepo la operaqción será aceptada
            Toast.makeText(this,"Acción aceptada",Toast.LENGTH_SHORT).show();
            // Guardar datos de la partida
            finish();
        }
    }
}
