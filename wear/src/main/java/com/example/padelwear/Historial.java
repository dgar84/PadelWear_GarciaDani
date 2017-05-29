package com.example.padelwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

/**
 * Created by daniel on 17/05/2017.
 */

public class Historial extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.selector2d);
        final GridViewPager paginador=(GridViewPager) findViewById(R.id.paginador);
        paginador.setAdapter(new AdaptadorGridPager(this,getFragmentManager()));

    }
}
