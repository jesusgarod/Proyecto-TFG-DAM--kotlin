package com.jesus.fisioapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EjercicioFisioAdapter(private val listaEjercicios: List<Ejercicio>,private val alHacerClicLargo: (Ejercicio) -> Unit
) : RecyclerView.Adapter<EjercicioFisioAdapter.EjercicioViewHolder>() {

    class EjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreEjercicioFisio)
        val tvSeriesReps: TextView = view.findViewById(R.id.tvSeriesRepeticionesFisio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio_fisio, parent, false)
        return EjercicioViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaEjercicios.size
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicioActual = listaEjercicios[position]

        holder.tvNombre.text = ejercicioActual.nombre
        holder.tvSeriesReps.text = "${ejercicioActual.series} series de ${ejercicioActual.repeticiones} repeticiones"

        // ¡LA MAGIA DEL CLIC LARGO!
        holder.itemView.setOnLongClickListener {
            alHacerClicLargo(ejercicioActual)
            true // Esto es obligatorio en Kotlin para que sepa que hemos gestionado el clic largo
        }
    }
}