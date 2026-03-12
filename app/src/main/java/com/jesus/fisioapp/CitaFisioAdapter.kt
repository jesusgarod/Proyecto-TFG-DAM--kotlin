package com.jesus.fisioapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CitaFisioAdapter (private val listaCitas: List<Cita>, private val alPulsarCita: (Cita) -> Unit): RecyclerView.Adapter<CitaFisioAdapter.CitaFisioViewHolder>() {

    class CitaFisioViewHolder (view: View) : RecyclerView.ViewHolder(view){

        val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHoraFisio)
        val tvMotivo: TextView = view.findViewById(R.id.tvMotivoFisio)
        val tvNombrePaciente: TextView = view.findViewById(R.id.tvNombrePacienteAgenda)
        val tvEstado: TextView = view.findViewById(R.id.tvEstadoCitaFisio)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaFisioViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_cita_fisio, parent, false)
        return CitaFisioViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return listaCitas.size
    }

    override fun onBindViewHolder(holder: CitaFisioViewHolder, position: Int) {
        val citaActual = listaCitas[position]

        // Juntamos la fecha y la hora para que quede como en el diseño
        holder.tvFechaHora.text = "${citaActual.fecha} - ${citaActual.hora}"
        holder.tvMotivo.text = "Motivo: ${citaActual.motivo}"

        val nombre = citaActual.paciente?.nombre ?: "Alguien"
        val apellidos = citaActual.paciente?.apellidos ?: ""

        holder.tvNombrePaciente.text = "\uD83D\uDC64 $nombre $apellidos"

//        holder.tvEstado.text = citaActual.estado ?: "Pendiente"
        val estadoReal = citaActual.estado ?: "Pendiente"
        holder.tvEstado.text = estadoReal

        when (estadoReal){
            "Aceptada" -> {

                holder.tvEstado.setTextColor(Color.parseColor("#1B5E20"))
                holder.tvEstado.setBackgroundColor(Color.parseColor("#C8E6C9"))
            }
            "Cancelada" -> {

                holder.tvEstado.setTextColor(Color.parseColor("#B71C1C"))
                holder.tvEstado.setBackgroundColor(Color.parseColor("#FFCDD2"))
            }
            else -> {

                holder.tvEstado.setTextColor(Color.parseColor("#1976D2"))
                holder.tvEstado.setBackgroundColor(Color.parseColor("#BBDEFB"))

            }
        }

        holder.itemView.setOnClickListener {

            alPulsarCita(citaActual)
        }
    }











































}