package com.example.appparcial2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appparcial2.adapter.PeliculaAdapter
import com.example.appparcial2.databinding.ActivityMainBinding
import com.example.appparcial2.viewmodel.PeliculaViewModel
import android.util.Log
import android.widget.Toast
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PeliculaViewModel by lazy {
        ViewModelHolder.getSharedPeliculaViewModel()
    }

    private lateinit var peliculaAdapter: PeliculaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupRecyclerView()
        /* cargamos las peliculas desde el JSON */
        viewModel.cargarPeliculasDesdeArchivo(applicationContext)
        observePeliculas()
        setupFab()


        binding.btnResetear.setOnClickListener {
            val archivo = File(applicationContext.filesDir, "peliculas.json")
            if (archivo.exists()) {
                archivo.delete()
                Toast.makeText(this, "Películas reseteadas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay datos para borrar", Toast.LENGTH_SHORT).show()
            }

            viewModel.cargarPeliculasDesdeArchivo(applicationContext)
        }

    }


    private fun setupFab() {
        binding.fabAddPelicula.setOnClickListener {
            // Abrir la actividad de registro sin pasar datos (para nueva película)
            val intent = Intent(this, RegistroPeliculaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        peliculaAdapter = PeliculaAdapter(emptyList()) { pelicula ->
            val intent = Intent(this, RegistroPeliculaActivity::class.java).apply {
                putExtra("pelicula", pelicula)
            }
            startActivity(intent)

        }

        binding.rvPeliculas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = peliculaAdapter
        }
    }



    private fun observePeliculas() {
        viewModel.peliculas.observe(this) {
                peliculas -> peliculaAdapter.updatePeliculas(peliculas)
        }
    }

}