package com.example.appparcial2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appparcial2.model.Genero
import com.example.appparcial2.model.Pelicula

class PeliculaViewModel: ViewModel() {

    private val _peliculas = MutableLiveData<List<Pelicula>>()
    val peliculas:LiveData<List<Pelicula>> = _peliculas


    init {
        _peliculas.value = createSamplePeliculas()
    }


    private fun createSamplePeliculas():List<Pelicula> {
        return listOf(
            Pelicula(titulo ="Terminator", anio =  1984, resenia = "muy buena", genero = Genero.ACCION, puntuacion =  4),
            Pelicula(titulo ="Volver al Futuro", anio = 1985,  resenia = "muy buena", genero = Genero.OTRO, puntuacion = 5),
            Pelicula(titulo ="Jurassic Park",anio = 1981,  resenia =  "excelente peli", genero = Genero.ACCION, puntuacion =  4)




        )
    }

    fun getPeliculaById(peliculaId:Int):Pelicula?{
        return peliculas.value?.find {it.id == peliculaId }
    }

    fun agregarPelicula(pelicula: Pelicula) {
        val listaActual = _peliculas.value.orEmpty().toMutableList()
        listaActual.add(pelicula)
        _peliculas.value = listaActual.toList() // forzamos que sea una nueva lista
    }



    fun actualizarPelicula(peliculaActualizada: Pelicula) {
        val currentList = _peliculas.value?.toMutableList() ?: return // Obtener una copia mutable
        val index = currentList.indexOfFirst { it.id == peliculaActualizada.id }
        if (index != -1) {
            currentList[index] = peliculaActualizada
            _peliculas.value = currentList.toList()
        }
    }

    fun eliminarPeliculaPorId(id: Int) {
        val currentList = _peliculas.value?.toMutableList() ?: return
        _peliculas.value = currentList.filter { it.id != id }.toList()
    }

}