package com.jesus.fisioapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EjercicioAdapter(private val listaEjercicios: List<Ejercicio>) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    // Encuentra los textos dentro de tu tarjeta XML
    class EjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloEjercicio)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcionEjercicio)
    }

    // Coge item_ejercicio.xml y crea una tarjeta en blanco
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio, parent, false)
        return EjercicioViewHolder(vista)
    }

    //Le dice a Android cuántas tarjetas tiene que dibujar en total
    override fun getItemCount(): Int {
        return listaEjercicios.size
    }

    // Coge los datos de un ejercicio y los escribe en la tarjeta
    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicioActual = listaEjercicios[position]

        holder.tvTitulo.text = ejercicioActual.nombre
        holder.tvDescripcion.text = ejercicioActual.descripcion
    }
}