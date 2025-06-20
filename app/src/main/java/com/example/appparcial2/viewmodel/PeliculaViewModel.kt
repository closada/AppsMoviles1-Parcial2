package com.example.appparcial2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appparcial2.model.Genero
import com.example.appparcial2.model.Pelicula
import com.example.appparcial2.repository.PeliculasRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class PeliculaViewModel: ViewModel() {

    private val _peliculas = MutableLiveData<List<Pelicula>>()
    val peliculas:LiveData<List<Pelicula>> = _peliculas


    /* Esta funcion existe para inicializar datos si no existe el JSON */
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

    fun agregarPelicula(pelicula: Pelicula, context: Context) {
        val listaActual = _peliculas.value.orEmpty().toMutableList()
        listaActual.add(pelicula)
        _peliculas.value = listaActual
        /* se guarda la informacion actualizada en el archivo JSON */
        guardarPeliculasEnArchivo(context)
    }




    fun actualizarPelicula(peliculaActualizada: Pelicula, context: Context) {
        val currentList = _peliculas.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == peliculaActualizada.id }
        if (index != -1) {
            currentList[index] = peliculaActualizada
            _peliculas.value = currentList
            /* se guarda la informacion actualizada en el archivo JSON */
            guardarPeliculasEnArchivo(context)
        }
    }


    fun eliminarPeliculaPorId(id: Int, context: Context) {
        val currentList = _peliculas.value?.toMutableList() ?: return
        _peliculas.value = currentList.filter { it.id != id }
        /* se guarda la informacion actualizada en el archivo JSON */
        guardarPeliculasEnArchivo(context)
    }





    /* FUNCIONES EXTRAS PARA MANIPULACION DE DATOS EN ARCHIVO */
    fun guardarPeliculasEnArchivo(context: Context) {
        val listaActual = _peliculas.value.orEmpty()
        PeliculasRepository.guardarPeliculas(context, listaActual)
    }

    fun cargarPeliculasDesdeArchivo(context: Context) {
        val peliculasDesdeArchivo = PeliculasRepository.cargarPeliculas(context)
        _peliculas.value = if (peliculasDesdeArchivo.isNotEmpty()) {
            peliculasDesdeArchivo.toMutableList()
        } else {
            createSamplePeliculas().toMutableList()
        }
    }





}