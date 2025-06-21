package com.example.appparcial2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.appparcial2.databinding.ActivityRegistroPeliculaBinding
import com.example.appparcial2.exception.DatosInvalidosException
import com.example.appparcial2.exception.PeliculaDuplicadaException
import com.example.appparcial2.model.Genero
import com.example.appparcial2.model.Pelicula
import com.example.appparcial2.viewmodel.PeliculaViewModel
import com.example.appparcial2.repository.PeliculasRepository
import java.util.*

class RegistroPeliculaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroPeliculaBinding
    private val viewModel: PeliculaViewModel by lazy {
        ViewModelHolder.getSharedPeliculaViewModel()
    }

    private var peliculaExistente: Pelicula? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistroPeliculaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configurarYearPicker()
        configurarSpinnerGenero()

        /* levanto la pelicula si me lo está mandando la mainActivity */
        val pelicula = intent.getSerializableExtra("pelicula") as? Pelicula

        if (pelicula != null) {
            mostrarDetallesParaEditar(pelicula)
        } else {
            prepararParaRegistro()
        }

        /* seteo los listener en los botones */
        binding.btnVolver.setOnClickListener {
            finish() // Cierra esta activity y vuelve a la anterior
        }

        binding.btnRegistrar.setOnClickListener {
            registrarPelicula()
        }

        binding.btnModificar.setOnClickListener {
            modificarPelicula()
        }

        binding.btnEliminar.setOnClickListener {
            eliminarPelicula()
        }
    }

    private fun configurarYearPicker() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.npAnio.apply {
            minValue = 1900
            maxValue = currentYear
            value = currentYear
        }
    }

    private fun configurarSpinnerGenero() {
        val generos = Genero.values().map { it.name.lowercase().replaceFirstChar(Char::uppercase) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spGenero.adapter = adapter
    }

    private fun mostrarDetallesParaEditar(pelicula: Pelicula) {
        peliculaExistente = pelicula

        binding.apply {
            etTitulo.setText(pelicula.titulo)
            etResenia.setText(pelicula.resenia)
            npAnio.value = pelicula.anio
            rbPuntuacion.rating = pelicula.puntuacion.toFloat()

            val indexGenero = Genero.values().indexOf(pelicula.genero)
            spGenero.setSelection(indexGenero)

            binding.layoutBotonesModificarEliminar.visibility = View.VISIBLE
            btnRegistrar.visibility = View.GONE
            btnModificar.visibility = View.VISIBLE
            btnEliminar.visibility = View.VISIBLE
        }
    }

    private fun prepararParaRegistro() {
        binding.apply {
            btnRegistrar.visibility = View.VISIBLE
            btnModificar.visibility = View.GONE
            btnEliminar.visibility = View.GONE
        }
    }


    /* FUNCION VALIDADORA DE DATOS: */
    private fun validarDatos(titulo: String, resenia: String, puntuacion: Int, esNuevo: Boolean) {
        if (titulo.isBlank()) throw DatosInvalidosException("El título no puede estar vacío")
        if (resenia.isBlank()) throw DatosInvalidosException("La reseña no puede estar vacía")
        if (puntuacion == 0) throw DatosInvalidosException("La puntuación no puede ser 0 estrellas")

        if (esNuevo) {
            val yaExiste = viewModel.getPeliculas().any { it.titulo.equals(titulo, ignoreCase = true) }
            if (yaExiste) throw PeliculaDuplicadaException("Ya existe una película con ese título")
        }
    }


    private fun registrarPelicula() {
        val titulo = binding.etTitulo.text.toString()
        val resenia = binding.etResenia.text.toString()
        val anio = binding.npAnio.value
        val puntuacion = binding.rbPuntuacion.rating.toInt()
        val generoSeleccionado = Genero.valueOf(binding.spGenero.selectedItem.toString().uppercase())

        try {
            validarDatos(titulo, resenia, puntuacion, esNuevo = true)

            val nuevaPelicula = Pelicula(
                titulo = titulo,
                resenia = resenia,
                anio = anio,
                puntuacion = puntuacion,
                genero = generoSeleccionado
            )

            viewModel.agregarPelicula(nuevaPelicula, applicationContext)

            Toast.makeText(this, "Película registrada", Toast.LENGTH_SHORT).show()
            finish()

        } catch (e: DatosInvalidosException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: PeliculaDuplicadaException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show()
            Log.e("RegistroPelicula", "Error al registrar", e)
        }
    }


    private fun modificarPelicula() {
        peliculaExistente?.let {
            val titulo = binding.etTitulo.text.toString()
            val resenia = binding.etResenia.text.toString()
            val anio = binding.npAnio.value
            val puntuacion = binding.rbPuntuacion.rating.toInt()
            val generoSeleccionado = Genero.valueOf(binding.spGenero.selectedItem.toString().uppercase())

            try {
                validarDatos(titulo, resenia, puntuacion, esNuevo = false)

                it.titulo = titulo
                it.resenia = resenia
                it.anio = anio
                it.puntuacion = puntuacion
                it.genero = generoSeleccionado

                viewModel.actualizarPelicula(it, applicationContext)

                Toast.makeText(this, "Película modificada", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()

            } catch (e: DatosInvalidosException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al modificar", Toast.LENGTH_SHORT).show()
                Log.e("RegistroPelicula", "Error al modificar", e)
            }
        }
    }


    private fun eliminarPelicula() {
        peliculaExistente?.let {
            viewModel.eliminarPeliculaPorId(it.id, applicationContext)
            Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}