package com.example.appparcial2

import android.os.Bundle
import android.util.Log
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
    private val viewModel: PeliculaViewModel by lazy {
        ViewModelHolder.getSharedPeliculaViewModel()
    }



    private var peliculaExistente: Pelicula? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRegistroPeliculaBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Verificación (opcional)
        Log.d("ViewModelTest", "Registro VM Hash: ${viewModel.hashCode()}")


        configurarYearPicker()

        configurarSpinnerGenero()



        val pelicula = intent.getSerializableExtra("pelicula") as? Pelicula



        if (pelicula != null) {

            mostrarDetallesParaEditar(pelicula)

            peliculaExistente = pelicula

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

        peliculaExistente = pelicula



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


        // --- AÑADIMOS ESTO PARA IMPRIMIR LA LISTA ACTUALIZADA ---
        // Accedemos al valor actual de la lista de películas en el ViewModel
        val listaActualizada = viewModel.peliculas.value
        Log.d("RegistroPelicula", "Lista de películas después de modificar: $listaActualizada")
        // Puedes cambiar "RegistroPelicula" por el TAG que quieras para filtrar en Logcat.
        // Y "$listaActualizada" imprimirá la representación toString() de la lista de películas.
        // Si quieres ver cada película individualmente, puedes iterar:
        // listaActualizada?.forEach { Log.d("RegistroPelicula", "Pelicula: $it") }
        // --- FIN DE LA ADICIÓN ---


        Toast.makeText(this, "Película registrada", Toast.LENGTH_SHORT).show()

        finish()

    }



    private fun modificarPelicula() {

        peliculaExistente?.let {
            it.titulo = binding.etTitulo.text.toString()
            it.resenia = binding.etResenia.text.toString()
            it.anio = binding.npAnio.value
            it.puntuacion = binding.rbPuntuacion.rating.toInt()
            it.genero = Genero.valueOf(binding.spGenero.selectedItem.toString().uppercase())



            viewModel.actualizarPelicula(it)

            // --- AÑADIMOS ESTO PARA IMPRIMIR LA LISTA ACTUALIZADA ---
            // Accedemos al valor actual de la lista de películas en el ViewModel
            val listaActualizada = viewModel.peliculas.value
            Log.d("RegistroPelicula", "Lista de películas después de modificar: $listaActualizada")
            // Puedes cambiar "RegistroPelicula" por el TAG que quieras para filtrar en Logcat.
            // Y "$listaActualizada" imprimirá la representación toString() de la lista de películas.
            // Si quieres ver cada película individualmente, puedes iterar:
            // listaActualizada?.forEach { Log.d("RegistroPelicula", "Pelicula: $it") }
            // --- FIN DE LA ADICIÓN ---


            Toast.makeText(this, "Película modificada", Toast.LENGTH_SHORT).show()

            setResult(RESULT_OK)

            finish()

        }

    }



    private fun eliminarPelicula() {

        peliculaExistente?.let {

            viewModel.eliminarPeliculaPorId(it.id)

            // --- AÑADIMOS ESTO PARA IMPRIMIR LA LISTA ACTUALIZADA ---
            // Accedemos al valor actual de la lista de películas en el ViewModel
            val listaActualizada = viewModel.peliculas.value
            Log.d("RegistroPelicula", "Lista de películas después de modificar: $listaActualizada")
            // Puedes cambiar "RegistroPelicula" por el TAG que quieras para filtrar en Logcat.
            // Y "$listaActualizada" imprimirá la representación toString() de la lista de películas.
            // Si quieres ver cada película individualmente, puedes iterar:
            // listaActualizada?.forEach { Log.d("RegistroPelicula", "Pelicula: $it") }
            // --- FIN DE LA ADICIÓN ---

            Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show()

            setResult(RESULT_OK)

            finish()

        }

    }

}