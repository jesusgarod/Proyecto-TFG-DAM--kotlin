package com.jesus.fisioapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EjercicioAdapter(private val listaEjercicios: List<Ejercicio>) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    // Encuentra los textos dentro de tu tarjeta XML
    class EjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTituloEjercicio)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcionEjercicio)

        val tvSeriesReps: TextView = view.findViewById(R.id.tvSeriesRepeticionesPaciente)
        val btnVerVideo: Button = view.findViewById(R.id.btnVerVideoPaciente)
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
        holder.tvSeriesReps.text = "${ejercicioActual.series} series de ${ejercicioActual.repeticiones} repeticiones"

        val enlace = ejercicioActual.urlVideo

        // Comprueba si el enlace existe y no está vacío
        if (!enlace.isNullOrEmpty()) {
            // Si hay vídeo el botón aparece
            holder.btnVerVideo.visibility = View.VISIBLE

            // vida al botón
            holder.btnVerVideo.setOnClickListener {
                // El 'context' es para viajar desde el Adapter
                val contexto = holder.itemView.context
                val abrirWeb = Intent(Intent.ACTION_VIEW, Uri.parse(enlace))
                contexto.startActivity(abrirWeb)
            }
        } else {
            // Si NO hay vídeo, se esconde el botón
            holder.btnVerVideo.visibility = View.GONE
        }
    }
}