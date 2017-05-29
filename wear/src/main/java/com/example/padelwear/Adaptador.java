package com.example.padelwear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by daniel on 17/05/2017.
 */

final class Adaptador extends WearableRecyclerView.Adapter {
    private String[] datos;
    private final Context contexto;
    private final LayoutInflater inflador;
    protected View.OnClickListener onClickListener;

    public Adaptador(Context contexto,String[] datos){
        this.contexto=contexto;
        this.datos=datos;
        inflador=LayoutInflater.from(contexto);
    }


    // Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ItemViewHolder extends WearableRecyclerView.ViewHolder{
        private TextView textView;

        public ItemViewHolder(View itemView){
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.nombre);
        }

    }


    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el archivo xml
        View v=inflador.inflate(R.layout.elemento_lista,null);
        v.setOnClickListener(onClickListener);
        return new ItemViewHolder(v);
    }


    // Usando como base el ViewHolder, lo personalizamos
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int posicion) {
        ItemViewHolder itemHolder=(ItemViewHolder) holder;
        TextView viewText=itemHolder.textView;
        viewText.setText(datos[posicion]);
        holder.itemView.setTag(posicion);
    }

    @Override
    public int getItemCount() {
        return datos.length;
    }


    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
}
