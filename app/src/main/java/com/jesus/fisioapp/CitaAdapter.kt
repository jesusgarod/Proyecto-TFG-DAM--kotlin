package com.jesus.fisioapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitaAdapter(private val listaCitas: List<Cita>, private val pulsadorBorrar: (Long) -> Unit) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHoraCita)
        val tvEstado: TextView = view.findViewById(R.id.tvEstadoCita)
        val tvMotivo: TextView = view.findViewById(R.id.tvMotivoCita)
        val tvBorrar: TextView = view.findViewById(R.id.tvBorrarCita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(vista)
    }

    override fun getItemCount(): Int {

        return listaCitas.size
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {

        val citaActual = listaCitas[position]

        // Juntamos la fecha y la hora en el mismo texto para que quede más limpio
        holder.tvFechaHora.text = "${citaActual.fecha} - ${citaActual.hora}"
        holder.tvMotivo.text = citaActual.motivo
        holder.tvEstado.text = citaActual.estado

        holder.tvBorrar.setOnClickListener {

            citaActual.id?.let { idCita -> pulsadorBorrar(idCita) }
        }
    }
}