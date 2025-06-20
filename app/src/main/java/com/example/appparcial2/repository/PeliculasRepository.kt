package com.example.appparcial2.repository

import android.content.Context
import com.example.appparcial2.model.Pelicula
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object PeliculasRepository {
    private const val ARCHIVO_JSON = "peliculas.json"

    fun guardarPeliculas(context: Context, lista: List<Pelicula>) {
        val json = Gson().toJson(lista)
        val archivo = File(context.filesDir, ARCHIVO_JSON)
        archivo.writeText(json)
    }

    fun cargarPeliculas(context: Context): List<Pelicula> {
        val archivo = File(context.filesDir, ARCHIVO_JSON)
        if (!archivo.exists()) return emptyList()

        val json = archivo.readText()
        val tipoLista = object : TypeToken<List<Pelicula>>() {}.type
        return Gson().fromJson(json, tipoLista)
    }
}
