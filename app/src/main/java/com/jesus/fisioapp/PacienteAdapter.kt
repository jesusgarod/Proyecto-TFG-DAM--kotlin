package com.jesus.fisioapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacienteAdapter (private val listaPacientes: List <Paciente>, private val alPulsarPaciente: (Paciente) -> Unit) : RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder>() {

    class PacienteViewHolder (view: View) : RecyclerView.ViewHolder(view){

        val tvNombre: TextView = view.findViewById(R.id.tvNombrePaciente)
        val tvEmail: TextView = view.findViewById(R.id.tvEmailPaciente)
        val tvEdad: TextView = view.findViewById(R.id.tvEdadPaciente)
        val tvIcono: TextView = view.findViewById(R.id.tvIconoPaciente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_paciente, parent, false)
        return PacienteViewHolder(vista)
    }

    // Cuenta cuántos pacientes hay
    override fun getItemCount(): Int {
        return listaPacientes.size
    }

    // Rellenamos cada tarjeta con los datos del paciente que toca
    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val pacienteActual = listaPacientes[position]

        // Juntamos nombre y apellidos
        val apellidosTexto = if (pacienteActual.apellidos != null) " ${pacienteActual.apellidos}" else ""
        holder.tvNombre.text = "${pacienteActual.nombre}$apellidosTexto"

        holder.tvEmail.text = pacienteActual.email

        // Si tiene edad la ponemos, si no, ponemos un guión
        if (pacienteActual.edad != null) {
            holder.tvEdad.text = "${pacienteActual.edad} años"
        } else {
            holder.tvEdad.text = "-- años"
        }

        // El toque pro: Ponemos la primera letra de su nombre en el círculo
        holder.tvIcono.text = pacienteActual.nombre.take(1).uppercase()

        holder.itemView.setOnClickListener {
            alPulsarPaciente(pacienteActual)
        }
    }

}