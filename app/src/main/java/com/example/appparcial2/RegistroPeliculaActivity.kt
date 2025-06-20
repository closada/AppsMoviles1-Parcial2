package com.example.appparcial2

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.appparcial2.databinding.ActivityRegistroPeliculaBinding
import com.example.appparcial2.model.Genero
import com.example.appparcial2.model.Pelicula
import com.example.appparcial2.viewmodel.PeliculaViewModel
import java.util.*

class RegistroPeliculaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroPeliculaBinding
    private val viewModel: PeliculaViewModel by viewModels()

    private var peliculaId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistroPeliculaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        configurarYearPicker()
        configurarSpinnerGenero()

        val receivedPeliculaId = intent.getIntExtra("pelicula_id", -1)

        if (receivedPeliculaId  !=  -1) {

            val pelicula = viewModel.getPeliculaById(receivedPeliculaId)
            if (pelicula != null) {
                mostrarDetallesParaEditar(pelicula)
                peliculaId = receivedPeliculaId
            } else {
                // Si el ID no corresponde a ninguna película, tratamos esto como un registro nuevo.
                prepararParaRegistro()
            }
        } else {
            prepararParaRegistro()
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
        binding.apply {
            etTitulo.setText(pelicula.titulo)
            etResenia.setText(pelicula.resenia)
            npAnio.value = pelicula.anio
            rbPuntuacion.rating = pelicula.puntuacion.toFloat()

            val indexGenero = Genero.values().indexOf(pelicula.genero)
            spGenero.setSelection(indexGenero)

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

    private fun registrarPelicula() {
        val titulo = binding.etTitulo.text.toString()
        val resenia = binding.etResenia.text.toString()
        val anio = binding.npAnio.value
        val puntuacion = binding.rbPuntuacion.rating.toInt()
        val generoSeleccionado = Genero.valueOf(binding.spGenero.selectedItem.toString().uppercase())

        if (titulo.isBlank()) {
            Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevaPelicula = Pelicula(
            titulo = titulo,
            resenia = resenia,
            anio = anio,
            puntuacion = puntuacion,
            genero = generoSeleccionado
        )

        viewModel.agregarPelicula(nuevaPelicula)

        Toast.makeText(this, "Película registrada", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun modificarPelicula() {
        val currentPeliculaId = peliculaId

        if (currentPeliculaId != null) {
            val titulo = binding.etTitulo.text.toString()
            val resenia = binding.etResenia.text.toString()
            val anio = binding.npAnio.value
            val puntuacion = binding.rbPuntuacion.rating.toInt()
            val generoSeleccionado = Genero.valueOf(binding.spGenero.selectedItem.toString().uppercase())

            if (titulo.isBlank()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return
            }

            val peliculaActualizada = Pelicula(
                id = currentPeliculaId, // Usamos el ID existente
                titulo = titulo,
                resenia = resenia,
                anio = anio,
                puntuacion = puntuacion,
                genero = generoSeleccionado
            )

            viewModel.actualizarPelicula(peliculaActualizada)

            Toast.makeText(this, "Película modificada", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error: No se puede modificar una película sin ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarPelicula() {
        // Aseguramos que tenemos un ID de película para eliminar
        val currentPeliculaId = peliculaId
        if (currentPeliculaId != null) {
            viewModel.eliminarPeliculaPorId(currentPeliculaId)

            Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show()
            finish() // Cierra esta Activity y vuelve a la MainActivity
        } else {
            Toast.makeText(this, "Error: No se puede eliminar una película sin ID.", Toast.LENGTH_SHORT).show()
        }
    }
}
