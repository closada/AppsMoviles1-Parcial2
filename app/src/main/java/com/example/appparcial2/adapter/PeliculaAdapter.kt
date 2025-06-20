package com.example.appparcial2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appparcial2.databinding.ItemPeliculaBinding
import com.example.appparcial2.model.Pelicula

class PeliculaAdapter(
    private var peliculas: List<Pelicula>,
    private val onPeliculaClick: (Pelicula) -> Unit
) : RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder>() {

    inner class PeliculaViewHolder(private val binding: ItemPeliculaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pelicula: Pelicula) {
            binding.apply {
                tvTitulo.text = pelicula.titulo
                tvAnio.text = pelicula.anio.toString()
                tvGenero.text = pelicula.genero.toString()
                tvComentario.text = pelicula.resenia
                rbPuntuacion.rating  = pelicula.puntuacion.toFloat()

                root.setOnClickListener {
                    onPeliculaClick(pelicula)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val binding = ItemPeliculaBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return PeliculaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val item = peliculas[position]
        holder.bind(item)
    }

    override fun getItemCount() = peliculas.size

    fun updatePeliculas(newPeliculas: List<Pelicula>) {
        peliculas = newPeliculas
        notifyDataSetChanged()
    }
}