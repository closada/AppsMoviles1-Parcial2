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

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val viewModel: PeliculaViewModel by viewModels()
    private lateinit var peliculaAdapter: PeliculaAdapter


    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        observePeliculas()
        setupFab()

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